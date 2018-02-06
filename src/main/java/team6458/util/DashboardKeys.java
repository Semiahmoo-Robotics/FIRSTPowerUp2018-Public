package team6458.util;

/**
 * A utility class that holds string IDs for writing values to the SmartDashboard.
 */
public final class DashboardKeys {

    public static final String FMS_GAME_DATA = "Switch/Scale Positions";
    public static final String GYROSCOPE = "Gyroscope";
    public static final String LEFT_ENCODER = "Left Encoder";
    public static final String RIGHT_ENCODER = "Right Encoder";

    public static final String CHOOSER_AUTONOMOUS = "Autonomous Command";
    public static final String AUTO_LEFT = "Left Side";
    public static final String AUTO_CENTRE = "Centred";
    public static final String AUTO_RIGHT = "Right Side";

    public static final String CMD_RESET_ENCODERS = "Reset Encoders to Zero";
    public static final String CMD_GYRO_CALIBRATE = "Calibrate Gyroscope";

    /**
     * No instantiation.
     */
    private DashboardKeys() {
    }

}
