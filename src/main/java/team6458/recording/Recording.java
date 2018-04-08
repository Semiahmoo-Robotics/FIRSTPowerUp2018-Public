package team6458.recording;

import team6458.SemiRobot;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Recording {

    public final List<RecordAction> actions;
    public final SemiRobot robot;

    public Recording(SemiRobot robot, List<RecordAction> list) {
        this.robot = robot;
        this.actions = list;
    }

    public Recording(SemiRobot robot) {
        this(robot, new ArrayList<>());
    }

    public Recording copy() {
        return new Recording(robot, new ArrayList<>(this.actions));
    }

    public double getDuration() {
        return robot.getPeriod() * actions.size();
    }

    public void add(RecordAction action) {
        if (action == null)
            return;
        actions.add(action);
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(actions.size() * 24 + 4);
        buffer.putInt(actions.size());
        actions.forEach(action -> buffer.putDouble(action.curve).putDouble(action.magnitude).putDouble(action.intake));
        return buffer.array();
    }

}
