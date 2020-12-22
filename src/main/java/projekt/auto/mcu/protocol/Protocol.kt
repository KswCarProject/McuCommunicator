package projekt.auto.mcu.protocol

interface Protocol {

    /**
     * Sends an MCU command to the corresponding communication protocol
     */
    fun sendMcuCommands(channel: Int, message: Int): Boolean

    /**
     * Obtains the current MCU version on the device
     */
    val getMcuVersion: String

    /**
     * Reads the MCU commands in the corresponding protocol's method
     */
    fun readMcuCommands()

    /**
     * Switches to the NTG/iDrive/etc. screen
     */
    fun switchToCar()

    /**
     * Switch back to Android
     */
    fun switchToAndroid()

    /**
     * Switch to the Audio Source
     */
    fun switchToAudioSource()

    /**
     * Switch to ATSL
     */
    fun switchToAtsl()

}