package projekt.auto.mcu.ksw.serial

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
enum class McuEvent {
    /**
     * @author Snaggly
     * Checks if it's an CanEventType.
     * This could be triggered by anything happening from the car.
     */
    IsCanEventType,

    /**
     * @author VincentZ4
     * This event is raised when MCU received CarData.
     * Use CarDataModel to decode the message!
     */
    CarDataReceived,

    /**
     * @author Snaggly
     * Is triggered when Screen Switches modes.
     */
    IsScreenSwitchEvent,

    /**
     * @author Snaggly
     * This event is raised when end user switches to OEM Radio be it by hardware button
     * or programmatically via an McuCommand.
     */
    SWITCHED_TO_OEM,

    /**
     * @author Snaggly
     * This event is raised when end user taps or holds the Menu button
     * to switch back to ARM.
     */
    SWITCHED_TO_ARM,

    /**
     * @author VincentZ4
     * This event is raised if any car button has been pressed!
     * Could be useful to speed up switch-cases?
     */
    AnyCarButtonPressed,

    /**
     * @author VincentZ4
     * This event is raised if Tel has been pressed down.
     */
    SteeringWheelTelButtonPressed,

    /**
     * @author VincentZ4
     * This event is raised if Tel has been released.
     */
    SteeringWheelTelButtonReleased,

    /**
     * @author VincentZ4
     * This event is raised when Previous media button has been pressed down.
     */
    MediaPreviousButtonPressed,

    /**
     * @author VincentZ4
     * Returns true if Previous media button has been released.
     */
    MediaPreviousButtonReleased,

    /**
     * @author VincentZ4
     * Returns true if Next media button has been pressed down.
     */
    MediaNextButtonPressed,

    /**
     * @author VincentZ4
     * Returns true if Next media button has been released.
     */
    MediaNextButtonReleased,

    /**
     * @author Snaggle
     * Returns true if PlayPause button has been pressed down.
     */
    MediaPlayPauseButtonPressed,

    /**
     * @author Snaggle
     * Returns true if PlayPause button has been released.
     */
    MediaPlayPauseButtonReleased,

    /**
     * @author Snaggle
     * When Media button from steering wheel has been pressed down.
     */
    MenuButtonPress,

    /**
     * @author Snaggle
     * When Media button from steering wheel has been released.
     */
    MenuButtonRelease,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted Up.
     */
    iDriveKnobTiltUp,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted Up and released.
     */
    iDriveKnobTiltUpRelease,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted down.
     */
    iDriveKnobTiltDown,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted down and released.
     */
    iDriveKnobTiltDownRelease,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted left.
     */
    iDriveKnobTiltLeft,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted left and released.
     */
    iDriveKnobTiltLeftRelease,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted right.
     */
    iDriveKnobTiltRight,

    /**
     * @author VincentZ4
     * Returns true if iDrive Knob has been tilted right and released.
     */
    iDriveKnobTiltRightRelease,

    /**
     * @author VincentZ4
     * Should fire when iDrive Knob has been pressed.
     */
    iDriveKnobPressed,

    /**
     * @author VincentZ4
     * Should fire when iDrive Knob has been pressed and released.
     */
    iDriveKnobReleased,

    /**
     * @author VincentZ4
     * Should fire when iDrive Knob has been turned clockwise.
     */
    iDriveKnobTurnClockwise,

    /**
     * @author VincentZ4
     * Should fire when iDrive Knob has been turned counter clockwise.
     */
    iDriveKnobTurnCounterClockwise,

    /**
     * @author VincentZ4
     * When Menu Button of iDrive is pressed.
     */
    iDriveMenuButtonPressed,

    /**
     * @author VincentZ4
     * When Menu Button of iDrive is pressed and released.
     */
    iDriveMenuButtonReleased,

    /**
     * @author VincentZ4
     * When user holds Telephone Button of iDrive.
     */
    iDriveTelephoneButtonLongPress,

    /**
     * @author VincentZ4
     * When user releases Telephone Button of iDrive.
     */
    iDriveTelephoneButtonLongRelease,

    /**
     * @author VincentZ4
     * When user presses Back Button of iDrive.
     */
    iDriveBackButtonPress,

    /**
     * @author VincentZ4
     * When user releases Back Button of iDrive.
     */
    iDriveBackButtonRelease,

    /**
     * @author VincentZ4
     * When user presses Options Button of iDrive.
     */
    iDriveOptionsButtonPress,

    /**
     * @author VincentZ4
     * When user releases Options Button of iDrive.
     */
    iDriveOptionsButtonRelease,

    /**
     * @author VincentZ4
     * When user presses Navigation Button of iDrive.
     */
    iDriveNavigationButtonPressed,

    /**
     * @author VincentZ4
     * When user releases Navigation Button of iDrive.
     */
    iDriveNavigationButtonReleased,

    /**
     * @author Snaggle
     * When the user presses the voice command button on the steering wheel.
     */
    VoiceCommandPress,

    /**
     * @author Snaggle
     * When the user presses the voice command button on the steering wheel.
     */
    VoiceCommandRelease,

    /**
     * @author Snaggle
     */
    IsParkingBeltEvent,

    /**
     * @author VincentZ4
     */
    ParkingBreakReleased,

    /**
     * @author VincentZ4
     */
    ParkingBreakOnAndBeltOff,

    /**
     * @author VincentZ4
     */
    BeltOn,

    /**
     * @author Snaggly
     * This has not been tested!
     */
    ParkingBreakReleasedAndBeltOn,

    /**
     * @author Snaggle
     */
    IsDoorEvent,

    /**
     * @author VincentZ4
     */
    AllDoorsClosed,

    /**
     * @author VincentZ4
     */
    FrontLeftDoorOpened,

    /**
     * @author VincentZ4
     */
    FrontRightDoorOpened,

    /**
     * @author VincentZ4
     */
    FrontDoorsOpened,

    /**
     * @author VincentZ4
     */
    TrunkOpened,

    /**
     * @author VincentZ4
     */
    TrunkAndLeftDoorOpened,

    /**
     * @author VincentZ4
     */
    TrunkAndRightDoorOpened,

    /**
     * @author VincentZ4
     */
    TrunkAndDoorsOpened,

    /**
     * @author VincentZ4
     */
    ParkingRadarViewOn,

    /**
     * @author VincentZ4
     */
    ParkingRadarViewOff,

}
