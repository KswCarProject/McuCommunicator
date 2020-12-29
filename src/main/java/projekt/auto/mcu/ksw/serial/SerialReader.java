package projekt.auto.mcu.ksw.serial;

import android.util.Log;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SerialReader implements McuCommunicator.Reader {
    private boolean isReading = false;

    public synchronized boolean getReading() {
        return isReading;
    }

    private synchronized void setReading(boolean value) {
        isReading = value;
    }

    @Override
    public void startReading(McuCommunicator.McuAction notifier) throws FileNotFoundException {
        DataInputStream fis = new DataInputStream(new FileInputStream("/dev/ttyMSM1"));
        setReading(true);
        Thread reader = new Thread(() -> {
            try {
                while (isReading) {
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
                            notifier.update(cmdType, data);
                    }
                }
                fis.close();
            } catch (IOException innerE) {
                setReading(false);
                Log.e("Snaggle", "Exception in IO-Reader" + innerE);
            }
        });
        reader.start();
    }

    @Override
    public void stopReading() {
        isReading = false;
    }

    private int checkSum(int cmdType, byte[] data) {
        int sum = cmdType;
        for (byte b : data)
            sum += b;

        return 0xFF - sum;
    }
}
