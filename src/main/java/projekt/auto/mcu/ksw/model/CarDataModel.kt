package projekt.auto.mcu.ksw.model

import kotlin.experimental.and

/**
 * @author Snaggly
 * The protocol has been taken from VincentZ4's KSWI API Document as referred here:
 * https://f30.bimmerpost.com/forums/showpost.php?p=26332187&postcount=3436
 *
 * Requires to be tested!
 */
class CarDataModel(data: ByteArray) {
    var vehicleRange: Int = 0
        private set
    var averageConsumption: Int = 0
        private set
    var averageSpeed: Int = 0
        private set
    var currentSpeed: Int = 0
        private set
    var currentRPM: Int = 0
        private set
    var leftInTank: Int = 0
        private set
    var temperature: Double = 0.0
        private set
    var isInMiles: Boolean = false
        private set
    var isInFahrenheit: Boolean = false
        private set

    init {
        update(data)
    }

    fun update(newData: ByteArray) {
        vehicleRange = newData[1].toInt().shl(8) + newData[2]
        averageConsumption = newData[3].toInt().shl(8) + newData[4]
        averageSpeed = newData[5].toInt().shl(8) + newData[6]
        currentSpeed = newData[7].toInt().shl(8) + newData[8]
        currentRPM = newData[9].toInt().shl(8) + newData[10]
        leftInTank = newData[11].toInt().shl(8) + newData[12]
        temperature = (newData[13].toInt().shl(8) + newData[14]).toDouble() / 10
        isInMiles = newData[15].and(8) > 0
        isInFahrenheit = newData[15].and(4) > 0
    }
}