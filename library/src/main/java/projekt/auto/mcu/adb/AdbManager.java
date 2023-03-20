package projekt.auto.mcu.adb;

import android.content.Context;
import android.net.TrafficStats;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

import projekt.auto.mcu.adb.lib.AdbConnection;
import projekt.auto.mcu.adb.lib.AdbCrypto;
import projekt.auto.mcu.adb.lib.AdbStream;

public class AdbManager {

    private static AdbCrypto setupCrypto(File fileDir) throws NoSuchAlgorithmException, IOException {
        File publicKey = new File(fileDir, "public.key");
        File privateKey = new File(fileDir, "private.key");
        AdbCrypto c = null;

        if (publicKey.exists() && privateKey.exists()) {
            try {
                c = AdbCrypto.loadAdbKeyPair(privateKey, publicKey);
            } catch (Exception ignored) {
            }
        }

        if (c == null) {
            c = AdbCrypto.generateAdbKeyPair();
            c.saveAdbKeyPair(privateKey, publicKey);
        }

        return c;
    }

    public static boolean executeCommands(Context context, String[] commands) {
        final AtomicReference<Exception> exception = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                TrafficStats.setThreadStatsTag((int) Thread.currentThread().getId());
                AdbCrypto adbCrypto = setupCrypto(context.getFilesDir());
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", 5555), 5000);
                AdbConnection adbConnection = AdbConnection.create(socket, adbCrypto);
                adbConnection.connect();

                AdbStream shellStream = adbConnection.open("shell:");
                for (String command : commands) {
                    shellStream.write(command + "\n");
                }
                shellStream.close();
                adbConnection.close();
                socket.close();
            } catch (Exception innerException) {
                exception.set(innerException);
            }
        });

        try {
            thread.start();
            thread.join();
            return exception.get() == null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
