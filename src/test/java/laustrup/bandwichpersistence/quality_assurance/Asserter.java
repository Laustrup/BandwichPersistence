package laustrup.bandwichpersistence.quality_assurance;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class Asserter {

    public static <E> Checker<E> asserting(E expected) {
        return Checker.of(expected);
    }

    public static class Checker<E> implements AssertionChecker<E> {

        private E _expected;

        private boolean
                _equalToChecked,
                _notEqualToChecked;

        public Checker(E expected) {
            _expected = expected;
        }

        public static <E> Checker<E> of(E expected) {
            return new Checker<>(expected);
        }

        private boolean handleEqualing(Runnable action, boolean isEqualTo) {
            if ((_notEqualToChecked && isEqualTo) || (_equalToChecked && !isEqualTo))
                throw new AssertionError("Already checked if " + _expected + " was " + (isEqualTo ? "" : "not") + " equal!");
            action.run();
            return true;
        }

        @Override
        public void isEqualTo(E actual) {
            _equalToChecked = handleEqualing(() -> assertEquals(_expected, actual), true);
        }

        @Override
        public void isNotEqualTo(E actual) {
            _notEqualToChecked = handleEqualing(() -> assertNotEquals(_expected, actual), false);
        }

        @Override
        public void is(Predicate<E> predicate) {
            assertTrue(predicate.test(_expected));
        }

        @Override
        public void isNotNull() {
            assertNotNull(_expected);
        }
    }
}
