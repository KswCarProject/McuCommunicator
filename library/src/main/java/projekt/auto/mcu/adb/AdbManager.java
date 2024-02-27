package projekt.auto.mcu.adb;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import projekt.auto.mcu.adb.lib.AdbConnection;
import projekt.auto.mcu.adb.lib.AdbCrypto;
import projekt.auto.mcu.adb.lib.AdbStream;

public class AdbManager implements AutoCloseable {
    private boolean isConnected = false;
    private Socket socket;
    private AdbConnection adbConnection;
    private AdbStream shellStream;
    private Optional<ShellObserver> shellObserver = Optional.empty();
    private Optional<ShellResultObserver> shellResultObserver = Optional.empty();
    private String identifier = "";
    private String currentCommand = "";
    private String buffer = "";
    private final Queue<String> commandWheel = new LinkedList<>();
    private final LinkedList<String> collectedLinesForCommand = new LinkedList<>();
    private final ExecutorService sender = Executors.newSingleThreadExecutor();
    private final ExecutorService receiver = Executors.newCachedThreadPool();

    private final Thread shellReaderWorker = new Thread(() -> {
        while (isConnected) {
            try {
                final String shellText = new String(shellStream.read(), StandardCharsets.UTF_8);
                String text = buffer + shellText;
                char[] shellCharacters = text.toCharArray();
                final ArrayList<String> splitShellLines = new ArrayList<>();
                StringBuilder lineBuilder = new StringBuilder();
                boolean isOverLine = false;
                for (int i = 0; i < shellCharacters.length; i++) {
                    char shellChar = shellCharacters[i];
                    if (shellChar == '<' && i < shellCharacters.length - 1 && shellCharacters[i + 1] == '\b') {
                        isOverLine = true;
                    } else if (shellChar == '\b') {
                        lineBuilder.setLength(lineBuilder.length() - 1);
                    } else {
                        if (isOverLine) {
                            lineBuilder.setLength(lineBuilder.indexOf("\r"));
                            isOverLine = false;
                        }
                        lineBuilder = lineBuilder.append(shellChar);
                    }
                    if (shellChar == '\n') {
                        splitShellLines.add(lineBuilder.toString());
                        lineBuilder.setLength(0);
                    }
                }
                if (lineBuilder.length() > 0) {
                    splitShellLines.add(lineBuilder.toString());
                }
                for (String line : splitShellLines) {
                    if (!line.endsWith("\r\n")) {
                        if (!(currentCommand != null && currentCommand.equals("")) && commandWheel.isEmpty()) { //Last command
                            this.shellObserver.ifPresent(s -> s.updateShell(line));
                            identifier = line;
                            this.shellResultObserver.ifPresent(this::submitResultingCommand);
                            synchronized (commandWheel) {
                                if (commandWheel.isEmpty()) {
                                    commandWheel.notifyAll();
                                }
                            }
                            currentCommand = "";
                        } else {
                            buffer = line;
                        }
                    } else {
                        buffer = "";
                        final String finalLine = line.substring(0, line.length() - 2);
                        if (commandWheel.contains(finalLine)) { //From writer
                            continue;
                        }
                        this.shellObserver.ifPresent(s -> s.updateShell(line));
                        String nextInCommand = Optional.ofNullable(commandWheel.peek()).orElse("");
                        if (!nextInCommand.isEmpty() && finalLine.endsWith(nextInCommand + "\r")) { //Return
                            commandWheel.remove();
                            if (!currentCommand.isEmpty()) {
                                identifier = finalLine.substring(0, finalLine.indexOf(nextInCommand));
                                this.shellResultObserver.ifPresent(this::submitResultingCommand);
                            }
                            collectedLinesForCommand.clear();
                            currentCommand = nextInCommand;
                        } else {
                            collectedLinesForCommand.add(finalLine);
                        }
                    }
                }
            } catch (IOException ignored) {
            } catch (InterruptedException e) {
                break;
            }
        }
    });

    private void submitResultingCommand(ShellResultObserver shellResultObserver) {
        final String command = currentCommand;
        final String[] lines = collectedLinesForCommand.toArray(new String[0]);
        String[] splitIdent = identifier.split("\\|");
        final boolean success;
        if (splitIdent.length == 2) {
            success = splitIdent[0].equals("0");
            identifier = splitIdent[1];
        } else {
            success = true;
        }
        receiver.submit(() ->
                shellResultObserver.updateResult(command, lines, success)
        );
    }

