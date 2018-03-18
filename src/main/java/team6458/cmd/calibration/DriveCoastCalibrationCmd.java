package team6458.cmd.calibration;

import team6458.SemiRobot;
import team6458.cmd.RobotCommand;
import team6458.feedback.CoastDistance;
import team6458.util.PreferenceKeys;
import team6458.util.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import static team6458.cmd.DriveStraightCommand.GYRO_CORRECTION;

/**
 * Calibrates and finds the amount of coasting at certain throttle to speed to distance values.
 * Optionally persists the data if everything succeeded (no cancellations).
 */
public class DriveCoastCalibrationCmd extends CalibrationCommand {

    private static final Logger LOGGER = Logger.getLogger(DriveCoastCalibrationCmd.class.getName());

    public final boolean persistResults;

    /**
     * Constructor. Will run forwards AND backwards doing calibration.
     *
     * @param robot          The robot instance
     * @param persistResults Whether or not to persist the results if everything succeeded
     * @param distance       The distance to run up before coasting
     * @param throttle1      The first throttle to run at
     * @param throttle2      The second throttle to run at
     * @param throttles      The rest of the throttles to run at
     */
    public DriveCoastCalibrationCmd(final SemiRobot robot, final boolean persistResults,
                                    final double distance,
                                    final double throttle1, final double throttle2,
                                    final double... throttles) {
        super(robot);
        this.persistResults = persistResults;

        addSequential(new DrivePopulateCommand(robot, distance, throttle1));
        addSequential(new DrivePopulateCommand(robot, -distance, throttle2));
        for (int i = 0; i < throttles.length; i++) {
            final double throttle = throttles[i];
            addSequential(new DrivePopulateCommand(robot, distance * (i % 2 == 0 ? 1 : -1), throttle));
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        LOGGER.log(Level.INFO, "Starting drive coast calibration");
    }

    @Override
    protected void end() {
        super.end();

        final CoastDistance result = getResult();
        if (result != null && wasSuccessful() && persistResults) {
            result.persist(PreferenceKeys.DRIVE_STRAIGHT_CALIBRATION, false);
            robot.setDriveStraightCoastDist(result);
        }
        LOGGER.log(Level.INFO,
                "Finished drive coast calibration: persist=" + persistResults + ", success=" + wasSuccessful());
        if (result != null) {
            LOGGER.log(Level.INFO, result.toJson(true));
        }
    }

    class DrivePopulateCommand extends RobotCommand {

        public static final long STOPPED_WAIT_TIME = 1500L;

        protected final double distance;
        protected final double throttle;
        private double coastSpeed = 0.0;
        private double coastDistance = 0.0;
        private double initialHeading;
        private boolean isCountingCoast = false;
        private long timeWhenStopped = 0L;
        private boolean kill = false;

        protected DrivePopulateCommand(SemiRobot robot, double distance, double throttle) {
            super(robot);
            requires(robot.getDrivetrain());
            this.distance = distance;
            this.throttle = Math.abs(throttle);
        }

        @Override
        protected void initialize() {
            super.initialize();
            initialHeading = robot.getSensors().gyro.getAngle();
            robot.getDrivetrain().resetEncoders();
            isCountingCoast = false;
            coastSpeed = coastDistance = 0.0;
            timeWhenStopped = 0L;
            kill = false;
        }

        @Override
        protected void execute() {
            super.execute();

            if (!isCountingCoast) {
                final double currentHeading = robot.getSensors().gyro.getAngle();
                final double angleDiff = currentHeading - initialHeading;

                robot.getDrivetrain().drive.curvatureDrive(Math.copySign(throttle, distance),
                        angleDiff * -GYRO_CORRECTION, false);

                if (Math.abs(distance - robot.getDrivetrain().getAverageDistance()) <= 0.0) {
                    coastSpeed = Math.abs(robot.getDrivetrain().getAverageRate());
                    isCountingCoast = true;
                    robot.getDrivetrain().drive.stopMotor();
                    robot.getDrivetrain().resetEncoders();

                    LOGGER.log(Level.INFO, "Starting coast count: throttle=" + throttle + ", speed=" + coastSpeed);
                }
            } else {
                robot.getDrivetrain().drive.stopMotor();
                if (timeWhenStopped <= 0 && Utils.isEqual(robot.getDrivetrain().getAverageRate(), 0.0, 0.001)) {
                    timeWhenStopped = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - timeWhenStopped >= STOPPED_WAIT_TIME && !kill) {
                    kill = true;
                    coastDistance = Math.abs(robot.getDrivetrain().getAverageDistance());
                    LOGGER.log(Level.INFO,
                            "Waiting for full stop: speed=" + coastSpeed + ", distance=" + coastDistance);
                }
            }
        }

        @Override
        protected void end() {
            super.end();
            robot.getDrivetrain().drive.stopMotor();

            if (kill) {
                DriveCoastCalibrationCmd.this.inUse.populate(coastSpeed, coastDistance);
                LOGGER.log(Level.INFO, "Populated result: speed=" + coastSpeed + ", distance=" + coastDistance);
            }
        }

        @Override
        protected boolean isFinished() {
            return isTimedOut() || kill;
        }
    }
}
