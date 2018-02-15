package team6458.util;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A utility class that prevents {@link edu.wpi.first.wpilibj.util.AllocationException}s by holding a global
 * reference to a hardware component.
 */
public final class Allocator {

    private static final Registrar<Spark> SPARK_REGISTRAR = new Registrar<>(Spark::new);
    private static final Registrar<DigitalInput> DIGITAL_INPUT_REGISTRAR = new Registrar<>(DigitalInput::new);
    private static final Registrar<AnalogInput> ANALOG_INPUT_REGISTRAR = new Registrar<>(AnalogInput::new);

    /**
     * Gets or creates a Spark instance.
     *
     * @param port The PWM port to use
     * @return A new or reused instance
     */
    public static Spark spark(int port) {
        return SPARK_REGISTRAR.getOrCreate(port);
    }

    /**
     * Gets or creates a DigitalInput instance.
     *
     * @param port The DIO port to use
     * @return A new or reused instance
     */
    public static DigitalInput digitalInput(int port) {
        return DIGITAL_INPUT_REGISTRAR.getOrCreate(port);
    }

    /**
     * Gets or creates a AnalogInput instance.
     *
     * @param port The analog port to use
     * @return A new or reused instance
     */
    public static AnalogInput analogInput(int port) {
        return ANALOG_INPUT_REGISTRAR.getOrCreate(port);
    }

    private static class Registrar<T> {

        private final Function<Integer, T> supplier;
        private final Map<Integer, T> map = new HashMap<>();

        private Registrar(Function<Integer, T> supplier) {
            this.supplier = supplier;
        }

        private T getOrCreate(int port) {
            return map.computeIfAbsent(port, supplier);
        }
    }

}
