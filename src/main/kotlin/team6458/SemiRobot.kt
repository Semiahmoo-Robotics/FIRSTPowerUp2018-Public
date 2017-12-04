package team6458

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.command.Scheduler
import java.util.logging.Logger

/**
 * The main robot class.
 */
class SemiRobot : IterativeRobot() {

    private val logger = Logger.getLogger(this::class.java.name)

    override fun robotInit() {

    }

    override fun disabledInit() {
        Scheduler.getInstance().disable()
    }

    override fun autonomousInit() {
        onDisabledModeLeave()
    }

    override fun teleopInit() {
        onDisabledModeLeave()
    }

    override fun testInit() {
        onDisabledModeLeave()
    }


    override fun disabledPeriodic() {

    }

    override fun autonomousPeriodic() {
        // ensures commands are run
        Scheduler.getInstance().run()
    }

    override fun teleopPeriodic() {
        // ensures commands are run
        Scheduler.getInstance().run()
    }

    override fun testPeriodic() {

    }

    private fun onDisabledModeLeave() {
        Scheduler.getInstance().enable()
    }
}