    private void connect(File filesDir) throws IOException, InterruptedException, NoSuchAlgorithmException {
        if (isConnected) {
            disconnect();
        }
        AdbCrypto adbCrypto = setupCrypto(filesDir);
        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 5555), 5000);
        adbConnection = AdbConnection.create(socket, adbCrypto);
        adbConnection.connect();
        shellStream = adbConnection.open("shell:");
        isConnected = true;
    }

    private AdbCrypto setupCrypto(File fileDir) throws NoSuchAlgorithmException, IOException {
        File publicKey = new File(fileDir, "public.key");
        File privateKey = new File(fileDir, "private.key");
        AdbCrypto c = null;
        if (publicKey.exists() && privateKey.exists()) {
            try {
                c = AdbCrypto.loadAdbKeyPair(privateKey, publicKey);
            } catch (Exception ignored) {
                Log.w("AdbManager", "Failed reading existing key files");
            }
        }
        if (c == null) {
            c = AdbCrypto.generateAdbKeyPair();
            c.saveAdbKeyPair(privateKey, publicKey);
        }
        return c;
    }

    public AdbManager(Context context) throws AdbConnectionException, ExecutionException, InterruptedException {
        Exception error = receiver.submit(() -> {
            try {
                connect(context.getFilesDir());
                shellReaderWorker.start();
                return null;
            } catch (Exception e) {
                return e;
            }
        }).get();
        if (error != null) {
            isConnected = false;
            throw new AdbConnectionException(error);
        }
    }

    public AdbManager(Context context, ShellObserver shellObserver) throws AdbConnectionException, InterruptedException, ExecutionException {
        this(context);
        this.shellObserver = Optional.ofNullable(shellObserver);
    }

    public AdbManager(Context context, ShellResultObserver shellResultObserver) throws AdbConnectionException, InterruptedException, ExecutionException {
        this(context);
        this.shellResultObserver = Optional.ofNullable(shellResultObserver);
    }

    public AdbManager(Context context, ShellObserver shellObserver, ShellResultObserver shellResultObserver) throws AdbConnectionException, ExecutionException, InterruptedException {
        this(context);
        this.shellObserver = Optional.ofNullable(shellObserver);
        this.shellResultObserver = Optional.ofNullable(shellResultObserver);
    }

    public synchronized void sendCommands(String... commands) throws AdbConnectionException {
        if (!isConnected) {
            throw new AdbConnectionException(new Exception("Not connected anymore!"));
        }
        for (String command : commands) {
            if (command == null || command.isEmpty())
                continue;

            boolean isAcceptedCommand = true;
            for (char c : command.toCharArray()) {
                if (c < ' ' || c > '~') {
                    isAcceptedCommand = false;
                    break;
                }
            }

            if (isAcceptedCommand) {
                commandWheel.add(command);
            }
            sender.submit(() -> {
                String line = command + "\n";
                try {
                    shellStream.write(line.getBytes(StandardCharsets.UTF_8), true);
                } catch (IOException | InterruptedException e) {
                    commandWheel.remove(command);
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void disconnect() throws IOException, InterruptedException {
        if (isConnected) {
            synchronized (commandWheel) {
                while (!commandWheel.isEmpty()) {
                    commandWheel.wait();
                }
            }
            sender.shutdown();
            receiver.shutdown();
            isConnected = false;
            shellReaderWorker.interrupt();
            shellStream.close();
            adbConnection.close();
            socket.close();
        }
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    static class AdbConnectionException extends Exception {
        private final Exception error;

        private AdbConnectionException(Exception error) {
            this.error = error;
        }

        @Override
        public String getLocalizedMessage() {
            return error.getLocalizedMessage();
        }

        @NonNull
        @Override
        public StackTraceElement[] getStackTrace() {
            return error.getStackTrace();
        }
    }

    public interface ShellObserver {
        void updateShell(String text);
    }

    public interface ShellResultObserver {
        void updateResult(String command, String[] lines, boolean success);
    }
}