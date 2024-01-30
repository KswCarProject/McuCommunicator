package projekt.auto.mcu.ksw.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author Snaggly
 * This Class is an altered version taken fromm the Ksw Sources
 * The changes include mainly the removal of functional methods
 * and addition of parsing logic.
 * <p>
 * Use it to parse CarData and have an object to broadcast to clients!
 * Predertime the Event type with McuEventLogic before parsing.
 */
public class McuStatus {
    public static final int ANDROID_MODE = 1;
    public static final int CAR_MODE = 2;
    public static final int TYPE_MCU_STATUS = 5;
    public ACData acData = new ACData();
    public BenzData benzData = new BenzData();
    public CarData carData = new CarData();
    public MediaData mediaData = new MediaData();
    public MediaStringInfo mediaStringInfo = new MediaStringInfo();
    public MediaPlayStatus mediaPlayStatus = new MediaPlayStatus();
    public DiscStatus discStatus = new DiscStatus();
    public EqData eqData = new EqData();
    public CarBluetoothStatus bluetoothStatus = new CarBluetoothStatus();
    public String mcuVerison;
    public int systemMode = 1;

    public static final class MediaType {
        public static final int SRC_ALL_APP = 13;
        public static final int SRC_AUX = 6;
        public static final int SRC_BT = 3;
        public static final int SRC_BT_MUSIC = 4;
        public static final int SRC_CAR = 0;
        public static final int SRC_CAR_FM = 14;
        public static final int SRC_DTV = 9;
        public static final int SRC_DVD = 8;
        public static final int SRC_DVD_YUV = 12;
        public static final int SRC_DVR = 5;
        public static final int SRC_F_CAM = 11;
        public static final int SRC_MUSIC = 1;
        public static final int SRC_PHONELINK = 7;
        public static final int SRC_RADIO = 10;
        public static final int SRC_VIDEO = 2;
    }

    public McuStatus() {
    }

    public McuStatus(int systemMode2, String mcuVerison2) {
        this.systemMode = systemMode2;
        this.mcuVerison = mcuVerison2;
    }

    public int getSystemMode() {
        return this.systemMode;
    }

    public void setSystemMode(int systemMode2) {
        this.systemMode = systemMode2;
    }

    public String getMcuVerison() {
        return this.mcuVerison;
    }

    public void setMcuVerison(String mcuVerison2) {
        this.mcuVerison = mcuVerison2;
    }

    public CarData getCarData() {
        return this.carData;
    }

    public void setCarData(CarData carData2) {
        this.carData = carData2;
    }

    public ACData getAcData() {
        return this.acData;
    }

    public void setAcData(ACData acData2) {
        this.acData = acData2;
    }

    public static class ACData {
        public static final int LEFT_ABOVE = 128;
        public static final int LEFT_AUTO = 16;
        public static final int LEFT_BELOW = 32;
        public static final int LEFT_FRONT = 64;
        public static final int RIGHT_ABOVE = 8;
        public static final int RIGHT_AUTO = 1;
        public static final int RIGHT_BELOW = 2;
        public static final int RIGHT_FRONT = 4;
        public static int i = 0;
        public boolean AC_Switch;
        public boolean autoSwitch;
        public boolean backMistSwitch;
        public boolean eco;
        public boolean frontMistSwitch;
        public boolean isOpen;
        public float leftTmp;
        public int loop;
        public int mode;
        public float rightTmp;
        public float speed;
        public boolean sync;

        private float getTmp(int dataTmp) {
            if (dataTmp == -1 || dataTmp == 0) {
                return (float) dataTmp;
            }
            return (((float) (dataTmp - 1)) * 0.5f) + 16.0f;
        }

        public void setRightTmp(int rightTmp2) {
            this.rightTmp = getTmp(rightTmp2);
        }

        public void setLeftTmp(int leftTmp2) {
            this.leftTmp = getTmp(leftTmp2);
        }

