package team6458.cmd.calibration;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team6458.SemiRobot;
import team6458.feedback.CoastDistance;

/**
 * Creates a {@link CoastDistance} object based on calibrating certain values.
 */
public abstract class CalibrationCommand extends CommandGroup {

    public final SemiRobot robot;
    protected CoastDistance result = null;
    protected CoastDistance inUse = null;
    private boolean wasSuccessful = false;

    public CalibrationCommand(SemiRobot robot) {
        super();
        this.robot = robot;
    }

    @Override
    protected void initialize() {
        super.initialize();
        result = null;
        wasSuccessful = false;
        inUse = new CoastDistance();
    }

    @Override
    protected void end() {
        finishUp(true);
    }

    @Override
    protected final void interrupted() {
        finishUp(false);
    }

    private void finishUp(boolean success) {
        result = inUse;
        inUse = null;
        wasSuccessful = success;
    }

    /**
     * @return The result (completed or not), or null if not started
     */
    public final CoastDistance getResult() {
        return result;
    }

    /**
     * @return True if the result was successful (not interrupted or cancelled), false otherwise (or if null)
     */
    public boolean wasSuccessful() {
        return getResult() != null && wasSuccessful;
    }
}
