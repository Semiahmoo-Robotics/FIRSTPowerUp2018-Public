package team6458.sensor;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * A class ported over from 2017.
 * <p>
 * Using an {@link edu.wpi.first.wpilibj.AnalogInput}, this class outputs distance based on an ultrasonic rangefinder
 * such as the <a href=https://www.maxbotix.com/Ultrasonic_Sensors/MB1010.htm>MB1010 LV-MaxSonar-EZ1</a>.
 */
public class AnalogRangefinder implements Sendable {

    public static final double DEFAULT_SCALING_FACTOR = 5.0 / 512.0;
    private final AnalogInput analog;
    private final double scalingFactorVoltsPerInch;

    /**
     * Create a rangefinder instance with the {@link #DEFAULT_SCALING_FACTOR}.
     */
    public AnalogRangefinder(AnalogInput analog) {
        this(analog, DEFAULT_SCALING_FACTOR);
    }

    public AnalogRangefinder(AnalogInput analog, double scalingFactorVoltsPerInch) {
        this.analog = analog;
        this.scalingFactorVoltsPerInch = scalingFactorVoltsPerInch;
    }

    /**
     * Get the distance in the units provided.
     *
     * @param units The units to use
     * @return The value in the units provided
     */
    public double getDistance(Units units) {
        return (analog.getAverageVoltage() / scalingFactorVoltsPerInch) * units.inchMult;
    }

    /**
     * Get the distance in the units provided as the string {@code "# in/cm"} (in/cm depending on units).
     *
     * @param units The units to use
     * @return The value in the units provided, with the units appended
     */
    public String getStringDistance(Units units) {
        return getDistance(units) + " " + units.unitName;
    }

    @Override
    public String getName() {
        return "AnalogRangefinder";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getSubsystem() {
        return null;
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("AnalogRangefinder");
        builder.addDoubleProperty("Voltage", analog::getAverageVoltage, null);
        builder.addDoubleProperty("Distance (in)", () -> getDistance(Units.INCHES), null);
        builder.addDoubleProperty("Distance (cm)", () -> getDistance(Units.CM), null);
    }

    enum Units {
        INCHES(1.0, "in"), CM(2.54, "cm");

        public final double inchMult;
        public final String unitName;

        Units(double inchMult, String unitName) {
            this.inchMult = inchMult;
            this.unitName = unitName;
        }
    }

}
