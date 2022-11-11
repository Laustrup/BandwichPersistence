package laustrup.bandwichpersistence.utilities;

import lombok.Getter;
import lombok.ToString;

import java.util.Random;

/**
 * A utility class, that behaves as a boolean, but with extra features.
 * Is named after the philosopher Plato ironically,
 * because of Plato's duality.
 * Instead of just being true or false, it has more values, such as undefined.
 * Uses an enum for identifying those values.
 * Can also be null, since it's a class object.
 */
@ToString
public class Plato implements IPlato {

    /**
     * Determines the value of the Plato class.
     * Is typed as an enum.
     */
    @Getter
    private Argument _argument;

    /**
     * Sets the argument and also calculates truth.
     * @param argument An enum with multiple values,
     *                 that define the value of the Plato class.
     *                 This is the value that will be the new argument of the Plato class.
     * @return The calculated truth variable, which means true if argument is TRUE.
     */
    public boolean set_argument(Argument argument) {
        _argument = argument;
        return determineTruth();
    }

    /**
     * Sets the argument and also calculates truth.
     * @param isTrue Determines if argument should be TRUE or not.
     * @return The calculated truth variable, which means true if argument is TRUE.
     */
    public boolean set_argument(boolean isTrue) {
        if (isTrue) _argument = Argument.TRUE;
        else _argument = Argument.FALSE;
        return determineTruth();
    }

    /**
     * Checks if the argument is true or not.
     * @return A boolean version of argument.
     */
    public boolean translateArgument() { return _argument == Argument.TRUE; }

    /**
     * Will only be true, if argument is TRUE.
     * Is calculated each time argument changes.
     */
    private Boolean _truth;

    /**
     * Gets the boolean value of the Plato class.
     * @return Both truth and argument should be of value true.
     */
    public boolean get_truth() { return _truth && (_argument == Argument.TRUE || _argument == Argument.ABOVE_HALF); }

    public Plato() { this(Argument.UNDEFINED); }
    public Plato(Argument argument) { set_argument(argument); }
    public Plato(boolean isTrue) { set_argument(isTrue); }

    @Override
    public boolean randomize() { return randomize(1); }

    @Override
    public boolean randomize(int change) {
        if (new Random().nextInt(change + 1) != 0) _argument = Argument.TRUE;
        else _argument = Argument.FALSE;
        return determineTruth();
    }

    /**
     * Will calculate the truth from argument.
     * @return The calculated truth.
     */
    private Boolean determineTruth() {
        _truth = _argument == Argument.TRUE || _argument == Argument.ABOVE_HALF;
        return _truth;
    }

    /**
     * Contains the different value options for a Plato class.
     */
    public enum Argument {
        TRUE,
        FALSE,
        UNDEFINED,
        ABOVE_HALF,
        BELOW_HALF
    }
}
