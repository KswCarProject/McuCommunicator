package projekt.auto.mcu.ksw.serial.enums

enum class BLUETOOTH_STATE(val value: Byte) {
    NORMAL_MODE(0x0),
    CALL_MODE(0x1),
    UNKNOWN(0x02),
    UNUSED(0x03),
    BT_IN_BACKCAR(0x4)
}