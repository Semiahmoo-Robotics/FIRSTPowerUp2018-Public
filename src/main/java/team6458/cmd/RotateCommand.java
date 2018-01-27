package team6458.cmd;

import team6458.SemiRobot;
import team6458.util.Utils;

/**
 * A command that attempts to rotate the robot left or right in a given direction.
 */
public class RotateCommand extends RobotCommand {

    public static final double DEFAULT_SPEED = 0.4;
    public static final double ANGLE_TOLERANCE = 5.0;

    public final double headingChange;
    public final double speed;

    private double originalOrientation;
    private double targetOrientation;

    public RotateCommand(SemiRobot robot, double headingChange, double speed) {
        super(robot);
        requires(robot.getDrivetrain());
        setTimeout(10.0);

        this.headingChange = headingChange;
        this.speed = speed;
    }

    public RotateCommand(SemiRobot robot, double headingChange) {
        this(robot, headingChange, DEFAULT_SPEED);
    }

    @Override
    public synchronized void start() {
        super.start();
        originalOrientation = robot.getSensors().gyro.getAngle();
        targetOrientation = originalOrientation + headingChange;
    }

    @Override
    protected void execute() {
        super.execute();
        robot.getDrivetrain().drive.curvatureDrive(0.0, Math.copySign(speed, targetOrientation - originalOrientation), true);
    }

    @Override
    protected void end() {
        super.end();
        robot.getDrivetrain().drive.curvatureDrive(0.0, 0.0, true);
    }

    @Override
    public synchronized boolean isInterruptible() {
        return true;
    }

    @Override
    protected boolean isFinished() {
        return Utils.isEqual(originalOrientation, targetOrientation, ANGLE_TOLERANCE);
    }
}
