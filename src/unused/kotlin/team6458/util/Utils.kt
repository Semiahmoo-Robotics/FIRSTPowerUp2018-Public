package team6458.util

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController
import kotlin.math.absoluteValue

/**
 * Get the "raw" trigger axis, -1..1 where -1 is left, 1 is right
 */
fun XboxController.getTriggerAxis(): Double {
    return (-(this.getTriggerAxis(GenericHID.Hand.kLeft).absoluteValue)
            + this.getTriggerAxis(GenericHID.Hand.kRight).absoluteValue).coerceIn(-1.0..1.0)
}
