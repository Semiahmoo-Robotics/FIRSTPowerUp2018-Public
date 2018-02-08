package team6458.cmd;

import team6458.SemiRobot;
import team6458.util.Utils;

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
     * The throttle to go at, from 0.0 to 1.0.
     */
    public final double throttle;

    private double initialHeading;

    /**
     * Constructor.
     * @param distance Distance in metres, may be negative
     * @param throttle Throttle between 0.0 and 1.0 (positive only)
     */
    public DriveStraightCommand(SemiRobot robot, double distance, double throttle) {
        super(robot);
        requires(robot.getDrivetrain());

        this.throttle = Utils.clamp(Math.abs(throttle), 0.0, 1.0);
        this.distance = distance;
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

        robot.getDrivetrain().drive.curvatureDrive(Math.copySign(throttle, distance),
                angleDiff * -GYRO_CORRECTION * Math.signum(distance), false);
    }

    @Override
    protected void end() {
        super.end();
        robot.getDrivetrain().drive.stopMotor();
    }

    @Override
    protected boolean isFinished() {
        return distance >= 0 ? robot.getDrivetrain().getAverageDistance() >= distance : robot.getDrivetrain().getAverageDistance() <= distance;
    }

    @Override
    public synchronized boolean isInterruptible() {
        return true;
    }
}
