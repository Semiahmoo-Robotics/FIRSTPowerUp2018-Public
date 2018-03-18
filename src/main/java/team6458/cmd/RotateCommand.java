package team6458.cmd;

import team6458.SemiRobot;
import team6458.util.Utils;
import team6458.util.ValueGradient;

/**
 * A command that rotates the robot left or right to face a new given relative heading.
 */
public class RotateCommand extends RobotCommand {

    /**
     * The default angle tolerance in degrees at which the target angle and real angle have to match by.
     */
    public static final double ANGLE_TOLERANCE = 4.0;
    /**
     * The default speed gradient to use.
     * <p>
     * Max of 0.45, min of 0.325, range of 20 deg starting at 10 deg.
     */
    public static final ValueGradient DEFAULT_GRADIENT = new ValueGradient(0.45, 0.325, 20.0, 10.0);

    public final double headingChange;
    public final ValueGradient speedGradient;

    private double originalOrientation;
    private double targetOrientation;

    /**
     * Constructor.
     *
     * @param robot         The robot instance
     * @param headingChange The amount to change the heading by, positive is clockwise
     * @param gradient      The speed gradient to use
     */
    public RotateCommand(SemiRobot robot, double headingChange, ValueGradient gradient) {
        super(robot);
        requires(robot.getDrivetrain());
        setTimeout(2.5);

        this.headingChange = headingChange;
        this.speedGradient = gradient;
    }

    public RotateCommand(SemiRobot robot, double headingChange) {
        this(robot, headingChange, DEFAULT_GRADIENT);
    }

    @Override
    protected void initialize() {
        super.initialize();
        originalOrientation = robot.getSensors().gyro.getAngle();
        targetOrientation = originalOrientation + headingChange;
    }

    @Override
    protected void execute() {
        super.execute();
        robot.getDrivetrain().drive.curvatureDrive(0.0,
                Math.copySign(getCurrentThrottle(), targetOrientation - originalOrientation), true);
    }

    @Override
    protected void end() {
        super.end();
        robot.getDrivetrain().drive.stopMotor();
    }

    @Override
    public synchronized boolean isInterruptible() {
        return true;
    }

    /**
     * @return True if the current heading has overshot the target, false otherwise
     */
    public final boolean hasOvershot() {
        final double currentAngle = robot.getSensors().gyro.getAngle();
        final double remainingAngle = Math.abs(currentAngle - targetOrientation);
        final double predictedCoast = robot.getRotateCoastDist().getDistance(Math.abs(robot.getSensors().gyro.getRate()));
        return (headingChange >= 0.0 ?
                currentAngle > targetOrientation + ANGLE_TOLERANCE :
                currentAngle < targetOrientation - ANGLE_TOLERANCE);
    }

    /**
     * @return The current throttle to use based on the selected speed gradient
     */
    public final double getCurrentThrottle() {
        if (hasOvershot())
            return 0.0;

        final double currentAngle = robot.getSensors().gyro.getAngle();
        final double remainingAngle = Math.abs(currentAngle - targetOrientation);

        return speedGradient.interpolate(remainingAngle);
    }

    @Override
    protected boolean isFinished() {
        return Utils.isEqual(robot.getSensors().gyro.getAngle(),
                targetOrientation, ANGLE_TOLERANCE) || hasOvershot() || isTimedOut();
    }

}
