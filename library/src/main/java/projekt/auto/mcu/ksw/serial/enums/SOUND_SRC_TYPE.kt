package projekt.auto.mcu.ksw.serial.enums

enum class SOUND_SRC_TYPE(val typeValue: Byte) {
    SRC_MUSIC(0x1),
    SRC_MOVIE(0x2),
    SRC_BT(0x3),
    SRC_BTMUSIC(0x4),
    SRC_DVR(0x5),
    SRC_AUX(0x6),
    SRC_MOBILE_APP(0x7),
    SRC_DVD(0x8),
    SRC_CMMB(0x9),
    SRC_RADIO(0xA),
    SRC_ATSL_AIRCONSOLE(0xd)
}