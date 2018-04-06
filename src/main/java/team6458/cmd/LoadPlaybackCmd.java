package team6458.cmd;

import edu.wpi.first.wpilibj.Preferences;
import team6458.SemiRobot;
import team6458.recording.RecordAction;
import team6458.recording.Recording;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.logging.Logger;

public class LoadPlaybackCmd extends RobotCommand {
    private static final Logger LOGGER = Logger.getLogger(LoadPlaybackCmd.class.getName());

    public LoadPlaybackCmd(SemiRobot robot) {
        super(robot);
    }

    @Override
    protected void execute() {
        super.execute();

        String data = Preferences.getInstance().getString("Playback", "");

        if (data != null && !data.isEmpty()) {
            try {
                ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(data));
                int number = buffer.getInt();

                Recording recording = new Recording(robot);

                for (int i = 0; i < number; i++) {
                    recording.add(new RecordAction(buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), false));
                }

                robot.lastRecording = recording;

                LOGGER.info("Loaded recording, has " + number + " actions");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.warning("Failed to load recording");
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
