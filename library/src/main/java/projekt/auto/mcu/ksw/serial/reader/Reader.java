package projekt.auto.mcu.ksw.serial.reader;

import projekt.auto.mcu.ksw.serial.McuCommunicator;

public interface Reader {
    void startReading(McuCommunicator.McuAction notifier) throws Exception;

    void stopReading();

    boolean getReading();
}