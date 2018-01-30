package team6458.sensor;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;

/**
 * A simple utility class for holding information about the encoders on the robot.
 */
public final class EncoderPresets {

    /**
     * The encoder data for the <a href="https://www.andymark.com/encoder-p/am-3314a.htm">CIMcoder</a> encoder type.
     *
     * <p>The gearbox ratio for the motors these CIMcoders are mounted on is 10.71:1.
     * (The motor spins 10.71 times for every 1 rotation of the wheels.)
     *
     * <p>The wheels have a diameter of 15.24 cm (6"), which is a circumference of ~47.878 cm.
     * <b>Note that the values are in the instance are in <i>metres</i>.</b>
     */
    public static final EncoderPresets CIMCODER = new EncoderPresets(EncodingType.k2X, 20,
            (Math.PI * 0.1524) / 10.71);

    /**
     * The sample rate for the encoder.
     */
    public final EncodingType encodingType;
    /**
     * The number of pulses per encoder revolution.
     */
    public final int pulsesPerRevolution;
    /**
     * The distance in metres travelled for each encoder revolution.
     */
    public final double distanceMPerRevolution;
    /**
     * The distance in metres travelled for each pulse. (This is computed from the pulses per revolution and distance per pulse.)
     */
    public final double distanceMPerPulse;

    public EncoderPresets(EncodingType encodingType, int pulsesPerRevolution, double distanceMPerRevolution) {
        this.encodingType = encodingType;
        this.pulsesPerRevolution = pulsesPerRevolution;
        this.distanceMPerRevolution = distanceMPerRevolution;

        this.distanceMPerPulse = distanceMPerRevolution / pulsesPerRevolution;
    }

}
