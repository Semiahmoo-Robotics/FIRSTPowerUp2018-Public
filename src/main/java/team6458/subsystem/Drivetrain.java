package team6458.subsystem;

import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import team6458.SemiRobot;
import team6458.sensor.EncoderPresets;
import team6458.util.Ports;
import team6458.util.Registrar;

import static team6458.sensor.EncoderPresets.CIMCODER;
import static team6458.util.Ports.DIO.*;

/**
 * The drivetrain subsystem. This subsystem controls all vehicular aspects of the robot,
 * specifically the motors that make it move forwards/backwards/turn. It also has any sensors attached.
 */
public final class Drivetrain extends RobotSubsystem {

    public final Encoder leftEncoder = createEncoder(CIMCODER,
            Registrar.digitalInput(LEFT_ENCODER_CHANNEL_A), Registrar.digitalInput(LEFT_ENCODER_CHANNEL_B), false);
    public final Encoder rightEncoder = createEncoder(CIMCODER,
            Registrar.digitalInput(RIGHT_ENCODER_CHANNEL_A), Registrar.digitalInput(RIGHT_ENCODER_CHANNEL_B), false);

    private final Spark leftMotor = Registrar.spark(Ports.PWM.LEFT_MOTOR);
    private final Spark rightMotor = Registrar.spark(Ports.PWM.RIGHT_MOTOR);
    public final DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);

    public Drivetrain(SemiRobot robot) {
        super(robot, "Drivetrain");
    }

    @Override
    protected void initDefaultCommand() {
    }

//    /**
//     * @return A stream consisting of the encoder objects
//     */
//    public Stream<Encoder> streamEncoders() {
//        return Stream.of(leftEncoder, rightEncoder);
//    }

    /**
     * Resets all encoders to zero.
     */
    public void resetEncoders() {
//        streamEncoders().forEach(Encoder::reset);
        leftEncoder.reset();
        rightEncoder.reset();
    }

    /**
     * @return The average distance per second of the encoders in m/s
     */
    public double getAverageRate() {
//        return streamEncoders().mapToDouble(Encoder::getRate).average().orElse(0.0);
        return (leftEncoder.getRate() + rightEncoder.getRate()) / 2.0;
    }

    /**
     * @return The average recorded distance of the encoders in metres
     */
    public double getAverageDistance() {
//        return streamEncoders().mapToDouble(Encoder::getDistance).average().orElse(0.0);
        return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0;
    }

    /**
     * @return True if any encoders are stopped, false if all are still moving
     */
    public boolean areEncodersStopped() {
//        return streamEncoders().anyMatch(Encoder::getStopped);
        return leftEncoder.getStopped() || rightEncoder.getStopped();
    }

    private Encoder createEncoder(EncoderPresets preset, DigitalSource channelA, DigitalSource channelB, boolean reverse) {
        final Encoder e = new Encoder(channelA, channelB, reverse, preset.encodingType);

        e.setDistancePerPulse(preset.distanceMPerPulse);
        e.setSamplesToAverage(7);
        e.setMaxPeriod(0.1); // 0.1 seconds
        e.setMinRate(0.03); // 3 cm

        return e;
    }
}
