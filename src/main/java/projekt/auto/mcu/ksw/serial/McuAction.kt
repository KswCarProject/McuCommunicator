package projekt.auto.mcu.ksw.serial

interface McuAction {
    fun update(cmdType: Int, data: ByteArray?)
}