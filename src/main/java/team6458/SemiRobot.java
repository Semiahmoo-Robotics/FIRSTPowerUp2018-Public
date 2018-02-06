package team6458;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team6458.cmd.DriveStraightCommand;
import team6458.cmd.GyroCalibrationCommand;
import team6458.cmd.RotateCommand;
import team6458.cmd.RotateCommand.SpeedGradient;
import team6458.subsystem.Drivetrain;
import team6458.subsystem.Sensors;
import team6458.util.DashboardKeys;
import team6458.util.PlateAssignment;
import team6458.util.exception.GetBeforeInitException;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static team6458.util.DashboardKeys.*;

/**
 * The main robot class.
 */
public final class SemiRobot extends TimedRobot {

    private static final Logger LOGGER = Logger.getLogger(SemiRobot.class.getName());
    // SendableChoosers
    private final SendableChooser<Command> debugCommands = new SendableChooser<>();
    private final SendableChooser<Supplier<Command>> autoChooser = new SendableChooser<>();
    // Plate assignment
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
            // TODO put the three sides here
            SmartDashboard.putData(CHOOSER_AUTONOMOUS, autoChooser);

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
            debugCommands.addDefault("None", new InstantCommand());

            // RotateCommand tests
            final int[] angles = {20, 45, 50, 90, 180, 360};
            Arrays.stream(angles).forEach(d -> {
                debugCommands.addObject("Turn -" + d + " deg (LEFT)", new RotateCommand(this, -d));
                debugCommands.addObject("Turn +" + d + " deg (RIGHT)", new RotateCommand(this, d));
            });

            // Encoder tests
            final double[] distances = {0.5, 1.0, 2.0, 3.0};
            for (double distance : distances) {
                debugCommands.addObject("Drive " + distance + " m",
                        new DriveStraightCommand(this, distance, 0.25));
            }

            debugCommands.addObject("Taunt", new CommandGroup() {
                {
                    final SpeedGradient grad = new SpeedGradient(0.4, 0.4, 0, 0);
                    final double waggle = 20.0;
                    addSequential(new RotateCommand(SemiRobot.this, 180.0, grad));
                    addSequential(new RotateCommand(SemiRobot.this, -waggle, grad));
                    addSequential(new RotateCommand(SemiRobot.this, waggle * 2, grad));
                    addSequential(new RotateCommand(SemiRobot.this, -waggle * 2, grad));
                    addSequential(new RotateCommand(SemiRobot.this, waggle * 2, grad));
                    addSequential(new RotateCommand(SemiRobot.this, -waggle * 2, grad));
                    addSequential(new RotateCommand(SemiRobot.this, waggle * 2, grad));
                    addSequential(new RotateCommand(SemiRobot.this, -waggle * 2, grad));
                    addSequential(new RotateCommand(SemiRobot.this, waggle + 180.0, grad));
                }
            });

            SmartDashboard.putData("DEBUG (Enabling Test Mode will run sel. command)", debugCommands);
        }

        LOGGER.log(Level.INFO, "\n==============================\nRobot initialization complete.\n==============================\n");
    }

    @Override
    public void disabledInit() {
        // Disables any commands that may run
        Scheduler.getInstance().disable();
        Scheduler.getInstance().removeAll();
    }

    @Override
    public void autonomousInit() {
        updatePlateAssignmentFromFMS();

        // Enables commands to be run
        Scheduler.getInstance().enable();

        // Choose autonomous program
        final Supplier<Command> supplier = autoChooser.getSelected();
        if (supplier == null) {
            LOGGER.log(Level.WARNING, "Null auto command");
        } else {
            // TODO start cmd
//            final Command cmd = supplier.get();
        }
    }

    @Override
    public void teleopInit() {
        // Enables commands to be run
        Scheduler.getInstance().enable();
    }

    @Override
    public void testInit() {
        final Command cmd = debugCommands.getSelected();
        Scheduler.getInstance().removeAll();
        if (cmd != null) {
            // Enables commands to be run
            Scheduler.getInstance().enable();
            cmd.start();
        }
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
