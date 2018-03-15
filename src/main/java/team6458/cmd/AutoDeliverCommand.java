package team6458.cmd;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team6458.SemiRobot;
import team6458.cmd.RotateCommand.SpeedGradient;
import team6458.util.PlateAssignment.PlateSide;


/**
 * Autonomously drive from an alliance station to a switch plate, and deliver (optionally).
 * <p>This does not handle far cases, i.e.: LEFT station to RIGHT switch or RIGHT station to LEFT switch.
 */
public class AutoDeliverCommand extends CommandGroup {

    public enum AllianceSide {
        LEFT, CENTRE, RIGHT
    }

    /** +u☻0.
     * The speed at which to run the intakes at.
     */
    public static final double INTAKE_SPEED = 1.0;
    /**
     * The time in seconds to run the intake for.
     */
    public static final double INTAKE_TIME = 4.0;

    /**
     * Constructor. All parameters should not be null.
     *
     * @param robot          The robot instance
     * @param allianceSide   The side of the alliance wall
     * @param plateSide      The plate side
     * @param shouldDeliver  True to actually deliver the cube IF POSSIBLE, false to not attempt
     * @param throttle       The throttle to drive at
     * @param rotateGradient The speed gradient to use while rotating
     */
    public AutoDeliverCommand(final SemiRobot robot, final AllianceSide allianceSide,
                              final PlateSide plateSide, final boolean shouldDeliver, final double throttle,
                              final SpeedGradient rotateGradient) {
        super(allianceSide.toString() + ", deliver: " + shouldDeliver);

        final boolean canDeliver = shouldDeliver &&
                !((allianceSide == AllianceSide.LEFT && plateSide == PlateSide.RIGHT) ||
                        (allianceSide == AllianceSide.RIGHT && plateSide == PlateSide.LEFT));

        if (allianceSide == AllianceSide.CENTRE) {
            if (plateSide == PlateSide.LEFT) {
                addSequential(new DriveStraightCommand(robot, -0.3, throttle));
                addSequential(new RotateCommand(robot, -45, rotateGradient));
                addSequential(new DriveStraightCommand(robot, -2.46, throttle));
                addSequential(new RotateCommand(robot, 45, rotateGradient));
                addSequential(new DriveStraightCommand(robot, -1.0, throttle) {
                    {
                        setTimeout(0.5);
                    }
                });
            } else {
                addSequential(new DriveStraightCommand(robot, -1.3, throttle));
                addSequential(new RotateCommand(robot, 45, rotateGradient));
                addSequential(new DriveStraightCommand(robot, -1.05, throttle));
                addSequential(new RotateCommand(robot, -45, rotateGradient));
                addSequential(new DriveStraightCommand(robot, -1.1, throttle) {
                    {
                        setTimeout(0.5);
                    }
                });
            }
        } else {
            final int sideSign = allianceSide == AllianceSide.LEFT ? -1 : 1; // -1 if left plate, 1 for right
            addSequential(new DriveStraightCommand(robot, -0.84, throttle));
            addSequential(new RotateCommand(robot, -sideSign * 45, rotateGradient));
            addSequential(new DriveStraightCommand(robot, -1.4, throttle));
            addSequential(new RotateCommand(robot, sideSign * 45, rotateGradient));
            addSequential(new DriveStraightCommand(robot, -1.1, throttle) {
                {
                    setTimeout(0.5);
                }
            });
        }

        if (canDeliver) {
            addSequential(new RampMotorCommand(robot, INTAKE_SPEED, INTAKE_TIME));
        }
    }
}
