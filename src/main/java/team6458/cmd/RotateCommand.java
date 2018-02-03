package team6458.cmd;

import team6458.SemiRobot;
import team6458.util.Utils;

/**
 * A command that attempts to rotate the robot left or right in a given direction.
 */
public class RotateCommand extends RobotCommand {

    /**
     * The default throttle.
     */
    public static final double DEFAULT_SPEED = 0.35;
    /**
     * The default angle tolerance in degrees at which the target angle and real angle have to match by.
     */
    public static final double ANGLE_TOLERANCE = 4.0;

    public final double headingChange;
    public final double throttle;

    private double originalOrientation;
    private double targetOrientation;

    /**
     * Constructor.
     * @param robot The robot instance
     * @param headingChange The amount to change the heading by, positive is clockwise
     * @param throttle The throttle as a percentage, positive values only
     */
    public RotateCommand(SemiRobot robot, double headingChange, double throttle) {
        super(robot);
        requires(robot.getDrivetrain());
        setTimeout(5.0);

        this.headingChange = headingChange;
        this.throttle =  Math.abs(Math.abs(throttle) > 1.0 ? 1.0 : throttle);
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
        robot.getDrivetrain().drive.curvatureDrive(0.0, Math.copySign(throttle,
                targetOrientation - originalOrientation), true);
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

    /**
     * @return True if the current heading has overshot the target, false otherwise
     */
    protected final boolean hasOvershot() {
        double currentAngle = robot.getSensors().gyro.getAngle();
        return (headingChange >= 0.0 ? currentAngle > targetOrientation : currentAngle < targetOrientation);
    }

    @Override
    protected boolean isFinished() {
        return Utils.isEqual(robot.getSensors().gyro.getAngle(),
                targetOrientation, ANGLE_TOLERANCE) || hasOvershot() || isTimedOut();
    }
}
