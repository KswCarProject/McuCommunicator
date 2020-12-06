package projekt.auto.mcu.ksw

@Suppress("unused")
class McuConstants {

    object BtSubCommand {
        const val AUTO_CONN = 106
        const val CLOSE_BT = 105
        const val MUSIC_NEXT = 101
        const val MUSIC_PAUSE = 104
        const val MUSIC_PLAY = 103
        const val MUSIC_PLAYPAUSE = 102
        const val MUSIC_PREVIOUS = 100
        const val MUSIC_RELEASE = 113
        const val MUSIC_UNRELEASE = 114
        const val OPEN_BT = 107
        const val PHONE_ACCEPT = 112
        const val PHONE_CALL = 108
        const val PHONE_HANDUP = 109
        const val VOICE_TO_PHONE = 110
        const val VOICE_TO_SYSTEM = 111
    }

    object CanSubCommand {
        const val CLOSE_PANORAMIC = 106
        const val LOOK_AHEAD = 104
        const val LOOK_BEHIND = 105
        const val LOOK_LEFT = 102
        const val LOOK_RIGHT = 103
        const val OPEN_PANORAMIC = 101
    }

    object MediaSubCommand {
        const val CLOSE_MUSIC = 106
        const val CLOSE_PIP = 118
        const val CLOSE_VIDEO = 112
        const val MUSIC_LIST_CLOSE = 125
        const val MUSIC_LIST_OPEN = 124
        const val MUSIC_LOOP_ALL = 121
        const val MUSIC_LOOP_RANDOM = 123
        const val MUSIC_LOOP_SINGLE = 122
        const val MUSIC_NEXT = 101
        const val MUSIC_PAUSE = 105
        const val MUSIC_PLAY = 104
        const val MUSIC_PLAYPAUSE = 103
        const val MUSIC_PREVIOUS = 100
        const val MUSIC_RANDOM = 120
        const val PIP_NEXT = 114
        const val PIP_PAUSE = 117
        const val PIP_PLAY = 116
        const val PIP_PLAYPAUSE = 115
        const val PIP_PREVIOUS = 113
        const val VIDEO_NEXI = 108
        const val VIDEO_PAUSE = 111
        const val VIDEO_PLAY = 110
        const val VIDEO_PLAYPAUSE = 109
        const val VIDEO_PREVIOUS = 107
    }

    object SystemCommand {
        const val ACCEPT_PHONE = 116
        const val ANDROID_MODE = 602
        const val BACK = 115
        const val BENZ_CONTROL = 801
        const val CALL_BUTTON = 123
        const val CAR_MODE = 601
        const val CLOSE_FM = 124
        const val DORMANT = 118
        const val EXPORT_CONFIG = 300
        const val HANDUP_PHONE = 117
        const val HOME = 114
        const val KEY_VOLTAGE = 702
        const val LIGHT_CONTROL_COLOR = 701
        const val MCU_UPDATE = 700
        const val MEDIA_NEXT = 104
        const val MEDIA_PAUSE = 106
        const val MEDIA_PLAY = 105
        const val MEDIA_PLAY_PAUSE = 121
        const val MEDIA_PREVIOUS = 103
        const val MUTE = 100
        const val MUTE_NAVI = 900
        const val NEXT_FM = 120
        const val OPEN_AUX = 605
        const val OPEN_BT = 607
        const val OPEN_CVBSDVR = 609
        const val OPEN_DTV = 606
        const val OPEN_FM = 110
        const val OPEN_F_CAM = 610
        const val OPEN_MODE = 604
        const val OPEN_NAVI = 108
        const val OPEN_SETTINGS = 111
        const val OPEN_SPEECH = 109
        const val OUT_MODE = 603
        const val PREV_FM = 119
        const val SCREEN_OFF = 113
        const val SCREEN_ON = 112
        const val SOURCE_CHANGE = 107
        const val SYSTEM_READY = 99
        const val UPDATE_CONFIG = 200
        const val UPDATE_MCU = 202
        const val UPDATE_SYSTEM = 201
        const val USB_HOST = 122
        const val USING_NAVI = 608
        const val VOLUME_DOWN = 102
        const val VOLUME_UP = 101
    }
}