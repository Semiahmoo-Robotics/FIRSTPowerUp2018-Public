# Team 6458's *FIRST* POWER UP

Welcome to Team 6458's code repository for the 2018 *FIRST* POWER UP
Robotics Competition.

## Resources
* [Shuffleboard Maven link](http://first.wpi.edu/FRC/roborio/maven/release/edu/wpi/first/shuffleboard/app/)
* [2018 FRC Control System](https://wpilib.screenstepslive.com/s/4485)
* [Command based programming](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/599732-what-is-command-based-programming)
* [Using SendableChooser for options on the dashboard](https://wpilib.screenstepslive.com/s/currentCS/m/smartdashboard/l/255419-choosing-an-autonomous-program-from-smartdashboard)

## Code Specifications
* Java **8** only. For the time being, there are no other JVM languages (the next consideration is Kotlin).
  * Java 9 is not supported, unfortunately.
* Use `private static final Logger LOGGER` instances where logging is necessary. See
[SemiRobot.java](src/main/java/team6458/SemiRobot.java) for an example.
* [Null is bad.](https://en.wikipedia.org/wiki/Tony_Hoare#Apologies_and_retractions)
Avoid nulls wherever possible.
State all accepted inputs as nullable or non-null. The last thing that should
happen is that the robot locks up and crashes with a NullPointerException
due to poor practices.
  * If nulls are used in non-closed scope situations, you will have to
  justify its use. You will most likely be asked to rewrite it without null.

### Formatting
* Four spaces ONLY
  * [Eclipse instructions](https://stackoverflow.com/questions/407929/how-do-i-change-eclipse-to-use-spaces-instead-of-tabs)
* Correct indentations for all code
* Keep it neat, please. This won't be nitpicked to a T, but it shouldn't
look like a bomb went off.

### Javadocs
* **EVERY** Javadoc-able thing needs Javadoc documentation, unless it is:
  * A `private static final Logger LOGGER` instance in a class
  * An obvious getter/setter
  * Note that normal code should only be commented with normal comments
* All documentation must be grammatically correct English with no spelling errors
  * Note: text for `@tags` like `@param` or `@returns` do not need a full stop
* See [PlateAssignment.java](src/main/java/team6458/util/PlateAssignment.java) for a thorough example.
* Tip: write your documentation like a serial killer is out to get you.
That's what it feels like to stare at code weeks or days later sometimes.
Save yourself the hassle in the future and do it properly in the present.
