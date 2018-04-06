package team6458.cmd;

import edu.wpi.first.wpilibj.Preferences;
import team6458.SemiRobot;
import team6458.recording.Recording;

import java.util.Base64;
import java.util.logging.Logger;

public class SavePlaybackCmd extends RobotCommand {

    private static final Logger LOGGER = Logger.getLogger(SavePlaybackCmd.class.getName());

    public SavePlaybackCmd(SemiRobot robot) {
        super(robot);
    }

    @Override
    protected void execute() {
        super.execute();

        Recording recording = robot.lastRecording;

        if (recording != null) {
            Preferences.getInstance().putString("Playback", Base64.getEncoder().encodeToString(recording.toByteArray()));
            LOGGER.info("Saved playback");
        }

    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
