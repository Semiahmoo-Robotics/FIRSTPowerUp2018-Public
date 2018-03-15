package team6458.cmd;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team6458.SemiRobot;

public class AutoCompletionCommand extends CommandGroup {

    public AutoCompletionCommand(final SemiRobot robot, final boolean rightSide) {
        super();

        final double throttle = 0.75;
        addSequential(new DriveStraightCommand(robot, 0.75, throttle));
        addSequential(new RotateCommand(robot, 45.0 * (rightSide ? 1 : -1)));
        addSequential(new DriveStraightCommand(robot, -1.1, throttle));
        addSequential(new RotateCommand(robot, -45.0 * (rightSide ? 1 : -1)));
        addSequential(new DriveStraightCommand(robot, -1.75, throttle * 0.75));
        addSequential(new RotateCommand(robot, 180.0 * (rightSide ? 1 : -1)));
    }
}
