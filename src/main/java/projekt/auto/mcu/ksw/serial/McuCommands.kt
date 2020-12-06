package projekt.auto.mcu.ksw.serial

interface McuCommands {
    val command: Int
    val data: ByteArray
    val update: Boolean

    companion object {
        val SET_TO_MUSIC_SOURCE: McuCommands = object : McuCommands {
            override val command: Int
                get() = 103
            override val data: ByteArray
                get() = byteArrayOf(1)
            override val update: Boolean
                get() = false
        }
        val SET_TO_ATSL_AIRCONSOLE: McuCommands = object : McuCommands {
            override val command: Int
                get() = 103
            override val data: ByteArray
                get() = byteArrayOf(13)
            override val update: Boolean
                get() = false
        }
        val SWITCH_TO_OEM: McuCommands = object : McuCommands {
            override val command: Int
                get() = 105
            override val data: ByteArray
                get() = byteArrayOf(18, 2)
            override val update: Boolean
                get() = false
        }
        val SWITCH_TO_ANDROID: McuCommands = object : McuCommands {
            override val command: Int
                get() = 105
            override val data: ByteArray
                get() = byteArrayOf(18, 1)
            override val update: Boolean
                get() = false
        }
    }
}