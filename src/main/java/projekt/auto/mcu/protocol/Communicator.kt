package projekt.auto.mcu.protocol

interface Communicator {

    /**
     * Sends an MCU command to the corresponding communication protocol
     */
    fun sendMcuCommands(channel: Int, message: Int) : Boolean

    /**
     * Obtains the current MCU version on the device
     */
    val getMcuVersion: String

    /**
     * Obtains the current MCU protocol used
      */
    val getMcuProtocol: String



}