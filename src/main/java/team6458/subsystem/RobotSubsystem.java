package team6458.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import team6458.SemiRobot;

/**
 * This is an abstract class extending {@link edu.wpi.first.wpilibj.command.Subsystem} that has a
 * {@link team6458.SemiRobot} instance passed in.
 *
 * All {@link RobotSubsystem}s should only be instantiated by the same robot instance.
 */
public abstract class RobotSubsystem extends Subsystem {

    /**
     * The immutable robot instance.
     */
    protected final SemiRobot robot;

    /**
     * The main constructor.
     */
    public RobotSubsystem(SemiRobot robot, String name) {
        super(name);
        this.robot = robot;
    }
}
