package team6458.feedback;

import com.eclipsesource.json.*;
import team6458.util.Utils;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class holds mappings for speeds (units/sec) to a "coasting" distance. For example, at a certain travelling
 * speed X, cutting drivetrain power at this point would result in a coasting distance Y.
 */
public final class CoastDistance {

    private final TreeMap<Double, Double> map = new TreeMap<>();

    /**
     * @param json The string json
     * @return A {@link CoastDistance} representation of the json result
     * @throws IllegalArgumentException If the top level json parsed is not an object
     */
    public static CoastDistance fromJson(String json) {
        final CoastDistance cd = new CoastDistance();
        final JsonValue jsonValue = Json.parse(json);

        if (!jsonValue.isObject())
            throw new IllegalArgumentException("Json parsed is not an object");

        final JsonValue mapping = jsonValue.asObject().get("mapping");
        if (mapping != null && mapping.isArray()) {
            final JsonArray array = mapping.asArray();

            array.forEach(val -> {
                if (val.isObject()) {
                    final JsonObject entry = val.asObject();
                    final JsonValue speed = entry.get("speed");
                    final JsonValue distance = entry.get("distance");

                    if (speed != null && distance != null && speed.isNumber() && distance.isNumber()) {
                        cd.populate(speed.asDouble(), distance.asDouble());
                    }
                }
            });
        }

        return cd;
    }

    /**
     * @param prettyPrint If the output should be pretty-printed
     * @return A string json version of this object
     */
    public String toJson(boolean prettyPrint) {
        final JsonObject obj = Json.object();
        final JsonArray mapping = Json.array();

        map.forEach((speed, distance) -> mapping.add(
                Json.object()
                        .add("speed", speed)
                        .add("distance", distance)
        ));
        obj.add("mapping", mapping);

        return obj.toString(prettyPrint ? WriterConfig.PRETTY_PRINT : WriterConfig.MINIMAL);
    }

    /**
     * @param speed        The speed in units per second
     * @param defaultValue The default value to return if there are no mappings
     * @return The interpolated mapping of speed to coast distance; values outside of the min/max range are clamped to that range
     */
    public double getDistance(double speed, double defaultValue) {
        final Map.Entry<Double, Double> lowerEntry = map.floorEntry(speed);
        final Map.Entry<Double, Double> upperEntry = map.ceilingEntry(speed);

        if (lowerEntry == null) {
            if (map.firstEntry() == null)
                return defaultValue;
            return map.firstEntry().getValue();
        } else if (upperEntry == null) {
            if (map.lastEntry() == null)
                return defaultValue;
            return map.lastEntry().getValue();
        }

        final double lower = lowerEntry.getValue();
        final double upper = upperEntry.getValue();

        return Utils.lerp(lower, upper, (speed - lowerEntry.getKey()) / (upperEntry.getKey() - lowerEntry.getKey()));
    }

    /**
     * @param speed The speed in units per second
     * @return The interpolated mapping of speed to coast distance; values outside of the min/max range are clamped to that range; 0.0 if there are no mappings
     * @see #getDistance(double, double)
     */
    public double getDistance(double speed) {
        return getDistance(speed, 0.0);
    }

    /**
     * @param speed The speed in units per second
     * @return True if the internal map contains a mapping for this exact speed (no interpolation)
     */
    public boolean doesDirectMappingExist(double speed) {
        return map.containsKey(speed);
    }

    /**
     * Add a speed->distance mapping.
     *
     * @param speed    The speed in units per second
     * @param distance The coast distance
     * @return This object for chaining
     */
    public CoastDistance populate(double speed, double distance) {
        map.put(speed, distance);
        return this;
    }

}
