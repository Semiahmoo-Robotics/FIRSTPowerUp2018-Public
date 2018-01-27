package team6458.subsystem;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import team6458.SemiRobot;

/**
 * The sensors subsystem. This has all the UNCOUPLED sensors for the robot: i.e. those that do not already
 * belong to a subsystem, like the motor encoders belonging to the {@link Drivetrain}.
 */
public final class Sensors extends RobotSubsystem {

    public final BuiltInAccelerometer accelerometer;
    public final ADXRS450_Gyro gyro;

    public Sensors(SemiRobot robot) {
        super(robot, "Sensors");

        accelerometer = new BuiltInAccelerometer();
        gyro = new ADXRS450_Gyro();
    }

    @Override
    protected void initDefaultCommand() {

    }
}