        public boolean isOpen(int type) {
            return (this.mode & type) != 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        public void parseFromACDataEvent(byte[] data) {
            this.isOpen = (data[1] & 128) != 0;
            this.AC_Switch = (data[1] & 64) != 0;
            this.loop = (data[1] & 32) != 0 ? 1 : 0;
            this.loop = (data[1] & 16) != 0 ? 2 : this.loop;
            this.frontMistSwitch = (data[1] & 8) != 0;
            this.backMistSwitch = (data[1] & 2) != 0;
            this.sync = (data[1] & 1) != 0;
            this.mode = data[2];
            this.setLeftTmp((int) data[3]);
            this.setRightTmp((int) data[4]);
            this.eco = (data[5] & 32) != 0;
            this.autoSwitch = (data[5] & 16) != 0;
            this.speed = (data[5] & 15) * 0.5f;
        }
    }

    public static class BenzData {
        public static final int AIR_MATIC_STATUS = 2;
        public static final int AUXILIARY_RADAR = 3;
        public static final int HIGH_CHASSIS_SWITCH = 1;
        public boolean airBagSystem;
        public int airMaticStatus;
        public boolean auxiliaryRadar;
        public boolean highChassisSwitch;
        public int key3 = 0;
        public int light1 = 0;
        public int light2 = 0;
        public int pressButton;

        public void parseFromBenzDataEvent(byte[] data) {
            this.highChassisSwitch = data[0] == 1;
            this.airMaticStatus = data[1];
            this.auxiliaryRadar = data[2] == 1;
            this.light1 = data[3];
            this.light2 = data[4];
            this.airBagSystem = data[5] == 1;
        }
    }

    public static class CarData {
        public static final int AHEAD_COVER = 8;
        public static final int BACK_COVER = 4;
        public static final int GEAR_D = 4;
        public static final int GEAR_N = 2;
        public static final int GEAR_P = 0;
        public static final int GEAR_R = 6;
        public static final int LEFT_AHEAD = 16;
        public static final int LEFT_BACK = 64;
        public static final int LLIGHT_OFF = 0;
        public static final int LLIGHT_ON = 8;
        public static final int RIGHT_AHEAD = 32;
        public static final int RIGHT_BACK = 128;
        public static final int RLIGHT_OFF = 0;
        public static final int RLIGHT_ON = 16;
        public float airTemperature;
        public float averSpeed;
        public int carDoor;
        public int carGear;
        public int distanceUnitType;
        public int engineTurnS;
        public boolean handbrake;
        public int mileage;
        public int oilSum;
        public int oilUnitType;
        public float oilWear;
        public boolean safetyBelt;
        public int signalDouble;
        public int signalLeft;
        public int signalRight;
        public int speed;
        public int temperatureUnitType;
        public int carWheelAngle;
        public int frontRadarDataL;
        public int frontRadarDataLM;
        public int frontRadarDataR;
        public int frontRadarDataRM;
        public int backRadarDataL;
        public int backRadarDataLM;
        public int backRadarDataR;
        public int backRadarDataRM;

        public void parseFromBrakeBeltEvent(byte[] data) {
            this.handbrake = ((data[1] & 255) & 8) != 0;
            this.safetyBelt = ((data[2] & 255) & 1) != 0;
            this.carGear = data[2] & 255 & 6;
            this.signalLeft = data[2] & 255 & 8;
            this.signalRight = data[2] & 255 & 16;
            this.signalDouble = data[2] & 255 & 32;
        }

        public void parseFromDoorEvent(byte[] data) {
            this.carDoor = data[1] & 255;
        }

        public void parseFromCarDataEvent(byte[] data) {
            this.mileage = ((data[1] & 255) << 8) + (data[2] & 255);
            this.oilWear = (((data[3] & 255) << 8) + (data[4] & 255)) / 10.0f;
            this.averSpeed = (((data[5] & 255) << 8) + (data[6] & 255)) / 10.0f;
            this.speed = ((data[7] & 255) << 8) + (data[8] & 255);
            this.engineTurnS = ((data[9] & 255) << 8) + (data[10] & 255);
            this.oilSum = ((data[11] & 255) << 8) + (data[12] & 255);
            if ((data[13] & 128) > 0) {
                int temp = (~(((data[13] & 255) * 256) + (data[14] & 255))) + 1;
                this.airTemperature = (float) ((65535 & temp) * (-0.1d));
            } else {
                int temp2 = ((data[13] & 255) * 256) + (data[14] & 255);
                this.airTemperature = (float) ((65535 & temp2) * 0.1d);
            }
            this.distanceUnitType = data[15] & 8;
            this.oilUnitType = data[15] & 2;
            this.oilUnitType += data[15] & 255 & 1;
        }

