package projekt.auto.mcu.ksw.serial.writer;

import java.io.FileOutputStream;
import java.io.IOException;

public class SerialWriter implements Writer {

    private final String mcuSource;

    public SerialWriter(String mcuSource) {
        this.mcuSource = mcuSource;
    }

    @Override
    public void writeCommand(byte[] frame) throws IOException {
        FileOutputStream fos = new FileOutputStream(mcuSource);
        fos.write(frame);
        fos.close();
    }

}
