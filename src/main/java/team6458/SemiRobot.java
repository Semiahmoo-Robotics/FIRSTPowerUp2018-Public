package team6458;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import team6458.util.CameraSetup;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main robot class.
 */
public class SemiRobot extends IterativeRobot {

    private static final Logger logger = Logger.getLogger(SemiRobot.class.getName());

    @Override
    public void robotInit() {
        logger.log(Level.INFO, "\n==========================\nStarting initialization...\n==========================\n");

        // Disables any commands that may run
        Scheduler.getInstance().disable();

        // Setup the default camera and log the result (successful or not)
        if (CameraSetup.setupDefaultCamera()) {
            logger.log(Level.INFO, "Default camera started");
        } else {
            logger.log(Level.WARNING, "Failed to start default camera!");
        }

        logger.log(Level.INFO, "\n==============================\nRobot initialization complete.\n==============================\n");
    }

    @Override
    public void disabledInit() {
        // Disables any commands that may run
        Scheduler.getInstance().disable();
    }

    @Override
    public void autonomousInit() {
        // Enables commands to be run
        Scheduler.getInstance().enable();
    }

    @Override
    public void teleopInit() {
        // Enables commands to be run
        Scheduler.getInstance().enable();
    }

    @Override
    public void testInit() {
        // Enables commands to be run
        Scheduler.getInstance().enable();
    }

    // ---------------------------------------------------------------------------

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testPeriodic() {
    }
}
