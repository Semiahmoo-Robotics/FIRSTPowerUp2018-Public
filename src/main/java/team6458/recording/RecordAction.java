package team6458.recording;

import team6458.util.Utils;

public class RecordAction {

    public final double magnitude;
    public final double curve;
    public final double intake;

    public RecordAction(double throttle, double curve, double intake, boolean squaredInputs) {
        this.magnitude = squaredInputs ? Utils.squareKeepSign(throttle) : throttle;
        this.curve = squaredInputs ? Utils.squareKeepSign(curve) : curve;
        this.intake = intake;
    }

    public boolean isEmpty() {
        return magnitude == 0 && curve == 0 && intake == 0;
    }
}
