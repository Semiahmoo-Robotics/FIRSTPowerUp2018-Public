package team6458.cmd;

import team6458.SemiRobot;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calibrates the gyroscope.
 */
public final class GyroCalibrationCommand extends RobotCommand {

    private static final Logger LOGGER = Logger.getLogger(GyroCalibrationCommand.class.getName());

    public GyroCalibrationCommand(SemiRobot robot) {
        super(robot);
        setTimeout(6.0);
    }

    @Override
    public synchronized void start() {
        super.start();
        LOGGER.log(Level.INFO, "Calibrating gyroscope...");
        robot.getSensors().gyro.calibrate();
        robot.getSensors().gyro.reset();
        LOGGER.log(Level.INFO, "Gyroscope calibrated.");
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
