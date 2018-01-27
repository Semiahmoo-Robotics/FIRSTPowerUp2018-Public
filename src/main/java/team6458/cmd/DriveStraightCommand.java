package team6458.cmd;

import team6458.SemiRobot;

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
    private static final double kP = 0.025;

    public final double distance;
    public final double speed;

    private double initialHeading;

    public DriveStraightCommand(SemiRobot robot, double distance, double speed) {
        super(robot);
        requires(robot.getDrivetrain());
        this.speed = speed;
        this.distance = distance;
    }

    @Override
    public synchronized void start() {
        super.start();
        initialHeading = robot.getSensors().gyro.getAngle();
    }

    @Override
    protected void execute() {
        super.execute();

        final double currentHeading = robot.getSensors().gyro.getAngle();
        final double angleDiff = currentHeading - initialHeading;

        robot.getDrivetrain().drive.curvatureDrive(Math.copySign(speed, distance), -angleDiff * kP, false);
    }

    @Override
    protected void end() {
        super.end();
        robot.getDrivetrain().drive.stopMotor();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public synchronized boolean isInterruptible() {
        return true;
    }
}
