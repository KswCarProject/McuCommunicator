package projekt.auto.mcu

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import projekt.auto.mcu.adb.AdbManager
import projekt.auto.mcu.protocol.KswProtocol
import projekt.auto.mcu.protocol.Protocol

object McuCommunicatorApp {

    private lateinit var mcuType: MCU

    /**
     * MCU Type to determine the proper protocol to execute
     */
    enum class MCU {
        KSW,
        PX6,
        MTK_10
    }

    /**
     * Constructor which returns itself for further instruction
     */
    fun McuCommunicatorApp(@NonNull mcuType: MCU): McuCommunicatorApp {
        this.mcuType = mcuType
        return this
    }

    /**
     * First-hand preparations to start the MCU Communicator library
     */
    @Throws(ImproperMcuApiInitializationException::class)
    fun initialize(context: Context): Protocol {
        when (mcuType) {
            MCU.KSW -> {
                // First, we must check if we have the READ_LOGS permission, as we must be capable of reading the MCU thrown by the CenterService
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_LOGS) != PackageManager.PERMISSION_GRANTED) {
                    if (AdbManager.executeCommands(context, arrayOf("pm grant ${context.packageName} ${Manifest.permission.READ_LOGS}"))) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_LOGS) != PackageManager.PERMISSION_GRANTED) {
                            throw ImproperMcuApiInitializationException("McuCommunicator was unable to grant itself READ_LOGS permission, perhaps you are using the wrong MCU type?")
                        }
                    } else {
                        throw ImproperMcuApiInitializationException("McuCommunicator was unable to execute ADB commands by itself, perhaps you are using the wrong MCU type?")
                    }
                }
                // Next, we set the library application to save the current protocol chosen
                McuCommunicatorApplication.protocol = KswProtocol()
                return McuCommunicatorApplication.protocol as KswProtocol
            }
            MCU.PX6 -> {
                throw ImproperMcuApiInitializationException("McuCommunicator does not support PX6 communication at the current moment")
            }
            MCU.MTK_10 -> {
                throw ImproperMcuApiInitializationException("McuCommunicator does not support MTK_10 communication at the current moment")
            }
        }
    }

    /**
     * Checks if the current Protocol is KSW
     */
    val isKswProtocol: Boolean
        get() = McuCommunicatorApplication.protocol is KswProtocol


    /**
     * Exception thrown when the API is used incorrectly
     */
    class ImproperMcuApiInitializationException(message: String) : Exception(message)

}