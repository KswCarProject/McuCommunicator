package projekt.auto.mcu.ksw.model;

import android.view.KeyEvent;

public class SystemStatus {
    public static final int TYPE_SYSTEM_STATUS = 1;
    public int ccd;
    public boolean dormant;
    public int epb;
    public KeyEvent event;
    public int ill;
    public int lastMode;
    public int rlight;
    public String topApp;
    public String topClass = "";
    public int acc = 2;
    public int screenSwitch = 2;

    public void parseFromMcuEvent(byte[] data) {
        this.ill = data[1] & 255 & 1;
        this.epb = data[1] & 255 & 8;
    }
}
