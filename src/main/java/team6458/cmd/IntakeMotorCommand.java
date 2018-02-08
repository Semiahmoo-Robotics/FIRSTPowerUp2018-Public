package team6458.cmd;

import team6458.SemiRobot;
import team6458.util.Utils;

/**
 * Run the {@link team6458.subsystem.Intake} motors for a given time.
 */
public class IntakeMotorCommand extends RobotCommand {

    private final double speed;

    /**
     * Constructor.
     * @param robot The robot instance
     * @param speed The speed to run the motors at, between -1.0 and 1.0
     * @param timeout The positive time to run the motors for before stopping
     */
    protected IntakeMotorCommand(SemiRobot robot, double speed, double timeout) {
        super(robot);
        requires(robot.getIntake());
        setTimeout(timeout);
        setInterruptible(true);
        this.speed = Utils.clamp(speed, -1.0, 1.0);
    }

    @Override
    protected void execute() {
        super.execute();
        robot.getIntake().setSpeed(speed);
    }

    @Override
    protected void end() {
        super.end();
        robot.getIntake().stopMotors();
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }
}
