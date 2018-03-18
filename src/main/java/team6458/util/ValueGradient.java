package team6458.util;

/**
 * A simple value holder class that holds a floating point value gradient. It consists of two values, a minimum and maximum,
 * and a range at which it linearly interpolates between those two values.
 */
public final class ValueGradient {

    /**
     * The maximum value.
     */
    public final double maximum;
    /**
     * The minimum value.
     */
    public final double minimum;
    /**
     * The range in units where the {@link #maximum} transitions to the {@link #minimum} linearly.
     * This is based on the units remaining plus the {@link #rangeStart}.
     */
    public final double range;
    /**
     * The start of the {@link #range}. Must greater than or equal to zero.
     * The remaining value from zero to this value will always be at the {@link #minimum}.
     */
    public final double rangeStart;

    public ValueGradient(double maximum, double minimum, double range, double rangeStart) {
        if (maximum < 0 || maximum > 1 || minimum < 0 || minimum > maximum || rangeStart < 0) {
            throw new IllegalArgumentException("Invalid parameters passed for value gradient");
        }

        this.maximum = maximum;
        this.minimum = minimum;
        this.range = range;
        this.rangeStart = rangeStart;
    }

    /**
     * @param unitsRemaining The value remaining
     * @return A value between {@link #minimum} and {@link #maximum} based on the units remaining
     */
    public double interpolate(double unitsRemaining) {
        if (unitsRemaining < rangeStart)
            return minimum;
        if (unitsRemaining > rangeStart + range)
            return maximum;

        final double percentage = Utils.clamp((unitsRemaining - rangeStart) / range, 0.0, 1.0);
        return Utils.lerp(minimum,  maximum, percentage);
    }
}
