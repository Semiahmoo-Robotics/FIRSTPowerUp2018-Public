package team6458.recording;

import team6458.SemiRobot;

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

}
