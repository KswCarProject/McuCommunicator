package projekt.auto.mcu.ksw.serial.reader;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import projekt.auto.mcu.ksw.serial.McuCommunicator;

/**
 * Only use if no other client is reading Serial!!
 */
public class SerialReader implements Reader {

    public int readerInterval = 50;
    private boolean isReading = false;
    private String mcuSource = "/dev/ttyMSM1";
    private volatile byte[] frame;

    public SerialReader(String mcuSource) {
        this.mcuSource = mcuSource;
    }

    public SerialReader() {
    }

    public synchronized boolean getReading() {
        return isReading;
    }

    private synchronized void setReading(boolean b) {
        isReading = b;
    }

    @Override
    public void startReading(McuCommunicator.McuAction notifier) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(mcuSource);
        setReading(true);
        new Thread(() -> {
            while (getReading()) {
                frame = new byte[128];
                try {
                    int size = fis.read(frame);
                    if (size > 0) {
                        if (frame[0] != (byte) 242)
                            continue;
                        int cmdType = frame[2] & 0xFF;
                        byte[] dataBytes = new byte[frame[3] & 0xFF];
                        System.arraycopy(frame, 4, dataBytes, 0, dataBytes.length);
                        notifier.update(cmdType, dataBytes);
                        Thread.sleep(readerInterval);
                    }
                } catch (IOException | InterruptedException exception) {
                    Log.d("McuSerialReader", "Exception in SerialReader Thread " + exception.getLocalizedMessage());
                }
            }
        }).start();
    }

    @Override
    public void stopReading() {
        setReading(false);
    }
}
