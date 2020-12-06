package projekt.auto.mcu.protocol

import projekt.auto.mcu.constants.KswConstants
import projekt.auto.mcu.ksw.reflection.McuCommunicator
import projekt.auto.mcu.ksw.reflection.McuCommunicator.sendMcuCommand

@Deprecated("Reflection methods for MCU communication are currently very limited, we will be using the Serial methods for more flexibility")
internal class KswReflectionProtocol : Protocol {

    override fun sendMcuCommands(channel: Int, message: Int): Boolean {
        return McuCommunicator.sendMcuCommand(channel, message)
    }

    override val getMcuVersion: String
        get() = McuCommunicator.getMcuVer()!!


    override fun readMcuCommands() {
        TODO("Not yet implemented")
    }

    override fun switchToCar() {
        sendMcuCommand(1, KswConstants.SystemCommand.CAR_MODE)
    }

    override fun switchToAndroid() {
        sendMcuCommand(1, KswConstants.SystemCommand.ANDROID_MODE)
    }

    override fun switchToAudioSource() {
        TODO("Not yet implemented")
    }

    override fun switchToAtsl() {
        TODO("Not yet implemented")
    }

}