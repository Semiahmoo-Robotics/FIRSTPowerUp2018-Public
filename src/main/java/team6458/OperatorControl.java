package team6458;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

/**
 * This is the human interface "subsystem", if you will. While not traditionally a proper subsystem,
 * it acts similarly, holding controllers and other data related to the human controlling the robot.
 *
 * <ul>
 * <li>LS - Drive</li>
 * <li>Hold A/B - Run</li>
 * <li>Hold X/Y - Lock heading</li>
 * <li>RT - Intake cube</li>
 * <li>LT - Expel cube</li>
 * </ul>
 */
public final class OperatorControl {

    /**
     * The coefficient used for the heading lock function.
     */
    private static final double GYRO_KP = 0.025;

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
     *
     * In non-op control mode, this will ensure that any human interfaces such as controllers do not respond unintentionally.
     */
    public void periodicUpdate() {
        if (!robot.isOperatorControl()) {
            if (lastOpControl) {
                // No human is allowed to control the robot at this time, stop motors and cancel anything necessary

                robot.getDrivetrain().drive.stopMotor();
                isHeadingLocked = false;
            }

            lastOpControl = false;
            return;
        }

        final double stickX = xboxController.getX(GenericHID.Hand.kLeft); // positive is clockwise
        final double stickY = -xboxController.getY(GenericHID.Hand.kLeft); // positive is forward
        final double angle = robot.getSensors().gyro.getAngle();
        final boolean buttonHeadingLock = xboxController.getXButton() || xboxController.getYButton();

        // Check heading lock (X/Y)
        if (buttonHeadingLock && !isHeadingLocked) {
            isHeadingLocked = true;
            targetLockedHeading = angle;
        } else if (!buttonHeadingLock && isHeadingLocked) {
            isHeadingLocked = false;
        }

        // Initial magnitude and curve using the controller
        double magnitude = stickY;
        double curve = stickX;

        // Correct for angle drift
        if (isHeadingLocked) {
            curve = Math.copySign(GYRO_KP * (angle - targetLockedHeading), -(magnitude == 0.0 ? 1 : magnitude));
        }

        // Drive the robot
        robot.getDrivetrain().drive.arcadeDrive(magnitude, curve);

        lastOpControl = robot.isOperatorControl();
    }

}
