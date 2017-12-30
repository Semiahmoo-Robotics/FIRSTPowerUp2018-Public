package team6458.util

import edu.wpi.first.wpilibj.*


/**
 * Adapted from https://github.com/Open-RIO/ToastAPI/
 *
 * The Registrar is a simple class designed to get rid of Allocation exceptions in code, where if multiple
 * instances of a Motor Controller, DIO, or Analog interface are created with the same port, WPILib throws
 * a big fat error in your face. To solve this, the Registrar keeps one instance of each when you request it,
 * allowing for multiple accesses, but not multiple constructions.
 *
 * @param ID - The type to use for the Identifier. In most cases, this is an integer.
 * @param Type - The type to store in this registrar. This is the superclass.
 */
class Registrar<ID, Type> {

    var registered = hashMapOf<ID, Type>()
        private set

    /**
     * Fetch an object from the Registrar, or create it if it doesn't exist yet.

     * @param id        The ID to store the object under
     * *
     * @param clazz     The Class (Type) of the object you are inserting (subclass of Type)
     * *
     * @param creator   The lambda function that will construct a new instance of the object if it doesn't
     * *                  already exist in the registrar
     * *
     * @param <T>       The type to insert / fetch from the registrar. This is a subclass of the Registrar's Type
     * *
     * @return          The object in the registrar / the new object created.
    </T> */
    @Synchronized
    fun <T : Type> fetch(id: ID, clazz: Class<T>, creator: () -> T): T {
        var in_register: Type? = registered[id]

        if (in_register == null) {
            registered.keys
                    .filter { it == id }
                    .forEach { in_register = registered[it] }
        }

        if (in_register == null) {
            val instance = creator.invoke()
            registered.put(id, instance)
            return instance
        } else {
            if (clazz.isAssignableFrom(in_register as Class<*>?))
                return in_register as T
            else
                throw IllegalStateException("An object already exists in the Registrar with the given ID, " +
                                                    "but is of a different type! Expected: " + clazz.name + ", but got: " + in_register)
        }
    }

    /**
     * Return the stream of the underlying Registrar hashmap. Use this to directly manipulate the registrar
     */
    @Synchronized
    fun stream() = registered.entries.stream()

    class SolenoidID(var pcmID: Int, var solenoidChannelA: Int, var solenoidChannelB: Int) {

        constructor(pcmID: Int, solenoidChannel: Int) : this(pcmID, solenoidChannel, -1) {}

        override fun equals(o: Any?): Boolean {
            if (o == null)
                return false

            if (o !is SolenoidID)
                return false

            return o.pcmID == pcmID
                    && o.solenoidChannelA == solenoidChannelB
                    && o.solenoidChannelB == solenoidChannelB
        }

    }

