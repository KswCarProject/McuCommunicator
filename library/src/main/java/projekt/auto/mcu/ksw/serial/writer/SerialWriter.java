package projekt.auto.mcu.ksw.serial.writer;

import android.os.Build;

import java.io.FileOutputStream;
import java.io.IOException;

public class SerialWriter implements Writer {

    private final String mcuSource;

    public SerialWriter() {
        if (Build.VERSION.RELEASE.contains("11")) {
            mcuSource = "/dev/ttyHS1";
        } else if (Build.DISPLAY.contains("8937")) {
            mcuSource = "/dev/ttyHSL1";
        } else {
            mcuSource = "/dev/ttyMSM1";
        }
    }

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
