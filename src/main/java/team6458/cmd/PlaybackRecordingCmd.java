package team6458.cmd;

import team6458.SemiRobot;
import team6458.recording.RecordingPlayback;

public class PlaybackRecordingCmd extends RobotCommand {
    public PlaybackRecordingCmd(SemiRobot robot) {
        super(robot);
    }

    @Override
    protected void execute() {
        super.execute();
        if (robot.lastRecording != null) {
            robot.getOperatorControl().recording = new RecordingPlayback(robot.lastRecording);
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
