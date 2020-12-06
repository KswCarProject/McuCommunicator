package projekt.auto.mcu.ksw.reflection

import android.annotation.SuppressLint
import dalvik.system.DexClassLoader

object McuCommunicator {

    /**
     * Gets the system MCU information
     */
    @JvmStatic
    fun getMcuVer(): String? {
        return try {
            @SuppressLint("PrivateApi")
            val cls = DexClassLoader(
                    "/system/app/KswPLauncher/KswPLauncher.apk",
                    "/data/tmp/",
                    "/data/tmp/",
                    ClassLoader.getSystemClassLoader()).loadClass("com.wits.ksw.settings.utlis_view.McuUtil")
            val method = cls.getDeclaredMethod("getMcuVersion")
            method.invoke(null) as String
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Sends a system MCU command directly to CenterService
     *
     * @param command       Command
     * @param subCommand    Sub-command
     */
    @JvmStatic
    fun sendMcuCommand(command: Int, subCommand: Int): Boolean {
        return try {
            @SuppressLint("PrivateApi")
            val cls = DexClassLoader(
                    "/system/app/KswPLauncher/KswPLauncher.apk",
                    "/data/tmp/",
                    "/data/tmp/",
                    ClassLoader.getSystemClassLoader()).loadClass("com.wits.pms.statuscontrol.WitsCommand")
            val method = cls.getDeclaredMethod("sendCommand", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
            method.invoke(null, command, subCommand, null)
            true
        } catch (e: Exception) {
            false
        }
    }
}