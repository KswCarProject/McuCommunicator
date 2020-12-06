package projekt.auto.mcu.protocol

import projekt.auto.mcu.ksw.reflection.McuCommunicator

internal class KswCommunicator : Communicator {

    override fun sendMcuCommands(channel: Int, message: Int): Boolean {
        return McuCommunicator.sendMcuCommand(channel, message)
    }

    override val getMcuVersion: String
        get() = TODO("Not yet implemented")


    override val getMcuProtocol: String
        get() = "KswCommunicator"

}