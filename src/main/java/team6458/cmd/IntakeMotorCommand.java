package team6458.cmd;

import team6458.SemiRobot;

/**
 * Run the
 */
public class IntakeMotorCommand extends RobotCommand {
    protected IntakeMotorCommand(SemiRobot robot) {
        super(robot);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }
}
