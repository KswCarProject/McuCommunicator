package projekt.auto.mcu.ksw.serial.writer;

import java.io.IOException;

public interface Writer {
    void writeCommand(byte[] frame) throws IOException;
}
