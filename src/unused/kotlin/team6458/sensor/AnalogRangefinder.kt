package team6458.sensor

import edu.wpi.first.wpilibj.AnalogInput


class AnalogRangefinder(val analog: AnalogInput, val scalingFactorVoltsPerInch: Double = (5.0 / 512.0)) {

    enum class Units(val inchMult: Double, val unitName: String) {
        INCHES(1.0, "in"), CM(2.54, "cm");

        fun unitify(d: Double): String = d.toString() + " " + unitName
    }

    fun getDistance(units: Units): Double {
        return (analog.voltage / scalingFactorVoltsPerInch) * units.inchMult
    }

    fun getStringDistance(units: Units): String = units.unitify(getDistance(units))

}
