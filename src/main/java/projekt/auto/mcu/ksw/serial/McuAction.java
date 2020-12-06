package projekt.auto.mcu.ksw.serial;

public interface McuAction {
    void update(int cmdType, byte[] data);
    void update(String logcatMessage);
}