    companion object {

        // -- STATICS -- //

        @Volatile private var pwmRegistrar = Registrar<Int, PWM>()
        @Volatile private var canRegistrar = Registrar<Int, SpeedController>()
        @Volatile private var dioRegistrar = Registrar<Int, DigitalSource>()
        @Volatile private var relayRegistrar = Registrar<Int, Relay>()
        @Volatile private var aiRegistrar = Registrar<Int, AnalogInput>()
        @Volatile private var aoRegistrar = Registrar<Int, AnalogOutput>()
        @Volatile private var compressorRegistrar = Registrar<Int, Compressor>()
        @Volatile private var solenoidRegistrar = Registrar<SolenoidID, SolenoidBase>()

        /**
         * Get a DigitalOutput instance from the Registrar
         * @param port the DIO port to use
         */
        fun digitalOutput(port: Int): DigitalOutput {
            return dioRegistrar.fetch(port, DigitalOutput::class.java, { DigitalOutput(port) })
        }

        /**
         * Get a DigitalInput instance from the Registrar
         * @param port the DIO port to use
         */
        fun digitalInput(port: Int): DigitalInput {
            return dioRegistrar.fetch(port, DigitalInput::class.java, { DigitalInput(port) })
        }

        /**
         * Get an AnalogOutput instance from the Registrar
         * @param port the AO port to use
         */
        fun analogOutput(port: Int): AnalogOutput {
            return aoRegistrar.fetch(port, AnalogOutput::class.java, { AnalogOutput(port) })
        }

        /**
         * Get an AnalogInput instance from the Registrar
         * @param port the AI port to use
         */
        fun analogInput(port: Int): AnalogInput {
            return aiRegistrar.fetch(port, AnalogInput::class.java, { AnalogInput(port) })
        }

        /**
         * Get a Spike style Relay instance from the Registrar
         * @param relayPort the Relay port to use
         */
        fun relay(relayPort: Int): Relay {
            return relayRegistrar.fetch(relayPort, Relay::class.java, { Relay(relayPort) })
        }

        // -- Motor Controllers -- //

        /**
         * Get a Talon [SR] instance from the Registrar
         * @param pwmPort the PWM Port to use
         */
        fun talon(pwmPort: Int): Talon {
            return pwmRegistrar.fetch(pwmPort, Talon::class.java, { Talon(pwmPort) })
        }

        /**
         * Get a Talon SRX [PWM] instance from the Registrar
         * @param pwmPort the PWM Port to use
         */
        fun talonSRX(pwmPort: Int): TalonSRX {
            return pwmRegistrar.fetch(pwmPort, TalonSRX::class.java, { TalonSRX(pwmPort) })
        }

        /**
         * Get a Jaguar [PWM] instance from the Registrar
         * @param pwmPort the PWM port to use
         */
        fun jaguar(pwmPort: Int): Jaguar {
            return pwmRegistrar.fetch(pwmPort, Jaguar::class.java, { Jaguar(pwmPort) })
        }

        /**
         * Get a Victor instance from the Registrar
         * @param pwmPort the PWM port to use
         */
        fun victor(pwmPort: Int): Victor {
            return pwmRegistrar.fetch(pwmPort, Victor::class.java, { Victor(pwmPort) })
        }

        /**
         * Get a Victor SP instance from the Registrar
         * @param pwmPort the PWM port to use
         */
        fun victorSP(pwmPort: Int): VictorSP {
            return pwmRegistrar.fetch(pwmPort, VictorSP::class.java, { VictorSP(pwmPort) })
        }

        /**
         * Get a Spark instance from the Registrar
         * @param pwmPort the PWM port to use
         */
        fun spark(pwmPort: Int): Spark {
            return pwmRegistrar.fetch(pwmPort, Spark::class.java, { Spark(pwmPort) })
        }

        /**
         * Get an SD540 instance from the Registrar
         * @param pwmPort the PWM port to use
         */
        fun sd540(pwmPort: Int): SD540 {
            return pwmRegistrar.fetch(pwmPort, SD540::class.java, { SD540(pwmPort) })
        }

        /**
         * Get a Servo instance from the Registrar
         * @param pwmPort the PWM port to use
         */
        fun servo(pwmPort: Int): Servo {
            return pwmRegistrar.fetch(pwmPort, Servo::class.java, { Servo(pwmPort) })
        }

        // Use only if CANTalon is available
//        /**
//         * Get a Talon SRX [CAN] instance from the Registrar
//         * @param canID the CAN Device ID to use
//         */
//        fun canTalon(canID: Int): CANTalon {
//            return canRegistrar.fetch(canID, CANTalon::class.java, { CANTalon(canID) })
//        }

        // -- Pneumatics -- //

        /**
         * Get a Compressor instance from the Registrar
         * @param pcmID the PCM CAN Device ID to use
         */
        fun compressor(pcmID: Int): Compressor {
            return compressorRegistrar.fetch(pcmID, Compressor::class.java, { Compressor(pcmID) })
        }

        /**
         * Get a Solenoid instance from the Registrar
         * @param pcmID the PCM CAN Device ID to use
         * *
         * @param solenoidChannel the channel on the PWM to use
         */
        fun solenoid(pcmID: Int, solenoidChannel: Int): Solenoid {
            return solenoidRegistrar.fetch(SolenoidID(pcmID, solenoidChannel), Solenoid::class.java,
                                           { Solenoid(pcmID, solenoidChannel) })
        }

        /**
         * Get a Solenoid instance from the Registrar
         * @param solenoidChannel the channel on the PWM to use
         */
        fun solenoid(solenoidChannel: Int): Solenoid {
            return solenoid(0, solenoidChannel)
        }

        /**
         * Get a DoubleSolenoid instance from the Registrar
         * @param pcmID the PCM CAN Device ID to use
         * *
         * @param solenoidFwdChannel the forward channel on the PWM to use
         * *
         * @param solenoidRevChannel the reverse channel on the PWM to use
         */
        fun doubleSolenoid(pcmID: Int, solenoidFwdChannel: Int, solenoidRevChannel: Int): DoubleSolenoid {
            return solenoidRegistrar.fetch(SolenoidID(pcmID, solenoidFwdChannel, solenoidRevChannel),
                                           DoubleSolenoid::class.java,
                                           { DoubleSolenoid(pcmID, solenoidFwdChannel, solenoidRevChannel) })
        }

        /**
         * Get a DoubleSolenoid instance from the Registrar
         * @param solenoidFwdChannel the forward channel on the PWM to use
         * *
         * @param solenoidRevChannel the reverse channel on the PWM to use
         */
        fun doubleSolenoid(solenoidFwdChannel: Int, solenoidRevChannel: Int): DoubleSolenoid {
            return doubleSolenoid(0, solenoidFwdChannel, solenoidRevChannel)
        }
    }
}