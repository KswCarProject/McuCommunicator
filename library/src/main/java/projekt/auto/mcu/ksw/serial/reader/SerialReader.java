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
        new Thread(() -> {
            setReading(true);
            int cmdType;
            byte[] dataBytes;
            frame = new byte[1024];
            int index;
            while (getReading()) {
                try {
                    int size = fis.read(frame);
                    for (index = 0; index < size; index++) {
                        if (frame[index] != (byte) 242)
                            continue;
                        if (index + 4 > size) { //Overflow
                            break;
                        }
                        index+=2;
                        cmdType = frame[index++] & 0xFF;
                        dataBytes = new byte[frame[index++] & 0xFF];
                        if (index + dataBytes.length > size) { //Overflow
                            break;
                        }
                        System.arraycopy(frame, index, dataBytes, 0, dataBytes.length);
                        notifier.update(cmdType, dataBytes);
                    }
                    Thread.sleep(readerInterval);
                } catch (IOException | InterruptedException exception) {
                    Log.d("McuSerialReader", "Exception in SerialReader Thread " + exception.getLocalizedMessage());
                }
            }
        }).start();
        while(!getReading()); //Wait until the thread actually started to allow replyable commands right after start
    }

    @Override
    public void stopReading() {
        setReading(false);
    }
}
