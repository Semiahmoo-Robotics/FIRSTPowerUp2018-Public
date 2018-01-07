package team6458;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import team6458.util.CameraSetup;
import team6458.util.PlateAssignment;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main robot class.
 */
public final class SemiRobot extends TimedRobot {

    private static final Logger LOGGER = Logger.getLogger(SemiRobot.class.getName());

    private PlateAssignment plateAssignment = PlateAssignment.ALL_INVALID;

    /**
     * @return The non-null plate assignment
     */
    public PlateAssignment getPlateAssignment() {
        return plateAssignment;
    }

    /**
     * Internal method that updates the plate assignment from the Field Management System.
     */
    private void updatePlateAssignmentFromFMS() {
        boolean isFMSAttached = DriverStation.getInstance().isFMSAttached();
        String fmsData = DriverStation.getInstance().getGameSpecificMessage();
        if (fmsData == null) {
            /*
            Note: a reference equality check is valid here because ALL_INVALID is the only possible "unknown"
            constant that is settable in these conditional branches
             */
            if (plateAssignment != PlateAssignment.ALL_INVALID) {
                LOGGER.log(Level.INFO, "Plate assignment set to ALL_INVALID, got null, was " + plateAssignment + " (FMS attached: " + isFMSAttached + ")");
                plateAssignment = PlateAssignment.ALL_INVALID;
            }
        } else {
            if (!plateAssignment.toString().equals(fmsData)) {
                LOGGER.log(Level.INFO, String.format("Plate assignment set to %s, was %s", fmsData, plateAssignment));
                plateAssignment = new PlateAssignment(fmsData);
            }
        }
    }

    @Override
    public void robotInit() {
        LOGGER.log(Level.INFO, "\n==========================\nStarting initialization...\n==========================\n");

        // Disables any commands that may run
        Scheduler.getInstance().disable();

        // Setup the default camera and log the result (successful or not)
        if (CameraSetup.setupDefaultCamera()) {
            LOGGER.log(Level.INFO, "Default camera started");
        } else {
            LOGGER.log(Level.WARNING, "Failed to start default camera!");
        }

        LOGGER.log(Level.INFO, "\n==============================\nRobot initialization complete.\n==============================\n");
    }

    @Override
    public void disabledInit() {
        // Disables any commands that may run
        Scheduler.getInstance().disable();
    }

    @Override
    public void autonomousInit() {
        updatePlateAssignmentFromFMS();

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
        updatePlateAssignmentFromFMS();
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
