package projekt.auto.mcu.ksw.model;

import android.bluetooth.BluetoothHidDevice;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * @author Snaggly
 * This Class is an altered version taken fromm the Ksw Sources
 * The changes include mainly the removal of functional methods
 * and addition of parsing logic.
 *
 * Use it to parse CarData and have an object to broadcast to clients!
 * Predertime the Event type with McuEventLogic before parsing.
 */
public class McuStatus {
    public ACData acData = new ACData();
    public BenzData benzData = new BenzData();
    public CarData carData = new CarData();
    public String mcuVerison;
    public int systemMode = 1;

    public McuStatus() {
    }

    public McuStatus(int systemMode2, String mcuVerison2) {
        this.systemMode = systemMode2;
        this.mcuVerison = mcuVerison2;
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
            this.AC_Switch = (data[1] & BluetoothHidDevice.SUBCLASS1_KEYBOARD) != 0;
            this.loop = (data[1] & 32) != 0 ? 1 : 0;
            this.frontMistSwitch = (data[1] & 8) != 0;
            this.backMistSwitch = (data[1] & 2) != 0;
            this.sync = (data[1] & 1) != 0;
            this.mode = data[2];
            this.setLeftTmp(data[3]);
            this.setRightTmp(data[4]);
            //this.autoSwitch = (data[5] & WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK) != 0;
            //this.speed = ((float) (data[5] & MidiConstants.STATUS_CHANNEL_MASK)) * 0.5f;
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
        public static final int LEFT_AHEAD = 16;
        public static final int LEFT_BACK = 64;
        public static final int RIGHT_AHEAD = 32;
        public static final int RIGHT_BACK = 128;
        public float airTemperature;
        public float averSpeed;
        public int carDoor;
        public int distanceUnitType;
        public int engineTurnS;
        public boolean handbrake;
        public int mileage;
        public int oilSum;
        public int oilUnitType;
        public float oilWear;
        public boolean safetyBelt;
        public int speed;
        public int temperatureUnitType;

        public boolean isOpen(int type) {
            return (this.carDoor & type) != 0;
        }

        public int getCarDoor() {
            return this.carDoor;
        }

        public void setCarDoor(int carDoor2) {
            this.carDoor = carDoor2;
        }

        public boolean isHandbrake() {
            return this.handbrake;
        }

        public void setHandbrake(boolean handbrake2) {
            this.handbrake = handbrake2;
        }

        public boolean isSafetyBelt() {
            return this.safetyBelt;
        }

        public void setSafetyBelt(boolean safetyBelt2) {
            this.safetyBelt = safetyBelt2;
        }

        public int getMileage() {
            return this.mileage;
        }

        public void setMileage(int mileage2) {
            this.mileage = mileage2;
        }

        public float getOilWear() {
            return this.oilWear;
        }

        public void setOilWear(float oilWear2) {
            this.oilWear = oilWear2;
        }

        public int getOilSum() {
            return this.oilSum;
        }

        public void setOilSum(int oilSum2) {
            this.oilSum = oilSum2;
        }

        public float getAverSpeed() {
            return this.averSpeed;
        }

        public void setAverSpeed(float averSpeed2) {
            this.averSpeed = averSpeed2;
        }

        public int getSpeed() {
            return this.speed;
        }

        public void setSpeed(int speed2) {
            this.speed = speed2;
        }

        public int getEngineTurnS() {
            return this.engineTurnS;
        }

        public void setEngineTurnS(int engineTurnS2) {
            this.engineTurnS = engineTurnS2;
        }

        public float getAirTemperature() {
            return this.airTemperature;
        }

        public void setAirTemperature(float airTemperature2) {
            this.airTemperature = airTemperature2;
        }

        public int getDistanceUnitType() {
            return this.distanceUnitType;
        }

        public void setDistanceUnitType(int distanceUnitType2) {
            this.distanceUnitType = distanceUnitType2;
        }

        public int getTemperatureUnitType() {
            return this.temperatureUnitType;
        }

        public void setTemperatureUnitType(int temperatureUnitType2) {
            this.temperatureUnitType = temperatureUnitType2;
        }

        public int getOilUnitType() {
            return this.oilUnitType;
        }

        public void setOilUnitType(int oilUnitType2) {
            this.oilUnitType = oilUnitType2;
        }

        public void parseFromBrakeBeltEvent(byte[] data) {
            this.handbrake = ((data[1] & 255) & 8) != 0;
            this.safetyBelt = ((data[2] & 255) & 1) != 0;
        }

        public void parseFromDoorEvent(byte[] data) {
            this.carDoor = data[1] & 255;
        }

        public void parseFromCarDataEvent(byte[] data) {
            this.mileage = ((data[1] & 255) << 8) + (data[2] & 255);
            this.oilWear = ((float) (((data[3] & 255) << 8) + (data[4] & 255))) / 10.0f;
            this.averSpeed = ((float) (((data[5] & 255) << 8) + (data[6] & 255))) / 10.0f;
            this.speed = ((data[7] & 255) << 8) + (data[8] & 255);
            this.engineTurnS = ((data[9] & 255) << 8) + (data[10] & 255);
            this.oilSum = ((data[11] & 255) << 8) + (data[12] & 255);
            if ((data[13] & 128) > 0) {
                this.airTemperature = (float) (((double) (65535 & ((~(((data[13] & 255) * 256) + (data[14] & 255))) + 1))) * -0.1d);
            } else {
                this.airTemperature = (float) (((double) (65535 & (((data[13] & 255) * 256) + (data[14] & 255)))) * 0.1d);
            }
            this.distanceUnitType = data[15] & 8;
            this.temperatureUnitType = data[15] & 4;
            this.oilUnitType = data[15] & 2;
            this.oilUnitType += data[15] & 255 & 1;
        }
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
}
