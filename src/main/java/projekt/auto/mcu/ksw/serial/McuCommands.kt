package projekt.auto.mcu.ksw.serial

import projekt.auto.mcu.ksw.serial.enums.*

/**
 * @author Snaggly
 * Listed are all the current known commands and functions to control the KSW MCU.
 * Most of them were found by VincentZ4 as seen here:
 * https://f30.bimmerpost.com/forums/showpost.php?p=26332187&postcount=3436
 *
 * Those that were not taken from the above linked Doc have been marked.
 *
 * This is still incomplete! Certain commands are yet to be reverse engineered or trace backed.
 * Many commands listed here are also yet to be researched.
 */

interface McuCommands {
    val command: Int
    val data: ByteArray
    val update: Boolean

    /**
     * @author Snaggly
     * Sets the volume of one source.
     * @param mode Available values: 0x1 - ARM, 0x2 - OEM
     * @param stream Available values: 0x1 - MediaVolume, 0x2 - PhoneVolume, 0x3 - NaviVolume
     * @param volume Enter a volume from 0 to 40 (0x0 - 0x28)
     * @param isSynced Available value: 0x0 - Is not in sycn!, 0x1 - Is in sync. Probably keep the inSync on!
     */
    class SetVolume(private val mode: Byte,
                    private val stream: Byte,
                    private val volume: Byte,
                    private val isSynced: Byte): McuCommands {
        override val command: Int
            get() = 0x62
        override val data: ByteArray
            get() = byteArrayOf(0x0, mode, stream, volume, isSynced)
        override val update: Boolean
            get() = false
    }

    /**
     * Sets the status of one source.
     * Requires more info!
     * @param volumeType Allowed values for Volume Type: 0x1, 0x2, 0x3, 0x21
     * @param volume Volume can go from 0 to 40 (0x0-0x28)
     */
    @Deprecated("Newer Firmwares use SetVolume instead")
    class SetSoundStatus(private val muteMode: MUTE_MODE,
                         private val deviceMode: SOUND_SRC_TYPE,
                         private val volumeType: Byte,
                         private val volume: Byte): McuCommands {
        override val command: Int
            get() = 0x62
        override val data: ByteArray
            get() = byteArrayOf(muteMode.muteVal, deviceMode.typeValue, volumeType, volume)
        override val update: Boolean
            get() = false
    }

    /**
     * Saves the statue of the built in Bluetooth device.
     * Requires more info!
     */
    class SetBluetoothState(private val bluetoothState: BLUETOOTH_STATE): McuCommands{
        override val command: Int
            get() = 0x63
        override val data: ByteArray
            get() = byteArrayOf(0x0, bluetoothState.value)
        override val update: Boolean
            get() = false
    }

    /**
     * Saves the statue of the built in Bluetooth device. Requires more info!
     * @param realYear The current year eg. 2020. Allowed values: 2000-2255
     * @param month Allowed values: 1-12
     * @param day Allowed values: 1-31
     * @param hour Allowed values: 0-23
     * @param minute Allowed values: 0-60
     * @param second Allowed values: 0-60
     */
    class SetTime(private val realYear: Int,
                  private val month: Byte,
                  private val day: Byte,
                  private val hour: Byte,
                  private val minute: Byte,
                  private val second: Byte,
                  private val timeType: TIME_TYPE): McuCommands{
        override val command: Int
            get() = 0x64
        override val data: ByteArray
            get() = byteArrayOf((realYear-2000).toByte(), month, day, hour, minute, second, timeType.value)
        override val update: Boolean
            get() = false
    }

    /**
     * Saves the statue of the built in Bluetooth device. Requires more info!
     * @param brightness 0-100
     * @param saturation 0-100
     * @param contrast 0-100
     * @param chroma 0-100
     * @param paramode 0-6
     */
    class SetBrightness(private val brightness: Byte,
                  private val saturation: Byte,
                  private val contrast: Byte,
                  private val chroma: Byte,
                  private val paramode: Byte): McuCommands{
        override val command: Int
            get() = 0x65
        override val data: ByteArray
            get() = byteArrayOf(brightness, saturation, contrast, chroma, paramode)
        override val update: Boolean
            get() = false
    }

