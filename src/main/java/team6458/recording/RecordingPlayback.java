package team6458.recording;

public class RecordingPlayback {

    public final Recording recording;
    private int step = 0;
    private boolean isDone = false;

    public RecordingPlayback(Recording recording) {
        this.recording = recording.copy();
    }

    public boolean isDone() {
        return isDone;
    }

    public RecordAction step() {
        if (isDone)
            return null;

        RecordAction action = recording.actions.get(step++);

        if (step >= recording.actions.size()) {
            isDone = true;
        }

        return action;
    }
}
