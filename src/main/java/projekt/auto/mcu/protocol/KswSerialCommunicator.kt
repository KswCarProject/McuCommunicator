package projekt.auto.mcu.protocol

import projekt.auto.mcu.protocol.Communicator

internal class KswSerialCommunicator : Communicator {

    override fun sendMcuCommands(channel: Int, message: Int): Boolean {
        TODO("Not yet implemented")
    }

    override val getMcuVersion: String
        get() = TODO("Not yet implemented")


    override val getMcuProtocol: String
        get() = "KswSerialCommunicator"

}