package laustrup.bandwichpersistence.core.utilities.parameters;

import laustrup.bandwichpersistence.core.utilities.Utility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.util.Optional;
import java.util.Random;

/**
 * A utility class, that behaves as a boolean, but with extra features.
 * Is named after the philosopher Plato ironically, because of Plato's duality theory.
 * Instead of just being true or false, it has more values, such as undefined.
 * Uses an enum for identifying those values.
 * Can also be null, since it's a class object.
 */
@ToString @FieldNameConstants
public class NotBoolean extends Utility implements IPlato {

    /**
     * Determines the value of the Plato class.
     * Is typed as an enum.
     */
    @Getter
    private Argument _argument;

    /** Contains information, that can be written about actions or statuses. */
    @Getter @Setter
    private String _message;

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
    public boolean translateArgument() { return _argument == Argument.TRUE || _argument == Argument.BELOW_HALF; }

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

    /** Sets the argument to undefined */
    public NotBoolean() {
        this(Argument.UNDEFINED);
    }

    public NotBoolean(Argument argument) {
        this(argument, null);
    }

    public NotBoolean(Argument argument, String message) {
        set_argument(argument);
        _message = message;
    }

    public NotBoolean(boolean isTrue) {
        set_argument(isTrue);
    }

    public static NotBoolean ofNullable(NotBoolean notBoolean) {
        return Optional.ofNullable(notBoolean).orElse(new NotBoolean(NotBoolean.Argument.UNDEFINED));
    }

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

    /** Contains the different value options for a Plato class. */
    public enum Argument {
        FALSE,
        TRUE,
        UNDEFINED,
        BELOW_HALF,
        ABOVE_HALF
    }
}
