package projekt.auto.mcu.ksw.serial.enums

enum class MESSAGE_BEAT_MODE (val value: Byte) {
    STOP_THE_CLOCK(0x0),
    CLEAR_MESSAGES_WAIT2SEC(0x1),
    NORMAL_MODE(0x2)
}