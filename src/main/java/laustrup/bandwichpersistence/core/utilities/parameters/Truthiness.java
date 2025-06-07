package laustrup.bandwichpersistence.core.utilities.parameters;

import laustrup.bandwichpersistence.core.utilities.Utility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@ToString @FieldNameConstants
public class Truthiness extends Utility<Void> implements ITruthiness {

    @Getter
    private Argument _argument;

    @Getter @Setter
    private String _message;

    public Truthiness() {
        this(Argument.UNDEFINED);
    }

    public Truthiness(Argument argument) {
        this(argument, null);
    }

    public Truthiness(Argument argument, String message) {
        set_argument(argument);
        _message = message;
    }

    public Truthiness(boolean isTrue) {
        set_argument(isTrue);
    }

    public boolean set_argument(BigDecimal point) {
        _argument = Argument.of(point);
        return _argument.is_true();
    }

    public boolean set_argument(Argument argument) {
        return set_argument(argument.is_true());
    }

    public boolean set_argument(boolean isTrue) {
        _argument = isTrue ? Argument.TRUE : Argument.FALSE;
        return _argument.is_true();
    }

    public static Truthiness ofNullable(Truthiness truthiness) {
        return Optional.ofNullable(truthiness).orElse(new Truthiness(Argument.UNDEFINED));
    }

    public boolean is_totallyTrue() {
        return _argument.get_point().equals(BigDecimal.ONE);
    }

    @Override
    public boolean randomize() { return randomize(1); }

    @Override
    public boolean randomize(int change) {
        _argument = new Random().nextInt(change + 1) != 0
                ? Argument.TRUE
                : Argument.FALSE;

        return _argument.is_true();
    }

    @Getter
    public enum Argument {
        FALSE(0),
        TRUE(1),
        UNDEFINED(0.5),
        BELOW_HALF(0.49),
        ABOVE_HALF(0.51);

        private final boolean _true;

        private BigDecimal _point;

        Argument(double point) {
            set_point(BigDecimal.valueOf(point));
            _true = point > 0.5;
        }

        public BigDecimal set_point(BigDecimal point) {
            validate(point);
            _point = point;
            return _point;
        }

        public static Argument of(BigDecimal point) {
            validate(point);
            if (point.equals(BigDecimal.ZERO))
                return FALSE;
            if (point.equals(BigDecimal.ONE))
                return TRUE;
            if (point.compareTo(BigDecimal.valueOf(0.5)) < 0)
                return BELOW_HALF;
            if (point.compareTo(BigDecimal.valueOf(0.5)) > 0)
                return ABOVE_HALF;
            return UNDEFINED;
        }

        private static void validate(BigDecimal point) {
            if (point.compareTo(BigDecimal.ZERO) < 0 || point.compareTo(BigDecimal.ONE) > 0)
                throw new IllegalArgumentException("Point value must not be less than 0 nor higher than 1!");
        }

        public boolean is_false() {
            return !_true;
        }
    }
}
