package team6458.cmd;

import edu.wpi.first.wpilibj.command.Command;
import team6458.SemiRobot;

/**
 * This is an abstract class that contains a reference to the main robot instance.
 */
public abstract class RobotCommand extends Command {

    protected final SemiRobot robot;

    protected RobotCommand(SemiRobot robot) {
        this.robot = robot;
    }
}
