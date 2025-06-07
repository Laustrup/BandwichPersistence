package laustrup.bandwichpersistence.quality_assurance;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        public AssertionChecker<E> isEqualTo(E actual) {
            return check(() -> _equalToChecked = handleEqualing(() -> assertEquals(_expected, actual), true));
        }

        @Override
        public AssertionChecker<E> isNotEqualTo(E actual) {
            return check(() -> _notEqualToChecked = handleEqualing(() -> assertNotEquals(_expected, actual), false));
        }

        @Override
        public AssertionChecker<E> is(Predicate<E> assertion) {
            return check(() -> assertTrue(assertion.test(_expected)));
        }

        @Override
        public AssertionChecker<E> isTrue() {
            return check(() -> assertTrue((boolean) _expected));
        }

        @Override
        public AssertionChecker<E> is(Supplier<E> supplier) {
            return check(() -> isEqualTo(supplier.get()));
        }

        @Override
        public AssertionChecker<E> contains(E actual) {
            return check(() -> assertTrue(((List<?>) _expected).contains(actual)));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <W> AssertionChecker<E> anyMatches(Predicate<W> assertion) {
            return check(() -> assertTrue(
                    ((List<W>) _expected).stream()
                            .anyMatch(assertion)
            ));
        }

        @Override
        public AssertionChecker<E> inCase(boolean condition, Predicate<E> assertion) {
            return check(() -> {
                if (condition)
                    assertTrue(assertion.test(_expected));
            });
        }

        @Override
        public AssertionChecker<E> isNotNull() {
            return check(() -> assertNotNull(_expected));
        }

        private AssertionChecker<E> check(Runnable action) {
            return check(this, action);
        }

        public static <T> AssertionChecker<T> check(AssertionChecker<T> checker, Runnable action) {
            action.run();
            return checker;
        }
    }
}
