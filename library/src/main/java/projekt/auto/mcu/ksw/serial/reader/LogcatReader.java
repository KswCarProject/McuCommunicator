package projekt.auto.mcu.ksw.serial.reader;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;

import projekt.auto.mcu.BuildConfig;
import projekt.auto.mcu.ksw.serial.McuCommunicator;

public class LogcatReader implements Reader {
    public int readerInterval = 250;
    private Process logProc;
    private boolean isReading = false;

    public LogcatReader() {
    }

    public LogcatReader(int readerInterval) {
        this.readerInterval = readerInterval;
    }

    public void startReading(McuCommunicator.McuAction notifier) {
        if (isReading) return;
        Thread readerThread = new Thread(() -> {
            try {
                Runtime.getRuntime().exec("logcat -c\n");
                logProc = Runtime.getRuntime().exec("logcat KswMcuListener:I *:S");
                isReading = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedReader bufRead = new BufferedReader(new InputStreamReader(logProc.getInputStream()));
            String line;
            try {
                while (isReading) {
                    try {
                        while (bufRead.ready()) {
                            line = bufRead.readLine();
                            if (line.contains("--Mcu toString-----")) {
                                line = line.substring(line.lastIndexOf('[') + 2, line.lastIndexOf(']') - 1);

                                line = line.replaceAll("\\s+", "");
                                String[] splitString = line.split("-", 2);
                                String commandStr = splitString[0].substring(splitString[0].indexOf(":") + 1);
                                int command = Integer.parseInt(commandStr, 16);
                                String byteStrs = splitString[1].substring(splitString[1].indexOf(":") + 1);
                                String[] dataStrs = byteStrs.split("-");
                                byte[] data = new byte[dataStrs.length];
                                for (int i = 0; i < data.length; i++) {
                                    data[i] = (byte) (Integer.parseInt(dataStrs[i], 16));
                                }

                                if (BuildConfig.DEBUG)
                                    Log.d("LogcatReader", "Mcu reader: " + command);
                                for (Byte b : data)
                                    if (BuildConfig.DEBUG)
                                        Log.d("LogcatReader", "Mcu reader: " + b);

                                notifier.update(command, data);
                            }
                        }
                        try {
                            //noinspection BusyWait
                            Thread.sleep(readerInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedIOException e) {
                        break;
                    }
                }
                isReading = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        readerThread.start();
    }

    public void stopReading() {
        if (logProc != null) logProc.destroy();
        isReading = false;
    }

    @Override
    public boolean getReading() {
        return isReading;
    }
}
