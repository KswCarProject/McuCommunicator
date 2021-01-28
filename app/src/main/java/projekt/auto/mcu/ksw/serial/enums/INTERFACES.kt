package projekt.auto.mcu.ksw.serial.enums

enum class INTERFACES(val value: Byte) {
    Android_Mode(1),
    OEM_Mode(2),
    DVR(5),
    AUX(6),
    DVD_KSW(8),
    CMMB(9),
    DVD_USR(12)
}