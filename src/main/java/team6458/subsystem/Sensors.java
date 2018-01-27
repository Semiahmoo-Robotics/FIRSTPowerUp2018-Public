package team6458.subsystem;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import team6458.SemiRobot;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The sensors subsystem. This has all the UNCOUPLED sensors for the robot: i.e. those that do not already
 * belong to a subsystem, like the motor encoders belonging to the {@link Drivetrain}.
 */
public final class Sensors extends RobotSubsystem {

    private static final Logger LOGGER = Logger.getLogger(Sensors.class.getName());

    public final BuiltInAccelerometer accelerometer;
    public final ADXRS450_Gyro gyro;

    public Sensors(SemiRobot robot) {
        super(robot, "Sensors");

        accelerometer = new BuiltInAccelerometer();
        LOGGER.log(Level.INFO, "Calibrating ADXRS250 gyroscope, expect a block");
        gyro = new ADXRS450_Gyro();
        LOGGER.log(Level.INFO, "Gyroscope calibrated.");
    }

    @Override
    protected void initDefaultCommand() {

    }
}
