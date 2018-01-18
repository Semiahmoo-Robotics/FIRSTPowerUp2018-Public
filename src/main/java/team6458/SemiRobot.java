package team6458;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team6458.subsystem.Drivetrain;
import team6458.subsystem.Sensors;
import team6458.util.CameraSetup;
import team6458.util.DashboardKeys;
import team6458.util.PlateAssignment;
import team6458.util.exception.GetBeforeInitException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main robot class.
 */
public final class SemiRobot extends TimedRobot {

    private static final Logger LOGGER = Logger.getLogger(SemiRobot.class.getName());

    private PlateAssignment plateAssignment = PlateAssignment.ALL_INVALID;

    // Operator control
    private OperatorControl opControl;

    // Subsystems
    private Drivetrain drivetrain;
    private Sensors sensors;

    @Override
    public void robotInit() {
        LOGGER.log(Level.INFO, "\n==========================\nStarting initialization...\n==========================\n");

        // Disables any commands that may run
        Scheduler.getInstance().disable();

        opControl = new OperatorControl(this);

        // Start up the subsystems
        {
            drivetrain = new Drivetrain(this);
            sensors = new Sensors(this);
        }

        // Setup the default camera and log the result (successful or not)
        if (CameraSetup.setupDefaultCamera()) {
            LOGGER.log(Level.INFO, "Default camera started");
        } else {
            LOGGER.log(Level.WARNING, "Failed to start default camera!");
        }

        // Initially write values to the SmartDashboard/Shuffleboard so they can be displayed as widgets
        // Use the DashboardKeys class for string IDs
        {
            // Show the positions of the switches and scale fed from the FMS
            SmartDashboard.putString(DashboardKeys.FMS_GAME_DATA, getPlateAssignment().toString());
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
    }

    // ---------------------------------------------------------------------------

    @Override
    public void robotPeriodic() {
        getOperatorControl().periodicUpdate();

        // Run the scheduler. This does nothing if it is disabled.
        Scheduler.getInstance().run();
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

    // Subsystem getters

    public Drivetrain getDrivetrain() {
        if (drivetrain == null) {
            throw new GetBeforeInitException("drivetrain");
        }
        return drivetrain;
    }

    public Sensors getSensors() {
        return sensors;
    }

    // Private methods

    /**
     * Internal method that updates the plate assignment from the Field Management System.
     */
    private void updatePlateAssignmentFromFMS() {
        final boolean isFMSAttached = DriverStation.getInstance().isFMSAttached();
        final String fmsData = DriverStation.getInstance().getGameSpecificMessage();
        final PlateAssignment oldAssignment = plateAssignment;
        if (fmsData == null) {
            /*
            Note: a reference equality check is valid here because ALL_INVALID is the only possible "unknown"
            constant that is settable in these conditional branches
             */
            if (plateAssignment != PlateAssignment.ALL_INVALID) {
                LOGGER.log(Level.INFO,
                        "Plate assignment set to ALL_INVALID, got null, was " + plateAssignment + " (FMS attached: " +
                                isFMSAttached + ")");
                plateAssignment = PlateAssignment.ALL_INVALID;
            }
        } else {
            if (!plateAssignment.toString().equals(fmsData)) {
                LOGGER.log(Level.INFO, String.format("Plate assignment set to %s, was %s", fmsData, plateAssignment.toString()));
                plateAssignment = new PlateAssignment(fmsData);
            }
        }

        if (plateAssignment != oldAssignment) {
            // Update SmartDashboard data
            SmartDashboard.putString(DashboardKeys.FMS_GAME_DATA, getPlateAssignment().toString());
        }
    }

    // Getters and setters

    public OperatorControl getOperatorControl() {
        if (opControl == null) {
            throw new GetBeforeInitException("operator control");
        }
        return opControl;
    }

    /**
     * @return The non-null plate assignment
     */
    public PlateAssignment getPlateAssignment() {
        return plateAssignment;
    }
}
