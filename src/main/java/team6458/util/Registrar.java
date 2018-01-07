package team6458.util;

import edu.wpi.first.wpilibj.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * "Borrowed" from https://github.com/Open-RIO/ToastAPI/<p>
 * <p>
 * The Registrar is a simple class designed to get rid of Allocation exceptions in code, where if multiple
 * instances of a Motor Controller, DIO, or Analog interface are created with the same port, WPILib throws
 * a big fat error in your face. To solve this, the Registrar keeps one instance of each when you request it,
 * allowing for multiple accesses, but not multiple constructions.
 *
 * @param <ID>   The type to use for the Identifier. In most cases, this is an integer.
 * @param <Type> The type to store in this registrar. This is the superclass
 */
public class Registrar<ID, Type> {

    public static volatile Registrar<Integer, PWM> pwmRegistrar = new Registrar<>();
    public static volatile Registrar<Integer, SpeedController> canRegistrar = new Registrar<>();
    public static volatile Registrar<Integer, Sendable> dioRegistrar = new Registrar<>();

    // -- STATICS -- //
    public static volatile Registrar<Integer, Relay> relayRegistrar = new Registrar<>();
    public static volatile Registrar<Integer, AnalogInput> aiRegistrar = new Registrar<>();
    public static volatile Registrar<Integer, AnalogOutput> aoRegistrar = new Registrar<>();
    public static volatile Registrar<Integer, Compressor> compressorRegistrar = new Registrar<>();
    public static volatile Registrar<SolenoidID, SolenoidBase> solenoidRegistrar = new Registrar<>();
    private volatile HashMap<ID, Type> registered = new HashMap<>();

    /**
     * Get a DigitalOutput instance from the Registrar
     *
     * @param port the DIO port to use
     */
    public static DigitalOutput digitalOutput(int port) {
        return dioRegistrar.fetch(port, DigitalOutput.class, () -> new DigitalOutput(port));
    }

    /**
     * Get a DigitalInput instance from the Registrar
     *
     * @param port the DIO port to use
     */
    public static DigitalInput digitalInput(int port) {
        return dioRegistrar.fetch(port, DigitalInput.class, () -> new DigitalInput(port));
    }

    /**
     * Get an AnalogOutput instance from the Registrar
     *
     * @param port the AO port to use
     */
    public static AnalogOutput analogOutput(int port) {
        return aoRegistrar.fetch(port, AnalogOutput.class, () -> new AnalogOutput(port));
    }

    /**
     * Get an AnalogInput instance from the Registrar
     *
     * @param port the AI port to use
     */
    public static AnalogInput analogInput(int port) {
        return aiRegistrar.fetch(port, AnalogInput.class, () -> new AnalogInput(port));
    }

    /**
     * Get a Spike style Relay instance from the Registrar
     *
     * @param relayPort the Relay port to use
     */
    public static Relay relay(int relayPort) {
        return relayRegistrar.fetch(relayPort, Relay.class, () -> new Relay(relayPort));
    }

    /**
     * Get a Talon [SR] instance from the Registrar
     *
     * @param pwmPort the PWM Port to use
     */
    public static Talon talon(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, Talon.class, () -> new Talon(pwmPort));
    }

    /**
     * Get a Talon SRX [PWM] instance from the Registrar
     *
     * @param pwmPort the PWM Port to use
     */
    public static PWMTalonSRX talonSRX(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, PWMTalonSRX.class, () -> new PWMTalonSRX(pwmPort));
    }

    // -- Motor Controllers -- //

    /**
     * Get a Jaguar [PWM] instance from the Registrar
     *
     * @param pwmPort the PWM port to use
     */
    public static Jaguar jaguar(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, Jaguar.class, () -> new Jaguar(pwmPort));
    }

    /**
     * Get a Victor instance from the Registrar
     *
     * @param pwmPort the PWM port to use
     */
    public static Victor victor(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, Victor.class, () -> new Victor(pwmPort));
    }

    /**
     * Get a Victor SP instance from the Registrar
     *
     * @param pwmPort the PWM port to use
     */
    public static VictorSP victorSP(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, VictorSP.class, () -> new VictorSP(pwmPort));
    }

    /**
     * Get a Spark instance from the Registrar
     *
     * @param pwmPort the PWM port to use
     */
    public static Spark spark(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, Spark.class, () -> new Spark(pwmPort));
    }

    /**
     * Get an SD540 instance from the Registrar
     *
     * @param pwmPort the PWM port to use
     */
    public static SD540 sd540(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, SD540.class, () -> new SD540(pwmPort));
    }

    /**
     * Get a Servo instance from the Registrar
     *
     * @param pwmPort the PWM port to use
     */
    public static Servo servo(int pwmPort) {
        return pwmRegistrar.fetch(pwmPort, Servo.class, () -> new Servo(pwmPort));
    }

    /**
     * Get a Compressor instance from the Registrar
     *
     * @param pcmID the PCM CAN Device ID to use
     */
    public static Compressor compressor(int pcmID) {
        return compressorRegistrar.fetch(pcmID, Compressor.class, () -> new Compressor(pcmID));
    }

    /**
     * Get a Solenoid instance from the Registrar
     *
     * @param pcmID           the PCM CAN Device ID to use
     * @param solenoidChannel the channel on the PWM to use
     */
    public static Solenoid solenoid(int pcmID, int solenoidChannel) {
        return solenoidRegistrar.fetch(new SolenoidID(pcmID, solenoidChannel), Solenoid.class, () -> new Solenoid(pcmID, solenoidChannel));
    }

    // Commented out due to CANTalon not being used
