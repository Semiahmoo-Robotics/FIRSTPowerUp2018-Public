package team6458.subsystem;

import team6458.SemiRobot;

/**
 * The sensors subsystem. This has all the UNCOUPLED sensors for the robot: i.e. those that do not already
 * belong to a subsystem, like the motor encoders belonging to the {@link Drivetrain}.
 */
public final class Sensors extends RobotSubsystem {

    public Sensors(SemiRobot robot) {
        super(robot, "Sensors");
    }

    @Override
    protected void initDefaultCommand() {

    }
}
