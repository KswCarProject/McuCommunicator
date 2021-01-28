package projekt.auto.mcu

import android.app.Application
import projekt.auto.mcu.protocol.Protocol

open class McuCommunicatorApplication : Application() {

    companion object {

        internal var protocol: Protocol? = null

    }

}