package projekt.auto.mcu.protocol

import projekt.auto.mcu.ksw.serial.McuCommunicator
import projekt.auto.mcu.ksw.serial.collection.McuCommands

internal class KswProtocol : Protocol {

    override fun sendMcuCommands(channel: Int, message: Int): Boolean {
        TODO("Not yet implemented")
    }

    override val getMcuVersion: String
        get() = TODO("Not yet implemented")


    override fun readMcuCommands() {
        TODO("Not yet implemented")
    }

    override fun switchToCar() {
        McuCommunicator.getInstance().sendCommand(McuCommands.SWITCH_TO_OEM)
    }

    override fun switchToAndroid() {
        McuCommunicator.getInstance().sendCommand(McuCommands.SWITCH_TO_ANDROID)
    }

    override fun switchToAudioSource() {
        McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_MUSIC_SOURCE)
    }

    override fun switchToAtsl() {
        McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_ATSL_AIRCONSOLE)
    }

}