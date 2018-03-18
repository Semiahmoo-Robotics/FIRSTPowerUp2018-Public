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

    public CalibrationCommand(SemiRobot robot) {
        super();
        this.robot = robot;
    }

    @Override
    protected void initialize() {
        super.initialize();
        result = null;
        inUse = new CoastDistance();
    }

    @Override
    protected abstract void execute();

    @Override
    protected void end() {
        super.end();

        result = inUse;
        inUse = null;
    }

    @Override
    protected final void interrupted() {
        super.interrupted();
    }

    /**
     * @return The result (completed or not), or null if not started
     */
    public final CoastDistance getResult() {
        return result;
    }

}