//    /**
//     * Get a Talon SRX [CAN] instance from the Registrar
//     * @param canID the CAN Device ID to use
//     */
//    public static CANTalon canTalon(int canID) {
//        return canRegistrar.fetch(canID, CANTalon.class, () -> new CANTalon(canID) );
//    }

    // -- Pneumatics -- //

    /**
     * Get a Solenoid instance from the Registrar
     *
     * @param solenoidChannel the channel on the PWM to use
     */
    public static Solenoid solenoid(int solenoidChannel) {
        return solenoid(0, solenoidChannel);
    }

    /**
     * Get a DoubleSolenoid instance from the Registrar
     *
     * @param pcmID              the PCM CAN Device ID to use
     * @param solenoidFwdChannel the forward channel on the PWM to use
     * @param solenoidRevChannel the reverse channel on the PWM to use
     */
    public static DoubleSolenoid doubleSolenoid(int pcmID, int solenoidFwdChannel, int solenoidRevChannel) {
        return solenoidRegistrar
                .fetch(new SolenoidID(pcmID, solenoidFwdChannel, solenoidRevChannel), DoubleSolenoid.class, () -> new DoubleSolenoid(pcmID, solenoidFwdChannel, solenoidRevChannel));
    }

    /**
     * Get a DoubleSolenoid instance from the Registrar
     *
     * @param solenoidFwdChannel the forward channel on the PWM to use
     * @param solenoidRevChannel the reverse channel on the PWM to use
     */
    public static DoubleSolenoid doubleSolenoid(int solenoidFwdChannel, int solenoidRevChannel) {
        return doubleSolenoid(0, solenoidFwdChannel, solenoidRevChannel);
    }

    /**
     * Fetch an object from the Registrar, or create it if it doesn't exist yet.
     *
     * @param id      The ID to store the object under
     * @param clazz   The Class (Type) of the object you are inserting (subclass of Type)
     * @param creator The lambda function that will construct a new instance of the object if it doesn't
     *                already exist in the registrar
     * @param <T>     The type to insert / fetch from the registrar. This is a subclass of the Registrar's Type
     * @return The object in the registrar / the new object created.
     */
    @SuppressWarnings("unchecked")
    public synchronized <T extends Type> T fetch(ID id, Class<T> clazz, Supplier<T> creator) {
        Type in_register = registered.get(id);

        if (in_register == null) {
            for (ID key : registered.keySet()) {
                if (key.equals(id)) {
                    in_register = registered.get(key);
                }
            }
        }

        if (in_register == null) {
            T instance = creator.get();
            registered.put(id, instance);
            return instance;
        } else {
            if (clazz.isAssignableFrom(in_register.getClass()))
                return (T) in_register;
            else
                throw new IllegalStateException("An object already exists in the Registrar with the given ID, " +
                        "but is of a different type! Expected: " + clazz.getName() + ", but got: " +
                        in_register.getClass().getName());
        }
    }

    /**
     * Return the stream of the underlying Registrar hashmap. Use this to directly manipulate the registrar
     */
    public synchronized Stream<Map.Entry<ID, Type>> stream() {
        return registered.entrySet().stream();
    }

    protected static class SolenoidID {

        public int pcmID;
        public int solenoidChannelA;
        public int solenoidChannelB;

        public SolenoidID(int pcmID, int solenoidChannel) {
            this(pcmID, solenoidChannel, -1);
        }

        public SolenoidID(int pcmID, int solenoidChannelA, int solenoidChannelB) {
            this.pcmID = pcmID;
            this.solenoidChannelA = solenoidChannelA;
            this.solenoidChannelB = solenoidChannelB;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;

            if (!(o instanceof SolenoidID))
                return false;

            SolenoidID other = (SolenoidID) o;
            return (other.pcmID == pcmID && other.solenoidChannelA == solenoidChannelB &&
                    other.solenoidChannelB == solenoidChannelB);
        }

    }
}
