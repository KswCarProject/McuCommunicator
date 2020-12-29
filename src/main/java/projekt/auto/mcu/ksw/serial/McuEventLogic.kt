package projekt.auto.mcu.ksw.serial

interface McuEventLogic {
    fun getMcuEvent(cmdType: Int, data: ByteArray)

    companion object {
        /**
         * @author Snaggly
         * Returns which McuEvent has been raised. The point is to have a way to quickly determine the event type.
         * This is a sample. Remove any unneeded events for efficiency!
         */
        fun getMcuEvent(cmdType: Int, data: ByteArray): McuEvent? {
            if (cmdType == 0x1C) {
                if (data[0] == 0x1.toByte()) return McuEvent.Idle
            }
            else if (cmdType == 0xA1) {
                if (data[0] == 0x17.toByte()) {
                    if (data[1] == 0x6.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveKnobTurnClockwise
                    if (data[1] == 0x7.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveKnobTurnCounterClockwise
                    if (data[1] == 0x2.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveKnobTiltDown
                    if (data[1] == 0x2.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveKnobTiltDownRelease
                    if (data[1] == 0x3.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveKnobTiltLeft
                    if (data[1] == 0x3.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveKnobTiltLeftRelease
                    if (data[1] == 0x4.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveKnobTiltRight
                    if (data[1] == 0x4.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveKnobTiltRightRelease
                    if (data[1] == 0x1.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveKnobTiltUp
                    if (data[1] == 0x1.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveKnobTiltUpRelease
                    if (data[1] == 0x5.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveKnobPressed
                    if (data[1] == 0x5.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveKnobReleased
                    if (data[1] == 0x19.toByte() && data[2] == 0x0.toByte()) return McuEvent.MediaPlayPauseButtonPressed
                    if (data[1] == 0x19.toByte() && data[2] == 0x1.toByte()) return McuEvent.MediaPlayPauseButtonReleased
                    if (data[1] == 0x18.toByte() && data[2] == 0x0.toByte()) return McuEvent.MediaNextButtonPressed
                    if (data[1] == 0x18.toByte() && data[2] == 0x1.toByte()) return McuEvent.MediaNextButtonReleased
                    if (data[1] == 0x17.toByte() && data[2] == 0x0.toByte()) return McuEvent.MediaPreviousButtonPressed
                    if (data[1] == 0x17.toByte() && data[2] == 0x1.toByte()) return McuEvent.MediaPreviousButtonReleased
                    if (data[1] == 0x14.toByte() && data[2] == 0x1.toByte()) return McuEvent.VoiceCommandPress
                    if (data[1] == 0x14.toByte() && data[2] == 0x0.toByte()) return McuEvent.VoiceCommandRelease
                    if (data[1] == 0x8.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveMenuButtonPressed
                    if (data[1] == 0x8.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveMenuButtonReleased
                    if (data[1] == 0xc.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveBackButtonPress
                    if (data[1] == 0xc.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveBackButtonRelease
                    if (data[1] == 0xe.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveNavigationButtonPressed
                    if (data[1] == 0xe.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveNavigationButtonReleased
                    if (data[1] == 0xd.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveOptionsButtonPress
                    if (data[1] == 0xd.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveOptionsButtonRelease
                    if (data[1] == 0x11.toByte() && data[2] == 0x1.toByte()) return McuEvent.SteeringWheelTelButtonPressed
                    if (data[1] == 0x11.toByte() && data[2] == 0x0.toByte()) return McuEvent.SteeringWheelTelButtonReleased
                    if (data[1] == 0xb.toByte() && data[2] == 0x1.toByte()) return McuEvent.iDriveTelephoneButtonLongPress
                    if (data[1] == 0xb.toByte() && data[2] == 0x0.toByte()) return McuEvent.iDriveTelephoneButtonLongRelease
                    if (data[1] == 0x10.toByte() && data[2] == 0x1.toByte()) return McuEvent.MenuButtonPress
                    if (data[1] == 0x10.toByte() && data[2] == 0x0.toByte()) return McuEvent.MenuButtonRelease
                } else if (data[0] == 0x1A.toByte()) {
                    if (data[1] == 0x1.toByte()) return McuEvent.SWITCHED_TO_ARM
                    if (data[1] == 0x2.toByte()) return McuEvent.SWITCHED_TO_OEM
                } else if (data[0] == 0x19.toByte()) return McuEvent.CarDataReceived
                else if (data[0] == 0x10.toByte()) {
                    if (data[1] == 0x0.toByte() && data[2] == 0x0.toByte()) return McuEvent.ParkingBreakReleased
                    if (data[1] == 0x8.toByte() && data[2] == 0x0.toByte()) return McuEvent.ParkingBreakOnAndBeltOff
                    if (data[1] == 0x8.toByte() && data[2] == 0x1.toByte()) return McuEvent.BeltOn
                    if (data[1] == 0x0.toByte() && data[2] == 0x1.toByte()) return McuEvent.ParkingBreakReleasedAndBeltOn
                } else if (data[0] == 0x12.toByte()) {
                    if (data[1] == 0x8.toByte() && data[2] == 0x0.toByte()) return McuEvent.AllDoorsClosed
                    if (data[1] == 0x18.toByte() && data[2] == 0x0.toByte()) return McuEvent.FrontLeftDoorOpened
                    if (data[1] == 0x28.toByte() && data[2] == 0x0.toByte()) return McuEvent.FrontRightDoorOpened
                    if (data[1] == 0x38.toByte() && data[2] == 0x0.toByte()) return McuEvent.FrontDoorsOpened
                    if (data[1] == 0xc.toByte() && data[2] == 0x0.toByte()) return McuEvent.TrunkOpened
                    if (data[1] == 0x1c.toByte() && data[2] == 0x0.toByte()) return McuEvent.TrunkAndLeftDoorOpened
                    if (data[1] == 0x2c.toByte() && data[2] == 0x0.toByte()) return McuEvent.TrunkAndRightDoorOpened
                    if (data[1] == 0x3c.toByte() && data[2] == 0x0.toByte()) return McuEvent.TrunkAndDoorsOpened
                }
            } else {
                if (cmdType == 0x11) {
                    if (data[0] == 0x3.toByte() && data[1] == 0x1.toByte()) return McuEvent.ParkingRadarViewOn
                    if (data[0] == 0x3.toByte() && data[1] == 0x0.toByte()) return McuEvent.ParkingRadarViewOff
                }
            }

            return null
        }
    }
}