        public void parseFromWheelAngleEvent(byte[] data) {
            this.carWheelAngle = ((data[1] & 255) * 256) + (data[2] & 255);
        }

        public void parseFromFrontRadarEvent(byte[] data) {
            this.frontRadarDataL = data[1] & 255;
            this.frontRadarDataLM = data[2] & 255;
            this.frontRadarDataRM = data[3] & 255;
            this.frontRadarDataR = data[4] & 255;
        }

        public void parseFromBackRadarEvent(byte[] data) {
            this.backRadarDataL = data[1] & 255;
            this.backRadarDataLM = data[2] & 255;
            this.backRadarDataRM = data[3] & 255;
            this.backRadarDataR = data[4] & 255;
        }
    }

    public static class MediaData {
        public static final int TYPE_AUX = 20;
        public static final int TYPE_BT = 21;
        public static final int TYPE_DISC = 16;
        public static final int TYPE_FM = 1;
        public static final int TYPE_MODE = 64;
        public static final int TYPE_MP3 = 18;
        public static final int TYPE_USB = 17;
        public boolean ALS;
        public boolean RAND;
        public boolean RPT;
        public boolean SCAN;
        public boolean ST;
        public String status;
        public int times;
        public int type;
        public Fm fm = new Fm();
        public Disc disc = new Disc();
        public Usb usb = new Usb();
        public DVD dvd = new DVD();
        public MODE mode = new MODE();
        public MP3 mp3 = new MP3();

        public int getVolumeFromEvent(byte[] data) {
            return data[0] & 127;
        }

        public boolean getMuteFromEvent(byte[] data) {
            return (data[0] & 128) > 0;
        }

        public static class DVD extends BaseMediaInfo {
            public int chapterNumber;
            public int min;
            public int sec;
            public int totalChapter;
        }

        public static class Disc extends BaseMediaInfo {
            public int min;
            public int number;
            public int sec;
            public int track;

            public void parseFromMediaEvent(byte[] data, MediaStringInfo currentMediaString) {
                this.number = data[1];
                this.track = data[2];
                this.min = data[5];
                this.sec = data[6];
                currentMediaString.min = this.min;
                currentMediaString.sec = this.sec;
            }
        }

        public static class Fm {
            public String freq;
            public String name;
            public int preFreq;

            public void parseFMFromEvent(byte[] data) {
                if (data[1] == 3) {
                    this.name = "FM" + data[1];
                    this.freq = ((((data[2] & 255) << 8) + (data[3] & 255)) / 100.0f) + "Mhz";
                    this.preFreq = data[4];
                }
                else if (data[1] == 19) {
                    this.name = "AM" + (data[1] - 16);
                    this.freq = (((data[2] & 255) << 8) + (data[3] & 255)) + "Khz";
                    this.preFreq = data[4];
                }
                else if (data[1] == -1) {
                    this.name = "-";
                }
                if (data[2] == -1) {
                    this.freq = "-";
                }
            }
        }

        public static class MODE extends BaseMediaInfo {
            public boolean ASL;
            public boolean PAUSE;
            public boolean RAND;
            public boolean RPT;
            public boolean SCAN;
            public boolean ST;

            public void parseFromMediaEvent(byte[] data) {
                this.ASL = data[1] == 1;
                this.ST = data[2] == 1;
                this.RAND = data[3] == 1;
                this.RPT = data[4] == 1;
                this.PAUSE = data[5] == 1;
                this.SCAN = data[6] == 1;
            }
        }

        public static class MP3 extends BaseMediaInfo {
            public int fileNumber;
            public int folderNumber;
            public int min;
            public int sec;

            public void parseFromMediaEvent(byte[] data, MediaStringInfo currentMediaString) {
                this.folderNumber = data[1] + (data[2] << 8);
                this.fileNumber = data[3] + (data[4] << 8);
                this.min = data[5];
                this.sec = data[6];
                currentMediaString.min = this.min;
                currentMediaString.sec = this.sec;
            }
        }

        public static class Usb extends BaseMediaInfo {
            public int fileNumber;
            public int folderNumber;
            public int min;
            public int sec;

