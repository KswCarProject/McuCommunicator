package projekt.auto.mcu.ksw.serial;

/**
 * @author Snaggly
 * Listed are all the current known events to that the KSW MCU raises.
 * Use this Interface to check which Event was raised by MCU on update.
 * Most of them were found by VincentZ4 as seen here:
 * https://f30.bimmerpost.com/forums/showpost.php?p=26332187&postcount=3436
 *
 * Those that were not taken from the above linked Doc have been marked.
 *
 * This might be still incomplete!
 * Certain events are yet to be reverse engineered or don't work anymore!
 */
interface McuEvent {

    fun equals(cmdType: Int, data: ByteArray): Boolean

    companion object{
        /**
         * @author Snaggly
         * Before checking any events prior, make sure to see if it's an CanEvent!
         */
        val IsCanEventType: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return cmdType == 0xA1
                }
            }

        /**
         * @author VincentZ4
         * This event is raised when MCU received CarData.
         * Use CarDataModel to decode the message!
         */
        val CarDataReceived: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[0] == 0x19.toByte()
                }
            }

        /**
         * @author Snaggly
         * Use this before checking any ScreenSwitch events.
         */
        val IsScreenSwitchEvent: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[0] == 0x1A.toByte()
                }
            }

        /**
         * @author Snaggly
         * This event is raised when end user switches to OEM Radio be it by hardware button
         * or programmatically via an McuCommand.
         */
        val SWITCHED_TO_OEM: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x2.toByte()
                }
            }

        /**
         * @author Snaggly
         * This event is raised when end user taps or holds the Menu button
         * to switch back to ARM.
         */
        val SWITCHED_TO_ARM: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Returns true if any car button has been pressed!
         * Could be useful to speed up switch-cases?
         */
        val AnyCarButtonPressed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[0] == 0x17.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Returns true if Tel has been pressed down.
         * Make sure to have checked first if it's a Button-Press-Type with AnyCarButtonPressed!
         */
        val SteeringWheelTelButtonPressed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x11.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Returns true if Tel has been released.
         * Make sure to have checked first if it's a Button-Press-Type with AnyCarButtonPressed!
         */
        val SteeringWheelTelButtonReleased: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x11.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Returns true if Previous media button has been pressed down.
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val MediaPreviousButtonPressed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x17.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Returns true if Previous media button has been released.
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val MediaPreviousButtonReleased: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x17.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Returns true if Next media button has been pressed down.
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val MediaNextButtonPressed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x18.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Returns true if Next media button has been released.
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val MediaNextButtonReleased: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x18.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltUp: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x1.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltUpRelease: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x1.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltDown: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x2.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltDownRelease: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x2.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltLeft: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x3.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltLeftRelease: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x3.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltRight: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x4.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTiltRightRelease: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x4.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobPressed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x5.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobReleased: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x5.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTurnClockwise: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x6.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveKnobTurnCounterClockwise: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x7.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveMenuButtonPressed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x8.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveMenuButtonReleased: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0x8.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveTelephoneButtonLongPress: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xb.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveTelephoneButtonLongRelease: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xb.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveBackButtonPress: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xc.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveBackButtonRelease: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xc.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveOptionsButtonPress: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xd.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveOptionsButtonRelease: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xd.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveNavigationButtonPressed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xe.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         * Make sure to have checked first if it's a Button-Press-Type (data[0]=0x17) with AnyCarButtonPressed!
         */
        val iDriveNavigationButtonReleased: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1] == 0xe.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author Snaggle
         */
        val IsParkingBeltEvent: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[0] == 0x10.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val ParkingBreakReleased: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x0.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val ParkingBreakOnAndBeltOff: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x8.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val BeltOn: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x8.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author Snaggly
         * This has not been tested!
         */
        val ParkingBreakReleasedAndBeltOn: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x0.toByte() && data[2] == 0x1.toByte()
                }
            }

        /**
         * @author Snaggle
         */
        val IsDoorEvent: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[0] == 0x12.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val AllDoorsClosed: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x8.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val FrontLeftDoorOpened: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x18.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val FrontRightDoorOpened: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x28.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val FrontDoorsOpened: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x38.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val TrunkOpened: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0xc.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val TrunkAndLeftDoorOpened: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x1c.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val TrunkAndRightDoorOpened: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x2c.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val TrunkAndDoorsOpened: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return data[1]==0x3c.toByte() && data[2] == 0x0.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val ParkingRadarViewOn: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return cmdType==0x11 && data[0] == 0x3.toByte() && data[1]==0x1.toByte()
                }
            }

        /**
         * @author VincentZ4
         */
        val ParkingRadarViewOff: McuEvent
            get() = object: McuEvent {
                override fun equals(cmdType: Int, data: ByteArray) : Boolean {
                    return cmdType==0x11 && data[0] == 0x3.toByte() && data[1]==0x0.toByte()
                }
            }

        /**
         * @author Snaggly
         * Returns which McuEvent has been raised. The point is to have a way to quickly determine the event type.
         * Currently it's pointless. Need to rewrite the entire structure above me with maybe ENUMs?
         */
        fun getMcuEvent(cmdType: Int, data: ByteArray): McuEvent? {
            if ( IsCanEventType.equals(cmdType, data) ) {
                if (AnyCarButtonPressed.equals(cmdType, data)) {
                    if (iDriveKnobTurnClockwise.equals(cmdType,data)) return iDriveKnobTurnClockwise
                    if (iDriveKnobTurnCounterClockwise.equals(cmdType,data)) return iDriveKnobTurnCounterClockwise
                    if (iDriveKnobTiltDown.equals(cmdType,data)) return iDriveKnobTiltDown
                    if (iDriveKnobTiltDownRelease.equals(cmdType,data)) return iDriveKnobTiltDownRelease
                    if (iDriveKnobTiltLeft.equals(cmdType,data)) return iDriveKnobTiltLeft
                    if (iDriveKnobTiltLeftRelease.equals(cmdType,data)) return iDriveKnobTiltLeftRelease
                    if (iDriveKnobTiltRight.equals(cmdType,data)) return iDriveKnobTiltRight
                    if (iDriveKnobTiltRightRelease.equals(cmdType,data)) return iDriveKnobTiltRightRelease
                    if (iDriveKnobTiltUp.equals(cmdType,data)) return iDriveKnobTiltUp
                    if (iDriveKnobTiltUpRelease.equals(cmdType,data)) return iDriveKnobTiltUpRelease
                    if (iDriveKnobPressed.equals(cmdType,data)) return iDriveKnobPressed
                    if (iDriveKnobReleased.equals(cmdType,data)) return iDriveKnobReleased
                    if (iDriveMenuButtonPressed.equals(cmdType,data)) return iDriveMenuButtonPressed
                    if (iDriveMenuButtonReleased.equals(cmdType,data)) return iDriveMenuButtonReleased
                    if (iDriveBackButtonPress.equals(cmdType,data)) return iDriveBackButtonPress
                    if (iDriveBackButtonRelease.equals(cmdType,data)) return iDriveBackButtonRelease
                    if (iDriveNavigationButtonPressed.equals(cmdType,data)) return iDriveNavigationButtonPressed
                    if (iDriveNavigationButtonReleased.equals(cmdType,data)) return iDriveNavigationButtonReleased
                    if (iDriveOptionsButtonPress.equals(cmdType,data)) return iDriveOptionsButtonPress
                    if (iDriveOptionsButtonRelease.equals(cmdType,data)) return iDriveOptionsButtonRelease
                    if (iDriveTelephoneButtonLongPress.equals(cmdType,data)) return iDriveTelephoneButtonLongPress
                    if (iDriveTelephoneButtonLongRelease.equals(cmdType,data)) return iDriveTelephoneButtonLongRelease
                    if (SteeringWheelTelButtonPressed.equals(cmdType,data)) return SteeringWheelTelButtonPressed
                    if (SteeringWheelTelButtonReleased.equals(cmdType,data)) return SteeringWheelTelButtonReleased
                    if (MediaNextButtonPressed.equals(cmdType,data)) return MediaNextButtonPressed
                    if (MediaNextButtonReleased.equals(cmdType,data)) return MediaNextButtonReleased
                    if (MediaPreviousButtonPressed.equals(cmdType,data)) return MediaPreviousButtonPressed
                    if (MediaPreviousButtonReleased.equals(cmdType,data)) return MediaPreviousButtonReleased
                }
                else if (IsScreenSwitchEvent.equals(cmdType, data)){
                    if (SWITCHED_TO_ARM.equals(cmdType, data)) return SWITCHED_TO_ARM
                    if (SWITCHED_TO_OEM.equals(cmdType, data)) return SWITCHED_TO_OEM
                }
                else if (CarDataReceived.equals(cmdType, data)) return CarDataReceived
                else if (IsParkingBeltEvent.equals(cmdType, data)){
                    if (ParkingBreakReleased.equals(cmdType, data)) return ParkingBreakReleased
                    if (ParkingBreakOnAndBeltOff.equals(cmdType, data)) return ParkingBreakOnAndBeltOff
                    if (BeltOn.equals(cmdType, data)) return BeltOn
                    if (ParkingBreakReleasedAndBeltOn.equals(cmdType, data)) return ParkingBreakReleasedAndBeltOn
                }
                else if (IsDoorEvent.equals(cmdType, data)) {
                    if (AllDoorsClosed.equals(cmdType, data)) return AllDoorsClosed
                    if (FrontLeftDoorOpened.equals(cmdType, data)) return FrontLeftDoorOpened
                    if (FrontRightDoorOpened.equals(cmdType, data)) return FrontRightDoorOpened
                    if (FrontDoorsOpened.equals(cmdType, data)) return FrontDoorsOpened
                    if (TrunkOpened.equals(cmdType, data)) return TrunkOpened
                    if (TrunkAndLeftDoorOpened.equals(cmdType, data)) return TrunkAndLeftDoorOpened
                    if (TrunkAndRightDoorOpened.equals(cmdType, data)) return TrunkAndRightDoorOpened
                    if (TrunkAndDoorsOpened.equals(cmdType, data)) return TrunkAndDoorsOpened
                }
            } else {
                if ( ParkingRadarViewOn.equals(cmdType, data) )
                    return ParkingRadarViewOn
                if ( ParkingRadarViewOff.equals(cmdType, data) )
                    return ParkingRadarViewOff
            }

            return null
        }
    }
}
