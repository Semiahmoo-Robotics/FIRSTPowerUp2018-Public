package team6458.cmd.calibration;

import team6458.SemiRobot;
import team6458.cmd.RobotCommand;
import team6458.feedback.CoastDistance;
import team6458.util.PreferenceKeys;
import team6458.util.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calibrates and finds the amount of coasting at certain rotate throttle to speed values.
 * Optionally persists the data if everything succeeded (no cancellations).
 */
public class RotateCoastCalibrationCmd extends CalibrationCommand {

    private static final Logger LOGGER = Logger.getLogger(RotateCoastCalibrationCmd.class.getName());

    public final boolean persistResults;

    /**
     * Constructor. Will spin during calibration.
     *
     * @param robot          The robot instance
     * @param persistResults Whether or not to persist the results if everything succeeded
     * @param time           The time to run up before coasting
     * @param throttle1      The first throttle to run at
     * @param throttle2      The second throttle to run at
     * @param throttles      The rest of the throttles to run at
     */
    public RotateCoastCalibrationCmd(final SemiRobot robot, final boolean persistResults,
                                     final double time,
                                     final double throttle1, final double throttle2,
                                     final double... throttles) {
        super(robot);
        this.persistResults = persistResults;

        addSequential(new RotatePopulateCommand(robot, time, throttle1));
        addSequential(new RotatePopulateCommand(robot, -time, throttle2));
        for (int i = 0; i < throttles.length; i++) {
            final double throttle = throttles[i];
            addSequential(new RotatePopulateCommand(robot, time * (i % 2 == 0 ? 1 : -1), throttle));
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        LOGGER.log(Level.INFO, "Starting rotate coast calibration");
    }

    @Override
    protected void end() {
        super.end();
        robot.getDrivetrain().drive.stopMotor();

        final CoastDistance result = getResult();
        if (result != null && wasSuccessful() && persistResults) {
            result.persist(PreferenceKeys.ROTATE_CALIBRATION, false);
            robot.setRotateCoastDist(result);
        }
        LOGGER.log(Level.INFO,
                "Finished rotate coast calibration: persist=" + persistResults + ", success=" + wasSuccessful());
        if (result != null) {
            LOGGER.log(Level.INFO, result.toJson(true));
        }
    }

    class RotatePopulateCommand extends RobotCommand {

        public static final long STOPPED_WAIT_TIME = 1500L;

        protected final double time;
        protected final double throttle;
        private double coastSpeed = 0.0;
        private double coastDistance = 0.0;
        private boolean isCountingCoast = false;
        private long timeWhenStopped = 0L;
        private long startTime = 0L;
        private boolean kill = false;

        protected RotatePopulateCommand(SemiRobot robot, double time, double throttle) {
            super(robot);
            requires(robot.getDrivetrain());
            this.time = time;
            this.throttle = Math.abs(throttle);
        }

        @Override
        protected void initialize() {
            super.initialize();
            robot.getSensors().gyro.reset();
            isCountingCoast = false;
            coastSpeed = coastDistance = 0.0;
            timeWhenStopped = 0L;
            startTime = System.currentTimeMillis();
            kill = false;
        }

        @Override
        protected void execute() {
            super.execute();

            if (!isCountingCoast) {
                robot.getDrivetrain().drive.curvatureDrive(0.0,
                        throttle, false);

                if (System.currentTimeMillis() - startTime >= time * 1000) {
                    coastSpeed = Math.abs(robot.getSensors().gyro.getRate());
                    isCountingCoast = true;
                    robot.getDrivetrain().drive.stopMotor();
                    robot.getSensors().gyro.reset();

                    LOGGER.log(Level.INFO, "Starting coast count: throttle=" + throttle + ", speed=" + coastSpeed);
                }
            } else {
                robot.getDrivetrain().drive.stopMotor();
                if (timeWhenStopped <= 0 && Utils.isEqual(robot.getSensors().gyro.getRate(), 0.0, 1.0)) {
                    timeWhenStopped = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - timeWhenStopped >= STOPPED_WAIT_TIME && !kill) {
                    kill = true;
                    coastDistance = Math.abs(robot.getSensors().gyro.getAngle());
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
                RotateCoastCalibrationCmd.this.inUse.populate(coastSpeed, coastDistance);
                LOGGER.log(Level.INFO, "Populated result: speed=" + coastSpeed + ", distance=" + coastDistance);
            }
        }

        @Override
        protected boolean isFinished() {
            return isTimedOut() || kill;
        }
    }
}
