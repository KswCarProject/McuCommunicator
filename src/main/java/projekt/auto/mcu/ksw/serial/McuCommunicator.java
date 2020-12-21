package projekt.auto.mcu.ksw.serial;

import android.util.Log;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class McuCommunicator {
    private static McuCommunicator instance;
    private final AtomicBoolean isReading = new AtomicBoolean();
    private McuAction handler;
    private LogcatReader readerThread;
    private byte[] frame;

    public interface McuAction {
        void update(int cmdType, byte[] data);
    }

    private McuCommunicator() {

    }

    public static McuCommunicator getInstance() {
        if (instance == null)
            instance = new McuCommunicator();

        return instance;
    }

    public McuCommunicator startReading(McuAction handler) throws FileNotFoundException {
        if (handler != null) {
            this.handler = handler;
            readerThread = new LogcatReader(handler);
            readerThread.startReading();
        }
        return this;
    }

    private void startReadingFromIO() throws FileNotFoundException {
        final AtomicReference<Exception> exception = new AtomicReference<>();
        DataInputStream fis = new DataInputStream(new FileInputStream("/dev/ttyMSM1"));
        isReading.set(true);
        new Thread(() -> {
            try {
                while (isReading.get()) {
                    if (fis.available() > 5) {
                        if (fis.readByte() != 0xf2)
                            continue;
                        fis.readByte();
                        int cmdType = fis.readByte();
                        int length = fis.readByte();
                        byte[] data = new byte[length];
                        for (int i = 0; i < length; i++) {
                            data[i] = fis.readByte();
                        }
                        if (fis.readByte() == checkSum(cmdType, data))
                            handler.update(cmdType, data);
                    }
                }
                fis.close();
            } catch (Exception innerE) {
                isReading.set(false);
                Log.d("Snaggle", "Exception in IO-Reader" + innerE);
                exception.set(innerE);
            }
        }).start();
    }

    public McuCommunicator stopReading() {
        if (handler != null && readerThread != null) {
            readerThread.stopReading();
            isReading.set(false);
        }
        return this;
    }

    public void sendCommand(int cmdType, byte[] data, boolean update) throws IOException, InvocationTargetException, IllegalAccessException {
        sendCommandViaShell(KSWobtain(cmdType, data, update));
    }

    public void sendCommand(McuCommands mcuCommands) throws IOException, InvocationTargetException, IllegalAccessException {
        sendCommand(mcuCommands.getCommand(), mcuCommands.getData(), mcuCommands.getUpdate());
    }

    public void killCommunicator() {
        stopReading();
        instance = null;
    }

    private int checkSum() {
        int sum = 0;
        for (byte b = 1; b < frame.length - 1; b++)
            sum += frame[b];

        return 0xFF - sum;
    }

    private int checkSum(int cmdType, byte[] data) {
        int sum = cmdType;
        for (byte b : data)
            sum += b;

        return 0xFF - sum;
    }

    public byte[] KSWobtain(int cmdType, byte[] data, boolean update) {
        frame = new byte[(data.length + 5)];
        frame[0] = (byte) 242;
        frame[1] = update ? (byte) 160 : 0;
        frame[2] = (byte) cmdType;
        frame[3] = (byte) data.length;
        System.arraycopy(data, 0, frame, 4, data.length);
        frame[frame.length - 1] = (byte) checkSum();

        return frame;
    }

    private void sendCommandViaShell(byte[] frame) throws IOException {
        FileOutputStream fos = new FileOutputStream("/dev/ttyMSM1");
        fos.write(frame);
        fos.close();
    }

}
