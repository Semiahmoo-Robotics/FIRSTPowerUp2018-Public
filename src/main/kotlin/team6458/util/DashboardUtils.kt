package team6458.util

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard


/**
 * Adds utility methods for getting/putting SmartDashboard values.
 * Supported are the Strings, the LEDs, the Sliders, and the Buttons.
 * The put methods will return the last value (or null/zero).
 * You can also read LEDs because it contains an internal cache when you set them in this class.
 *
 *
 * https://wpilib.screenstepslive.com/s/4485/m/24192/l/272692?data-resolve-url=true&data-manual-id=24192
 */
object DashboardUtils {

    private val leds: MutableMap<Int, Boolean> = mutableMapOf()
    val dashboard: SmartDashboard = SmartDashboard() // ???

    private fun rangeCheck(num: Int, max: Int) {
        if (num < 0)
            throw IllegalArgumentException("Number $num cannot be less than 0!")

        if (num > max)
            throw IllegalArgumentException("Number $num cannot be more than $max!")
    }

    /**
     * Shortcut to SmartDashboard#getString("DB/String 0..9", def)
     */
    fun getString(num: Int, def: String?): String? {
        rangeCheck(num, 9)
        return SmartDashboard.getString("DB/String $num", def)
    }

    /**
     * Shortcut to SmartDashboard#putString("DB/String 0..9", value)
     *
     * @return The old value (default null)
     */
    fun putString(num: Int, value: String): String? {
        rangeCheck(num, 9)
        val old = getString(num, null)

        SmartDashboard.putString("DB/String $num", value)
        return old
    }

    /**
     * Shortcut to SmartDashboard#getBoolean("DB/Button 0..3", def)
     */
    fun getButton(num: Int, def: Boolean): Boolean {
        rangeCheck(num, 3)
        return SmartDashboard.getBoolean("DB/Button $num", def)
    }

    /**
     * Shortcut to SmartDashboard#putBoolean("DB/Button 0..3", value)
     *
     * @return The old value (default false)
     */
    fun putButton(num: Int, value: Boolean): Boolean {
        rangeCheck(num, 3)
        val old = getButton(num, false)

        SmartDashboard.putBoolean("DB/Button $num", value)
        return old
    }

    /**
     * Toggles the button and returns the old value.
     *
     * @return The old value (default false)
     */
    fun toggleButton(num: Int): Boolean {
        return putButton(num, !getButton(num, false))
    }

    /**
     * Shortcut to getting the LED values
     */
    fun getLED(num: Int, def: Boolean): Boolean {
        rangeCheck(num, 3)
        synchronized(leds) {
            return leds.getOrDefault(num, def)
        }
    }

    /**
     * Shortcut to SmartDashboard#putBoolean("DB/LED 0..3", value)
     *
     * @return The old value (default false)
     */
    fun putLED(num: Int, value: Boolean): Boolean {
        rangeCheck(num, 3)
        val old = getLED(num, false)

        SmartDashboard.putBoolean("DB/LED $num", value)
        synchronized(leds) {
            leds.put(num, value)
        }
        return old
    }

    /**
     * Toggles the LED and returns the old value.
     *
     * @return The old value (default false)
     */
    fun toggleLED(num: Int): Boolean {
        return putLED(num, !getLED(num, false))
    }

    /**
     * Shortcut to SmartDashboard#getNumber("DB/Slider 0..3", def)
     */
    fun getSlider(num: Int, def: Double): Double {
        rangeCheck(num, 3)
        return SmartDashboard.getNumber("DB/Slider $num", def)
    }

    /**
     * Shortcut to SmartDashboard#putNumber("DB/Slider 0..3", value)
     *
     * @return The old value (default 0.0)
     */
    fun putSlider(num: Int, value: Double): Double {
        rangeCheck(num, 3)
        val old = getSlider(num, 0.0)

        SmartDashboard.putNumber("DB/Slider $num", value)
        return old
    }

    /**
     * Shortcut to SmartDashboard#getBoolean("DB/Button 3", false)
     */
    fun isDebug(): Boolean {
        return SmartDashboard.getBoolean("DB/Button 3", false)
    }

    /**
     * Shortcut to SmartDashboard#putBoolean("DB/Button 3", value)
     *
     * @return The old value (default false)
     */
    fun setDebug(value: Boolean): Boolean {
        val old = isDebug()

        SmartDashboard.putBoolean("DB/Button 3", value)
        return old
    }

    /**
     * Toggles debug mode and returns the old value.
     *
     * @return The old value (default false)
     */
    fun toggleDebug(): Boolean {
        return setDebug(!isDebug())
    }

}