package team6458

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.command.Scheduler
import kotlinx.coroutines.experimental.launch
import team6458.util.CameraSetup
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.measureNanoTime

/**
 * The main robot class.
 */
class SemiRobot : IterativeRobot() {

    private val logger = Logger.getLogger(this::class.java.name)

    override fun robotInit() {
        // Disable any commands that are running, if present
        Scheduler.getInstance().disable()

        // Start the camera asynchronously
        launch {
            val nano = measureNanoTime {
                CameraSetup.setupDefaultCamera()
            }
            logger.log(Level.INFO, "Started default camera in ${nano / 1_000_000f} ms")
        }
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