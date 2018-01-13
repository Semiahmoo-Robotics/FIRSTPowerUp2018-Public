package team6458;

import edu.wpi.first.wpilibj.XboxController;

/**
 * This is the human interface "subsystem", if you will. While not traditionally a proper subsystem,
 * it acts similarly, holding controllers and other data related to the human controlling the robot.
 */
public final class OperatorControl {

    private final SemiRobot robot;
    private final XboxController xboxController = new XboxController(0); // TODO dynamic port?

    public OperatorControl(SemiRobot robot) {
        this.robot = robot;
    }

    /**
     * Call periodically to have the operator control take effect.
     *
     * In non-op control mode, this will ensure that any human interfaces such as controllers do not respond unintentionally.
     */
    public void periodicUpdate() {
        if (!robot.isOperatorControl()) {
            // No human is allowed to control the robot at this time, don't execute future code.
            // Old drive values persisting from the controller will be blocked by the motor safety watchdog anyway
            return;
        }

        // Drive the robot
        robot.getDrivetrain().drive.arcadeDrive(xboxController.getY(), xboxController.getX());
    }

}
