package team6458.util;

/**
 * Various port assignments on the roboRIO. This is a constant holder.
 */
public final class Ports {

    private Ports() {
    }

    public static final class PWM {
        public static final int LEFT_MOTOR = 0;
        public static final int RIGHT_MOTOR = 1;

        private PWM() {
        }
    }

    public static final class DIO {
        public static final int LEFT_ENCODER_CHANNEL_A = 0;
        public static final int LEFT_ENCODER_CHANNEL_B = 1;
        public static final int RIGHT_ENCODER_CHANNEL_A = 2;
        public static final int RIGHT_ENCODER_CHANNEL_B = 3;

        private DIO() {
        }
    }
}