            public void parseFromMediaEvent(byte[] data, MediaStringInfo currentMediaString) {
                this.folderNumber = data[1] + (data[2] << 8);
                this.fileNumber = data[3] + (data[4] << 8);
                this.min = data[5];
                this.sec = data[6];
                currentMediaString.min = this.min;
                currentMediaString.sec = this.sec;
            }
        }

        public BaseMediaInfo getCurrentMediaInfo() {
            int i;
            if (!(this.type == 0 || (i = this.type) == 1)) {
                if (i == 64) {
                    return this.mode;
                }
                switch (i) {
                    case 16:
                        return this.disc;
                    case 17:
                        return this.usb;
                    case 18:
                        return this.mp3;
                    default:
                        switch (i) {
                            case 20:
                            case 21:
                                break;
                            default:
                                return null;
                        }
                }
            }
            return null;
        }

        public static class BaseMediaInfo {
            public String name = "";
            public String artist = "";
            public String album = "";
            public String folderName = "";

            public void reset() {
                this.name = "";
                this.artist = "";
                this.album = "";
            }
        }

        public void parseFromMediaEvent(byte[] data) {
            this.times++;
            this.type = data[0];
        }

        public boolean parseMediaStringInfoEvent(byte[] data, MediaStringInfo mediaStringInfo) throws UnsupportedEncodingException {
            if (mediaStringInfo != null) {
                mediaStringInfo.times++;
                String info = autoGetString(data);
                BaseMediaInfo currentMediaInfo;
                if (!((currentMediaInfo = getCurrentMediaInfo()) == null)) {
                    switch (data[0]) {
                        case 1:
                            if (currentMediaInfo.name != null && !currentMediaInfo.name.equals(info)) {
                                currentMediaInfo.reset();
                            }
                            currentMediaInfo.name = info;
                            break;
                        case 2:
                            currentMediaInfo.artist = info;
                            break;
                        case 3:
                            currentMediaInfo.album = info;
                            break;
                        case 4:
                            currentMediaInfo.folderName = info;
                            break;
                    }
                    return true;
                }
            }
            return false;
        }

        public static String autoGetString(byte[] data) throws UnsupportedEncodingException {
            byte[] checkStringBytes = new byte[(data.length - 1) * 20];
            byte[] stringBytes = new byte[data.length - 1];
            for (int i = 0; i < 20; i++) {
                System.arraycopy(data, 1, checkStringBytes, (data.length - 1) * i, stringBytes.length);
            }
            System.arraycopy(data, 1, stringBytes, 0, stringBytes.length);
            CharsetDetector charsetDetector = new CharsetDetector();
            charsetDetector.setText(checkStringBytes);
            CharsetMatch charsetMatch = charsetDetector.detect();
            String charSet = charsetMatch.getName();
            UniversalDetector detector = new UniversalDetector(null);
            detector.handleData(checkStringBytes, 0, checkStringBytes.length);
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();
            if (encoding == null || !encoding.contains("GB")) {
                if (!charSet.contains("windows") && !charSet.equals("UTF-16LE") && charsetMatch.getConfidence() >= 10) {
                    if (charsetMatch.getName().equals("Big5") && charsetMatch.getConfidence() >= 10) {
                        String checkString = new String(stringBytes, charSet);
                        String uniString = new String(stringBytes, "Unicode");
                        if (uniString.length() < checkString.length()) {
                            charSet = "Unicode";
                        }
                    }
                    return new String(stringBytes, charSet);
                }
                charSet = "Unicode";
                return new String(stringBytes, charSet);
            }
            return new String(stringBytes, encoding);
        }
    }

    public static class EqData {
        public int BAL;
        public int BAS;
        public int FAD;
        public int MID;
        public int TRE;
        public String changeVol;
        public int times;
        public int volume;

        public void parseFromEqEvent(byte[] data, MediaPlayStatus currentPlayStatus, MediaData currentMediaStatus) {
            this.times++;
            switch (data[0]) {
                case 1:
                    this.BAS = data[1] - 16;
                    this.changeVol = "BAS";
                    break;
                case 2:
                    this.MID = data[1] - 16;
                    this.changeVol = "MID";
                    break;
                case 3:
                    this.TRE = data[1] - 16;
                    this.changeVol = "TRE";
                    break;
                case 4:
                    this.FAD = data[1] - 16;
                    this.changeVol = "FAD";
                    break;
                case 5:
                    this.BAL = data[1] - 16;
                    this.changeVol = "BAL";
                    break;
                case 6:
                    currentPlayStatus.times++;
                    currentMediaStatus.mode.ASL = data[1] == 1;
                    currentPlayStatus.ALS = data[1] == 1;
                    this.changeVol = "ASL " + (data[1] == 1 ? "ON" : "OFF");
                    break;
            }
            this.volume = data[1] - 16;
        }
    }

