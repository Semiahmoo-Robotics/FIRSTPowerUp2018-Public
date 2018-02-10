package team6458.cmd;

import team6458.SemiRobot;
import team6458.subsystem.Ramp;
import team6458.util.Utils;

/**
 * Run the {@link Ramp} motors for a given time.
 */
public class RampMotorCommand extends RobotCommand {

    private final double speed;

    /**
     * Constructor.
     * @param robot The robot instance
     * @param speed The speed to run the motors at, between -1.0 and 1.0
     * @param timeout The positive time to run the motors for before stopping
     */
    protected RampMotorCommand(SemiRobot robot, double speed, double timeout) {
        super(robot);
        requires(robot.getRamp());
        setTimeout(timeout);
        setInterruptible(true);
        this.speed = Utils.clamp(speed, -1.0, 1.0);
    }

    @Override
    protected void execute() {
        super.execute();
        robot.getRamp().setSpeed(speed);
    }

    @Override
    protected void end() {
        super.end();
        robot.getRamp().stopMotors();
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }
}
