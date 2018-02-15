package team6458;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team6458.cmd.DriveStraightCommand;
import team6458.util.Utils;

import static team6458.util.DashboardKeys.INTAKE_THROTTLE;

/**
 * This is the human interface controller. While not traditionally a proper subsystem,
 * it acts similarly, holding controllers and other data related to the human controlling the robot.
 * <p>
 * <ul>
 * <li>LS - Drive</li>
 * <li>Hold A/B - Run</li>
 * <li>Hold X/Y - Lock heading</li>
 * <li>RT - Intake/launch cube (towards back)</li>
 * <li>LT - Reverse cube (towards front)</li>
 * </ul>
 */
public final class OperatorControl {

    /**
     * The coefficient used for the heading lock function. For now, uses the same value for the drive straight command.
     *
     * @see DriveStraightCommand#GYRO_CORRECTION
     */
    private static final double GYRO_KP = DriveStraightCommand.GYRO_CORRECTION;
    /**
     * The maximum absolute throttle value when run is not held.
     */
    private static final double MAX_NOT_RUN_HELD = 0.75;

    private final SemiRobot robot;
    private final XboxController xboxController = new XboxController(0);

    // State tracking
    private boolean lastOpControl;
    /**
     * Used for heading lock/drive straight.
     */
    private double targetLockedHeading = 0.0;
    /**
     * True if the heading is locked, false otherwise.
     */
    private boolean isHeadingLocked = false;

    public OperatorControl(SemiRobot robot) {
        this.robot = robot;
        lastOpControl = robot.isOperatorControl();
    }

    /**
     * Call periodically to have the operator control take effect.
     * <p>
     * In non-op control mode, this will ensure that any human interfaces such as controllers do not respond
     * unintentionally.
     */
    public void periodicUpdate() {
        if (!robot.isOperatorControl()) {
            if (lastOpControl) {
                // No human is allowed to control the robot at this time, stop motors and cancel anything necessary

                robot.getDrivetrain().drive.stopMotor();
                robot.getRamp().stopMotors();
                isHeadingLocked = false;
            }

            SmartDashboard.putNumber(INTAKE_THROTTLE, 0.0);

            lastOpControl = false;
            return;
        }

        final double stickX = xboxController.getX(Hand.kLeft); // positive is clockwise
        final double stickY = -xboxController.getY(Hand.kLeft); // positive is forward
        final double angle = robot.getSensors().gyro.getAngle();
        final boolean isHeadingLockHeld = xboxController.getXButton() || xboxController.getYButton();
        final boolean isRunHeld = xboxController.getBButton() || xboxController.getAButton();

        // Check heading lock (X/Y)
        if (isHeadingLockHeld && !isHeadingLocked) {
            isHeadingLocked = true;
            targetLockedHeading = angle;
        } else if (!isHeadingLockHeld && isHeadingLocked) {
            isHeadingLocked = false;
        }

        // Initial magnitude and curve using the controller
        double magnitude = (isRunHeld ? stickY : (stickY * MAX_NOT_RUN_HELD));
        double curve = (isRunHeld ? stickX : (stickX * MAX_NOT_RUN_HELD));
        double intakeThrottle = Utils
                .clamp((-xboxController.getTriggerAxis(Hand.kLeft) + xboxController.getTriggerAxis(Hand.kRight)), -1.0,
                        1.0);

        // Correct for angle drift
        if (isHeadingLocked) {
            curve = -GYRO_KP * (angle - targetLockedHeading);
        }

        // Drive the robot
        robot.getDrivetrain().drive.arcadeDrive(magnitude, curve);

        // Drive intake/launcher motors
        robot.getRamp().setSpeed(intakeThrottle);
        SmartDashboard.putNumber(INTAKE_THROTTLE, intakeThrottle);

//        if (SmartDashboard.getBoolean(PROXIMITY_RUMBLE, false)) {
//            double distance =
//                    1 - (Utils.clamp(robot.getSensors().rangefinder.getDistance(AnalogRangefinder.Units.INCHES), 10.0,
//                            26.0) - 10.0) / 16;
//
//            xboxController.setRumble(GenericHID.RumbleType.kRightRumble, distance * 0.6);
//        } else {
//            xboxController.setRumble(GenericHID.RumbleType.kRightRumble, 0);
//        }

        lastOpControl = true;
    }

}
