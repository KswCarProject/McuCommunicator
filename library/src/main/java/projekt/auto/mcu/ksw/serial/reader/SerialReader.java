package projekt.auto.mcu.ksw.serial.reader;

import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import projekt.auto.mcu.ksw.serial.McuCommunicator;

/**
 * Only use if no other client is reading Serial!!
 */
public class SerialReader implements Reader {

    public int readerInterval = 50;
    private boolean isReading = false;
    private String mcuSource = "/dev/ttyMSM1";

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

        // Multiplier of 2 - selected by the rule of thumb. Smaller values enable .read to block thread for a shorter time - https://docs.oracle.com/javase/6/docs/api/java/io/FileInputStream.html#read%28%29
        int BUFFER_SIZE = 64;

        // Maximum size of the MCU message + 1 byte for error. start(1) + empty(1) + command(1) + datalen(1) + ...datamax(16) + checksum(1)
        int MAX_MESSAGE = 22;

        new Thread(() -> {
            int prevSize = 0;
            // prevRead is used to keep track of messages started (but not fully retrieved) in the very end of the read
            // initialised with twice a MAX_MESSAGE to provide enough space for any consecutive errors
            byte[] prevRead = new byte[MAX_MESSAGE * 2];

            int indexStart = -1;
            int indexEnd = 0;
            int dataLen = 0;
            int checksum = 0;
            int command = 0;

            setReading(true);
            while (getReading()) {
                try {
                    byte[] nextRead = new byte[BUFFER_SIZE];
                    byte[] data = new byte[BUFFER_SIZE * 2];
                    int nextSize = fis.read(nextRead);
                    byte currentAsByte;
                    int current;
                    int next;

                    if (nextSize == 0) {
                        Thread.sleep(readerInterval);
                    }

                    for (int i = 0; i < prevSize + nextSize; i += 1) {
                        currentAsByte = (i >= prevSize ? nextRead[i - prevSize] : prevRead[i]);
                        // And 255 bytes. JAVA converts bytes to signed integers, bitwise operation bring them back to their unsigned - positive values
                        current = (currentAsByte < 0) ?  currentAsByte & 255 : currentAsByte;
                        next = (i < prevSize + nextSize) ? (((i + 1) >= prevSize ? nextRead[(i + 1) - prevSize] : prevRead[(i + 1)])) : -1;

                        // The start of the data: 0xF2 == -14 (signed) == 242 (unsigned) and is followed by empty byte
                        if (current == 242 && next == 0 && indexStart == -1) {
                            indexStart = i;
                            indexEnd = 0;
                        } else if (indexStart != -1) {
                                // Empty byte
                            if (i == indexStart + 1) {
                                checksum = current;
                            }
                                // Command byte
                            else if (i == indexStart + 2) {
                                command = current;
                                checksum += current;
                                // Data len byte
                            } else if (i == indexStart + 3) {
                                dataLen = current;
                                checksum += (current & 0xFF);
                                data = new byte[dataLen];
                                // Data bytes
                            } else if (i > indexStart + 3 && i < indexStart + 4 + dataLen) {
                                data[i - (indexStart + 4)] = currentAsByte;
                                checksum += current;
                                // Checksum byte after all of the data bytes
                            } else if (i == indexStart + 4 + dataLen) {
                                boolean isValid = (checksum ^ 255) == current;
                                if (isValid) {
                                    notifier.update(command, Arrays.copyOfRange(data, 0, dataLen));
                                    indexEnd = i + 1;
                                }
                                indexStart = -1;
                                dataLen = 0;
                            }
                        }
                    }
                    // next prevSize is the number of any not processed bytes in the buffer (those after the checksum of the last fully retrieved command)
                    int nextPrevSize = Math.min(MAX_MESSAGE * 2,  ((prevSize + nextSize) - indexEnd));
                    for (int i = 0; i < nextPrevSize; i += 1) {
                        int realI = ((prevSize + nextSize) - nextPrevSize) + i;
                        prevRead[i] = realI >= prevSize ? nextRead[realI - prevSize] : prevRead[realI];
                    }

                    prevSize = nextPrevSize;

                } catch (Exception e) {
                    Log.d("McuSerialReader", "Exception in SerialReader Thread " + e.getLocalizedMessage());
                }
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        while(!getReading()); //Wait until the thread actually started to allow replyable commands right after start
    }

    @Override
    public void stopReading() {
        setReading(false);
    }
}
