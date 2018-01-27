package team6458.util;

/**
 * A generic utility class with a collection of public static methods.
 */
public final class Utils {

    /**
     * No instantiation.
     */
    private Utils() {
    }

    /**
     * Determines if two doubles are equal to each other by the given tolerance.
     * @param a The first value
     * @param b The second value
     * @param tolerance The amount the two values have to be within to be considered equal
     * @return True if equal within the tolerance
     */
    public static boolean isEqual(double a, double b, double tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

}
