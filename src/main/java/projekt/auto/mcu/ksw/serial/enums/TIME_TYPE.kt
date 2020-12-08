package projekt.auto.mcu.ksw.serial.enums

enum class TIME_TYPE(val value: Byte) {
    TYPE_24HOURS(0x0),
    TYPE_12HOURS(0x1)
}