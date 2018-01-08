package team6458.util;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a utility class that has the data for the sides that the plates are on.
 * The perspective is relative to the team facing outwards.
 */
public final class PlateAssignment {
    //First To avoid null pointer exception
    private static final Logger LOGGER = Logger.getLogger(PlateAssignment.class.getName());

    /**
     * A constant with only {@link PlateSide#INVALID} plate sides. This will be used when there is no FMS (e.g.: test runs).
     */
    public static final PlateAssignment ALL_INVALID = new PlateAssignment("???");

    /**
     * Private internal array of plate sides, in order of nearest, centre, farthest from the alliance wall.
     */
    private final PlateSide[] sides;
    /**
     * A cached string only used in {@link #toString()} to avoid overallocation.
     */
    private final String cachedString;

    /**
     * Construct an instance using a three-letter message. Ex: LLL, LRL, LRR, RRR, RLR
     *
     * @param input A three-letter, non-null string, only consisting of the letters L and R
     */
    public PlateAssignment(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        sides = new PlateSide[] {
                PlateSide.getFromLetter(input.charAt(0)),
                PlateSide.getFromLetter(input.charAt(1)),
                PlateSide.getFromLetter(input.charAt(2))
        };
        cachedString = getNearest().toString() + getScale().toString() + getFarthest().toString();

        // warn for plate sides being INVALID, If ???, the input is intentional and no error message should be displayed
        if (Arrays.stream(sides).anyMatch(ps -> ps == PlateSide.INVALID) && !input.equals("???")) {
            LOGGER.log(Level.WARNING, "Found unknown PlateSides: " + Arrays.toString(sides));
        }
    }

    /**
     * @return The direction for the switch nearest to the alliance wall
     */
    public PlateSide getNearest() {
        return sides[0];
    }

    /**
     * @return The direction for the centre scale
     */
    public PlateSide getScale() {
        return sides[1];
    }

    /**
     * @return The direction for the switch farthest from the alliance wall (nearest to the enemy's)
     */
    public PlateSide getFarthest() {
        return sides[2];
    }

    @Override
    public String toString() {
        return cachedString;
    }

    /**
     * A simple enum that either indicates left, right, or an invalid position.
     */
    enum PlateSide {
        LEFT('L'), RIGHT('R'), INVALID('?');

        public final char letter;

        PlateSide(char letter) {
            this.letter = letter;
        }

        /**
         * Get a PlateSide enum from a letter.
         *
         * @param letter L, R
         * @return The corresponding PlateSide enum, or the {@link #INVALID} enum if it doesn't match
         */
        public static PlateSide getFromLetter(char letter) {
            switch (letter) {
                case 'L':
                    return LEFT;
                case 'R':
                    return RIGHT;
                default:
                    return INVALID;
            }
        }

        @Override
        public String toString() {
            return Character.toString(letter);
        }
    }

}