    /**
     * Switches to the desired sound/video stream.
     * For example while in ARM Setting Music Source to DVR opens the connected DVR video feed.
     */
    class SetMusicSource(private val musicSource: SOUND_SRC_TYPE): McuCommands{
        override val command: Int
            get() = 0x67
        override val data: ByteArray
            get() = byteArrayOf(musicSource.typeValue)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     * @param returnMode: 0x0 or 0x4
     */
    class SetMusicSourceWithReturnMode(private val returnMode: Byte, private val musicSource: SOUND_SRC_TYPE): McuCommands{
        override val command: Int
            get() = 0x68
        override val data: ByteArray
            get() = byteArrayOf(returnMode, musicSource.typeValue)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     * @param clickStatus: 0x0 or 0x1
     */
    class ClickOnScreen(private val deviceMode: SOUND_SRC_TYPE,
                        private val xCoordinate : Int,
                        private val yCoordinate : Int,
                        private val clickStatus: Byte): McuCommands{
        override val command: Int
            get() = 0x6B
        override val data: ByteArray
            get() = byteArrayOf(deviceMode.typeValue,
                    (xCoordinate / 0xFF).toByte(),
                    (xCoordinate % 0xFF).toByte(),
                    (yCoordinate / 0xFF).toByte(),
                    (yCoordinate % 0xFF).toByte(),
                    clickStatus)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     * @param brightness: Allowed values: 0-100
     */
    class SetBrightnessLevel(private val brightness: Byte): McuCommands{
        override val command: Int
            get() = 0x6C
        override val data: ByteArray
            get() = byteArrayOf(brightness)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info! Probably has to do with when MCU posts new messages
     */
    class SetMessageBeatTimer(private val mode: MESSAGE_BEAT_MODE): McuCommands{
        override val command: Int
            get() = 0x6F
        override val data: ByteArray
            get() = byteArrayOf(mode.value)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     * @param type probably 0-7 with 0 being turned off.
     */
    class SetAHDCameraType(private val type: Byte): McuCommands{
        override val command: Int
            get() = 0x70
        override val data: ByteArray
            get() = byteArrayOf(20, type)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     */
    class SetAUXPosition(private val position: Byte): McuCommands{
        override val command: Int
            get() = 0x70
        override val data: ByteArray
            get() = byteArrayOf(23, position)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     */
    class SwitchToDVDView(turnOn: Boolean): McuCommands{
        private var value: Byte = 0
        init {
            if(turnOn)
                value=0x1
        }
        override val command: Int
            get() = 0x71
        override val data: ByteArray
            get() = byteArrayOf(value)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     * @param low 0-23
     * @param mid 0-23
     * @param high 0-23
     * @param mode Equalizer Mode 0-4
     */
    class SetEqualizer(private val low: Byte,
                       private val mid: Byte,
                       private val high: Byte,
                       private val mode: Byte): McuCommands{
        override val command: Int
            get() = 0x73
        override val data: ByteArray
            get() = byteArrayOf(low, mid, high, mode)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     * @param state 5, 6, 8, 9
     */
    class SetCmdWheelState(private val state: Byte): McuCommands{
        override val command: Int
            get() = 0x74
        override val data: ByteArray
            get() = byteArrayOf(state)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     */
    class SetFMRadio(turnOn: Boolean, private val frequency: Int): McuCommands{
        private var value: Byte = 0x2
        init {
            if(turnOn)
                value=0x1
        }

        override val command: Int
            get() = 0x75
        override val data: ByteArray
            get() = byteArrayOf(value,
                    (frequency.shl(8).and(0xFF)).toByte(),
                    (frequency.and(0xFF).toByte()))
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     */
    class SetBMTVal(private val interfaceStatus: INTERFACES,
                    backCarState: Boolean,
                    backlight_isOff: Boolean): McuCommands{
        var backcarstate_val: Byte = 0x0
        var backlight_val: Byte = 0x00
        init {
            if(backCarState)
                backcarstate_val=0x1
            if (backlight_isOff)
                backlight_val=0x1
        }

        override val command: Int
            get() = 0x76
        override val data: ByteArray
            get() = byteArrayOf(interfaceStatus.value, backcarstate_val, backlight_val)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info! And proper research!
     */
    class SetDisplayType(private val ambientLight_R: Byte,
                         private val ambientLight_G: Byte,
                         private val ambientLight_B: Byte,
                         private val cur_select_position: Byte): McuCommands{
        override val command: Int
            get() = 0x77
        override val data: ByteArray
            get() = byteArrayOf(ambientLight_R, ambientLight_G, ambientLight_B, cur_select_position)
        override val update: Boolean
            get() = false
    }

    /**
     * Specific to XinCheng client.
     * Needs more info!
     * @param mode 0-0x1c
     */
    class SetVoiceControl(private val mode: Byte, turnOn: Boolean): McuCommands{
        private var value : Byte = 0x0
        init {
            if (turnOn)
                value=0x01
        }
        override val command: Int
            get() = 0x77
        override val data: ByteArray
            get() = byteArrayOf(mode, value)
        override val update: Boolean
            get() = false
    }

    /**
     * Needs more info!
     * @param instrumentBacklightValue 0-0x64, Default: 0x12
     */
    class SetVolumeInBackCar(cockboardAscendingStatus: Boolean,
                             airmaticStatus: Boolean,
                             auxiliaryRadarStatus: Boolean,
                             private val instrumentBacklightValue: Byte): McuCommands{
        private var cockboardAscendingStatus_value : Byte = 0x0
        private var airmaticStatus_value : Byte = 0x0
        private var auxiliaryRadarStatus_value : Byte = 0x0
        init {
            if (cockboardAscendingStatus)
                cockboardAscendingStatus_value=0x01
            if (airmaticStatus)
                airmaticStatus_value=0x01
            if (auxiliaryRadarStatus)
                auxiliaryRadarStatus_value=0x01
        }
        override val command: Int
            get() = 0x77
        override val data: ByteArray
            get() = byteArrayOf(cockboardAscendingStatus_value, airmaticStatus_value, auxiliaryRadarStatus_value, instrumentBacklightValue)
        override val update: Boolean
            get() = false
    }

    companion object {
        val Increase_AirCond_Temperature: McuCommands = object: McuCommands {
            override val command: Int
                get() = 0x60
            override val data: ByteArray
                get() = byteArrayOf(0x1)
            override val update: Boolean
                get() = false
        }

        val Decrease_AirCond_Temperature: McuCommands = object: McuCommands {
            override val command: Int
                get() = 0x61
            override val data: ByteArray
                get() = byteArrayOf(0x1, 0x81.toByte())
            override val update: Boolean
                get() = false
        }

        /**
         * Note: Is not available anymore!
         */
        val Activate_Front_Camera: McuCommands = object: McuCommands {
            override val command: Int
                get() = 0x66
            override val data: ByteArray
                get() = byteArrayOf(0x1, 0x1)
            override val update: Boolean
                get() = false
        }

        /**
         * Note: Is not available anymore!
         */
        val Close_Front_Camera: McuCommands = object: McuCommands {
            override val command: Int
                get() = 0x66
            override val data: ByteArray
                get() = byteArrayOf(0x0, 0x1)
            override val update: Boolean
                get() = false
        }


        @Deprecated("Please use the class SetMusicSource instead!")
        val SET_TO_MUSIC_SOURCE: McuCommands = object : McuCommands {
            override val command: Int
                get() = 103
            override val data: ByteArray
                get() = byteArrayOf(1)
            override val update: Boolean
                get() = false
        }

        @Deprecated("Please use the class SetMusicSource instead!")
        val SET_TO_ATSL_AIRCONSOLE: McuCommands = object : McuCommands {
            override val command: Int
                get() = 103
            override val data: ByteArray
                get() = byteArrayOf(13)
            override val update: Boolean
                get() = false
        }

        val HU_Powered_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(16, 1)
            override val update: Boolean
                get() = false
        }

        val Arm_in_upgrade_mode: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(16, 3)
            override val update: Boolean
                get() = false
        }

        val SWITCH_TO_OEM: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(18, 2)
            override val update: Boolean
                get() = false
        }

        val SWITCH_TO_ANDROID: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(18, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Stops the navigation callouts to center speaker
         * Needs more info!
         */
        val Stop_Voice: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(19, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Starts the navigation callouts to center speaker
         * Needs more info!
         */
        val Start_Voice: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(19, 1)
            override val update: Boolean
                get() = false
        }

        val PlayState_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(20, 0)
            override val update: Boolean
                get() = false
        }

        val PlayState_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x69
            override val data: ByteArray
                get() = byteArrayOf(20, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_Backcar_Mirror_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(1, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_Backcar_Mirror_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(1, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_Language_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(8, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_Language_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(8, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_SD_Host_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(9, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_SD_Host_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(9, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_CAMERA_SELECTION_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(11, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_CAMERA_SELECTION_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(11, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_DVD_SELECTION_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(12, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_DVD_SELECTION_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(12, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_BLACKSCREEN_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(13, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_BLACKSCREEN_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(13, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_Video_driving_ban_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(14, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_Video_driving_ban_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(14, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_Record_BT_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(18, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_Record_BT_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(18, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * If the car has an OEM screen. If not a clock shows up when switching to OEM Radio.
         * Needs more info!
         */
        val SYS_Original_Car_Video_Display_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(19, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * If the car has an OEM screen. If not a clock shows up when switching to OEM Radio.
         * Needs more info!
         */
        val SYS_Original_Car_Video_Display_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(19, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_FrontCamera_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(20, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val SYS_FrontCamera_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(20, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_ReversingTrack_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(22, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_ReversingTrack_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(22, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_Radar_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(23, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=On
         * Needs more info!
         */
        val SYS_Radar_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x6A
            override val data: ByteArray
                get() = byteArrayOf(23, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_Car_Type_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(1, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_Car_Type_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(1, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_Amplifier_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(2, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_Amplifier_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(2, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_AUX_Switching_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(3, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_AUX_Switching_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(3, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_DVR_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(4, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=Off
         * Needs more info!
         */
        val Record_DVR_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(4, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_BT_Type_Key_0: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(5, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_BT_Type_Key_1: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(5, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_BT_Type_Key_2: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(5, 2)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_BT_Type_Key_3: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(5, 3)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_CCC_iDrive_Type_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(6, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_CCC_iDrive_Type_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(6, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_KSW_Agreement_Select_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(7, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_KSW_Agreement_Select_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(7, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Reverseing_Mute_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(8, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Reverseing_Mute_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(8, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Handset_Automatic_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(9, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Handset_Automatic_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(9, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * On for chinese countries
         * Needs more info!
         */
        val Set_IDEOGRAM_Country_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(10, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * On for chinese countries
         * Needs more info!
         */
        val Set_IDEOGRAM_Country_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(10, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Collect_CAN_Data_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(15, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Collect_CAN_Data_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(15, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_AUX_Activation_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(16, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_AUX_Activation_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(16, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Voice_Key_Function_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(17, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Voice_Key_Function_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(17, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_360_Camera_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(18, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_360_Camera_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(18, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Backlight_Control_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(19, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Backlight_Control_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(19, 1)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Map_Key_Function_Off: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(21, 0)
            override val update: Boolean
                get() = false
        }

        /**
         * Default=0
         * Needs more info!
         */
        val Set_Map_Key_Function_On: McuCommands = object : McuCommands {
            override val command: Int
                get() = 0x70
            override val data: ByteArray
                get() = byteArrayOf(22, 1)
            override val update: Boolean
                get() = false
        }
    }
}