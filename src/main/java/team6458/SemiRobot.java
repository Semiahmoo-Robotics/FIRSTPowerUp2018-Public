package team6458;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.TimedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team6458.cmd.GyroCalibrationCommand;
import team6458.cmd.RotateCommand;
import team6458.subsystem.Drivetrain;
import team6458.subsystem.Sensors;
import team6458.util.DashboardKeys;
import team6458.util.PlateAssignment;
import team6458.util.exception.GetBeforeInitException;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static team6458.util.DashboardKeys.*;

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
            // Sensors should be last: a gyroscope will be calibrated for around 5 seconds blocking the thread
            sensors = new Sensors(this);
        }

        // Setup the default camera and log the result (successful or not)
//        if (CameraSetup.setupDefaultCamera()) {
//            LOGGER.log(Level.INFO, "Default camera started");
//        } else {
//            LOGGER.log(Level.WARNING, "Failed to start default camera!");
//        }

        // Write one-time values to the SmartDashboard/Shuffleboard so they can be displayed as widgets
        // Use the DashboardKeys class for string IDs
        // All other continuously updated values are updated in robotPeriodic
        {
            // One-time init so that they appear first
            updateSmartDashboardPeriodic();

            // Autonomous command selection

            // Self-updating sendables, like the gyroscope and encoders
            SmartDashboard.putData(GYROSCOPE, getSensors().gyro);
            SmartDashboard.putData(LEFT_ENCODER, getDrivetrain().leftEncoder);
            SmartDashboard.putData(RIGHT_ENCODER, getDrivetrain().rightEncoder);

            // Commands
            SmartDashboard.putData(CMD_GYRO_CALIBRATE, new GyroCalibrationCommand(this));
            SmartDashboard.putData(CMD_RESET_ENCODERS, new InstantCommand() {
                @Override
                protected void execute() {
                    super.execute();
                    getDrivetrain().resetEncoders();
                }
            });

            // TESTS -----------------------------------------------------------------------
            SmartDashboard.putString("Debug Tests (Teleop only)", "Start/cancel a command below (only one active at a time)");

            // RotateCommand tests
            final int[] angles = {15, 45, 90, 180, 270, 360};
            Arrays.stream(angles).forEach(d -> {
                SmartDashboard.putData("TEST (Gyro): Turn +" + d + " deg (RIGHT)", new RotateCommand(this, d));
                SmartDashboard.putData("TEST (Gyro): Turn -" + d + " deg (LEFT)", new RotateCommand(this, -d));
            });

            // Gearbox wearing-in
            final String KEY_WEARING_IN = "Time left on Gearbox Wearing-in";
            final int WEARING_IN_TIME = 30 * 60;
            SmartDashboard.putData("TEST (Gearboxes): Wear in gearboxes for " + (WEARING_IN_TIME / 60) + " minutes",
                    new TimedCommand("Gearbox Wearing-in", WEARING_IN_TIME) {
                        private long startTime = System.currentTimeMillis();

                        {
                            requires(getDrivetrain());
                        }

                        @Override
                        public synchronized void start() {
                            super.start();
                            startTime = System.currentTimeMillis();
                        }

                        @Override
                        protected void end() {
                            super.end();
                            getDrivetrain().drive.stopMotor();
                            SmartDashboard.putString(KEY_WEARING_IN, "Command ended");
                        }

                        @Override
                        protected void execute() {
                            super.execute();
                            getDrivetrain().drive.curvatureDrive(0.3, 0.0, true);
                            double timeLeft = WEARING_IN_TIME - ((System.currentTimeMillis() - startTime) / 1000.0);
                            SmartDashboard.putString(KEY_WEARING_IN,
                                    String.format("%.2f", (timeLeft > 60.0 ? timeLeft / 60 : timeLeft)) + " " +
                                            (timeLeft > 60.0 ? "min" : "sec") + " left");
                        }

                        @Override
                        public synchronized boolean isInterruptible() {
                            return true;
                        }
                    });
            SmartDashboard.putString(KEY_WEARING_IN, "Not started yet");
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

        // Update SmartDashboard
        updateSmartDashboardPeriodic();
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
        if (sensors == null) {
            throw new GetBeforeInitException("sensors");
        }
        return sensors;
    }

    // Private methods

    /**
     * Update certain values on the SmartDashboard.
     */
    private void updateSmartDashboardPeriodic() {
    }

    /**
     * Internal method that updates the plate assignment from the Field Management System.
     */
    private void updatePlateAssignmentFromFMS() {
        final boolean isFMSAttached = DriverStation.getInstance().isFMSAttached();
        final String fmsData = DriverStation.getInstance().getGameSpecificMessage();
        final PlateAssignment oldAssignment = plateAssignment;
        if (fmsData == null || fmsData.equals("")) {
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
                plateAssignment = PlateAssignment.fromString(fmsData);
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