    public static class MediaStringInfo {
        public String album;
        public String artist;
        public String folderName;
        public int min;
        public String name;
        public int sec;
        public int times;
    }

    public static class MediaPlayStatus {
        public static final int TYPE_AM = 1;
        public static final int TYPE_AUX = 20;
        public static final int TYPE_BT_MUSIC = 21;
        public static final int TYPE_DISC = 16;
        public static final int TYPE_FM = 0;
        public static final int TYPE_MP3 = 18;
        public static final int TYPE_USB = 17;
        public boolean ALS;
        public boolean RAND;
        public boolean RPT;
        public boolean SCAN;
        public boolean ST;
        public String status;
        public int times;
        public int type;

        public void parseStatusEvent(byte[] data, MediaData currentMedia) {
            currentMedia.times++;
            this.times++;
            currentMedia.type = data[0];
            String status = "";
            switch (data[1]) {
                case 0:
                    status = "PLAY";
                    break;
                case 1:
                    status = "PAUSE";
                    break;
                case 6:
                    if (currentMedia.type != 16) {
                        if (currentMedia.type == 17) {
                            status = "READING FILE";
                            break;
                        }
                    } else {
                        status = "READING DISC";
                        break;
                    }
                    break;
                case 7:
                    status = "ERROR";
                    break;
                case 8:
                    status = "NO MUSIC";
                    break;
                case 14:
                    status = "AUDIO OFF";
                    break;
                case 15:
                    status = "UNKNOWN";
                    break;
            }
            this.status = status;
            this.RPT = (data[2] & 16) > 1;
            this.ST = (data[2] & 8) > 1;
            this.SCAN = (data[2] & 2) > 1;
            this.RAND = (data[2] & 1) >= 1;
        }
    }

    public static class DiscStatus {
        public boolean[] discInsert;
        public int range;
        public String status;
        public int times;

        public void parseFromStatusEvent(byte[] data) {
            this.times++;
            String pStatus = "";
            switch (data[0]) {
                case 1:
                    pStatus = "LOAD";
                    break;
                case 2:
                    pStatus = "EJECT";
                    break;
                case 3:
                    pStatus = "DISC IN";
                    break;
                case 4:
                    pStatus = "DISC FULL";
                    break;
                case 5:
                    pStatus = "WAIT";
                    break;
            }
            this.discInsert = new boolean[6];
            for (int i = 0; i < this.discInsert.length; i++) {
                this.discInsert[i] = ((data[1] & 255) & (1 << i)) >= 1;
            }
            this.status = pStatus;
            this.range = data[2];
        }
    }

    public static class CarBluetoothStatus {
        public int batteryStatus;
        public int callSignal;
        public boolean isCalling;
        public int min;
        public String name;
        public boolean playingMusic;
        public int sec;
        public String settingsInfo;
        public int times;

        public void parseFromMediaEvent(byte[] data) {
            this.times++;
            this.min = data[5];
            this.sec = data[6];
        }

        public void parseFromStatusEvent(byte[] data, MediaData currentMedia) {
            currentMedia.times++;
            this.times++;
            this.isCalling = (data[0] & 128) > 1;
            this.callSignal = (data[0] & -32) >> 5;
            this.playingMusic = !((data[0] & 8) <= 1);
            this.batteryStatus = data[0] & 7;
        }

        public void parseFromInfoEvent(byte[] data, MediaData currentMedia) {
            currentMedia.times++;
            byte[] btStringBytes = new byte[data.length - 1];
            System.arraycopy(data, 1, btStringBytes, 0, btStringBytes.length);
            String btInfo = new String(btStringBytes, StandardCharsets.US_ASCII);
            if (data[0] == 1) {
                this.name = btInfo;
            } else if (data[0] == 4) {
                this.settingsInfo = btInfo;
            }
        }
    }
}
