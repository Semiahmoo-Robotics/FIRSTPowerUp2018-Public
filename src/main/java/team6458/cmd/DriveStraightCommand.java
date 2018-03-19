package team6458.cmd;

import team6458.SemiRobot;
import team6458.util.ValueGradient;

/**
 * A command that drives straight for X metres, using the encoders on the {@link team6458.subsystem.Drivetrain}
 * to measure distance, and the gyroscope to adjust the heading if drift is an issue.
 */
public class DriveStraightCommand extends RobotCommand {

    /**
     * A proportional constant used for gyroscopic correction. If this value is too high, the robot
     * may oscillate.
     * <p>
     * This value should be tweaked through trial and error for best results.
     */
    public static final double GYRO_CORRECTION = 0.0275;

    /**
     * The distance to travel. May be negative to go backwards.
     */
    public final double distance;
    /**
     * The throttle to go at, as a {@link ValueGradient}.
     */
    public final ValueGradient throttle;

    private double initialHeading;

    /**
     * Constructor.
     * @param distance Distance in metres, may be negative
     * @param throttle Throttle between 0.0 and 1.0 (positive only) as a {@link ValueGradient}
     */
    public DriveStraightCommand(SemiRobot robot, double distance, ValueGradient throttle) {
        super(robot);
        requires(robot.getDrivetrain());

        if (throttle.maximum < 0 || throttle.minimum < 0)
            throw new IllegalArgumentException("Throttle gradient provided has negative values");

        this.throttle = throttle;
        this.distance = distance;
    }

    /**
     * Constructor.
     * @param distance Distance in metres, may be negative
     * @param throttle Maximum throttle between 0.0 and 1.0 (positive only)
     */
    public DriveStraightCommand(SemiRobot robot, double distance, double throttle) {
        super(robot);
        requires(robot.getDrivetrain());

        this.throttle = createThrottleGradient(Math.abs(throttle), Math.abs(distance));
        this.distance = distance;
    }

    public static ValueGradient createThrottleGradient(double maxThrottle, double distance) {
        return new ValueGradient(maxThrottle, Math.min(maxThrottle, 0.35), Math.min(1.0, Math.abs(distance)), 0.0);
    }

    protected double getRemainingDistance() {
        return Math.abs(distance - robot.getDrivetrain().getAverageDistance());
    }

    @Override
    protected void initialize() {
        super.initialize();
        initialHeading = robot.getSensors().gyro.getAngle();
        robot.getDrivetrain().resetEncoders();
    }

    @Override
    protected void execute() {
        super.execute();

        final double currentHeading = robot.getSensors().gyro.getAngle();
        final double angleDiff = currentHeading - initialHeading;

        robot.getDrivetrain().drive.curvatureDrive(Math.copySign(throttle.interpolate(getRemainingDistance()), distance),
                angleDiff * -GYRO_CORRECTION, false);
    }

    @Override
    protected void end() {
        super.end();
        robot.getDrivetrain().drive.stopMotor();
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut() || getRemainingDistance() <= 0.0;
    }

    @Override
    public synchronized boolean isInterruptible() {
        return true;
    }
}
