package team6458.subsystem;

import edu.wpi.first.wpilibj.Spark;
import team6458.SemiRobot;
import team6458.util.Allocator;
import team6458.util.Ports.PWM;
import team6458.util.Utils;

import java.util.stream.Stream;

/**
 * The launcher/intake system. Holds motor controllers.
 */
public final class Ramp extends RobotSubsystem {

    public final Spark intakeLeft = Allocator.spark(PWM.LEFT_INTAKE);
    public final Spark intakeRight = Allocator.spark(PWM.RIGHT_INTAKE);
    public final Spark rampLeft = Allocator.spark(PWM.LEFT_RAMP);
    public final Spark rampRight = Allocator.spark(PWM.RIGHT_RAMP);

    /**
     * The main constructor.
     *
     * @param robot The robot instance
     */
    public Ramp(SemiRobot robot) {
        super(robot, "Launcher");

        intakeRight.setInverted(true);
        rampLeft.setInverted(true);
    }

    @Override
    protected void initDefaultCommand() {

    }

    public Stream<Spark> stream() {
        return Stream.of(intakeLeft, intakeRight, rampLeft, rampRight);
//        return Stream.of(rampLeft, rampRight);
    }

    /**
     * Stop all ramp motors.
     */
    public void stopMotors() {
        stream().forEach(Spark::stopMotor);
    }

    /**
     * Set all ramp motors to this speed.
     *
     * @param speed The throttle between -1.0 and 1.0 (will be clamped)
     */
    public void setSpeed(double speed) {
        final double clamped = Utils.clamp(speed, -1.0, 1.0);
        stream().forEach(it -> it.set(clamped));
    }
}
