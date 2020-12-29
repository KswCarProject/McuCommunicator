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
    private McuAction handler;
    private LogcatReader readerThread;
    private byte[] frame;
    public static Reader mcuReader;

    private McuCommunicator() {

    }

    public static McuCommunicator getInstance() {
        if (instance == null)
            instance = new McuCommunicator();

        return instance;
    }

    public void sendCommand(int cmdType, byte[] data, boolean update) throws IOException, InvocationTargetException, IllegalAccessException {
        sendCommandViaShell(KSWobtain(cmdType, data, update));
    }

    public void sendCommand(McuCommands mcuCommands) throws IOException, InvocationTargetException, IllegalAccessException {
        sendCommand(mcuCommands.getCommand(), mcuCommands.getData(), mcuCommands.getUpdate());
    }

    public void killCommunicator() {
        instance = null;
    }

    private int checkSum() {
        int sum = 0;
        for (byte b = 1; b < frame.length - 1; b++)
            sum += frame[b];

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

    public interface McuAction {
        void update(int cmdType, byte[] data);
    }

    public interface Reader {
        public void startReading(McuAction notifier) throws Exception;
        public void stopReading();
        public boolean getReading();
    }

}
