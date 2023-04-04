package projekt.auto.mcu.ksw.serial;

import android.os.Build;

import java.util.concurrent.atomic.AtomicReference;

import projekt.auto.mcu.ksw.serial.collection.McuCommands;
import projekt.auto.mcu.ksw.serial.reader.LogcatReader;
import projekt.auto.mcu.ksw.serial.reader.Reader;
import projekt.auto.mcu.ksw.serial.writer.SerialWriter;
import projekt.auto.mcu.ksw.serial.writer.Writer;

public class McuCommunicator {
    private static McuCommunicator instance;
    public Writer mcuWriter;
    public Reader mcuReader;
    public int beatTimerInterval = 1000;
    private volatile boolean isBeatRunning = false;

    public McuCommunicator(Writer mcuWriter, Reader mcuReader) {
        this.mcuWriter = mcuWriter;
        this.mcuReader = mcuReader;
    }

    public static McuCommunicator getInstance() {
        if (instance == null) {
            SerialWriter serialWriter;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q && Build.DISPLAY.contains("M600")) {
                serialWriter = new SerialWriter("/dev/ttyHS1");
            } else if (Build.DISPLAY.contains("8937")) {
                serialWriter = new SerialWriter("/dev/ttyHSL1");
            } else {
                serialWriter = new SerialWriter("/dev/ttyMSM1");
            }
            instance = new McuCommunicator(serialWriter, new LogcatReader());
        }
        return instance;
    }

    public void sendCommand(int cmdType, byte[] data, boolean update) throws Exception {
        mcuWriter.writeCommand(KSW_Obtain(cmdType, data, update));
    }

    public void sendCommand(McuCommands mcuCommands) throws Exception {
        sendCommand(mcuCommands.getCommand(), mcuCommands.getData(), mcuCommands.getUpdate());
    }

    public void killCommunicator() {
        stopBeat();
        instance = null;
    }

    public byte[] KSW_Obtain(int cmdType, byte[] data, boolean update) {
        byte[] frame = new byte[(data.length + 5)];
        int sum = 0;
        frame[0] = (byte) 242;
        frame[1] = update ? (byte) 160 : 0;
        frame[2] = (byte) cmdType;
        frame[3] = (byte) data.length;
        System.arraycopy(data, 0, frame, 4, data.length);
        for (byte b = 1; b < frame.length - 1; b++)
            sum += frame[b];
        frame[frame.length - 1] = (byte) (0xFF - sum);

        return frame;
    }

    public void startBeat() throws Exception {
        if (isBeatRunning) {
            return;
        }
        isBeatRunning = true;
        AtomicReference<Exception> outerExc = new AtomicReference<>();
        byte[] beatData = {0x8, 0x0};
        Thread beatThread = new Thread(() -> {
            while (isBeatRunning) {
                try {
                    sendCommand(0x68, beatData, false);
                    Thread.sleep(beatTimerInterval);
                } catch (Exception exception) {
                    outerExc.set(exception);
                }
            }
        });
        beatThread.start();
        if (outerExc.get() != null)
            throw outerExc.get();
    }

    public void stopBeat() {
        isBeatRunning = false;
    }

    public interface McuAction {
        void update(int cmdType, byte[] data);
    }

}
