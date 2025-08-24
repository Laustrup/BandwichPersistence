package laustrup.bandwichpersistence.quality_assurance;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class Asserter {

    public static <EXPECTED> Checker<EXPECTED> asserting(EXPECTED expected) {
        return Checker.of(expected);
    }

    public static class Checker<EXPECTED> implements AssertionChecker<EXPECTED> {

        private final EXPECTED _expected;

        private final boolean _negate;

        private boolean
                _equalToChecked,
                _notEqualToChecked;

        public Checker(EXPECTED expected) {
            this(expected, false);
        }

        private Checker(EXPECTED expected, boolean negate) {
            _expected = expected;
            _negate = negate;
        }

        public Checker<EXPECTED> not(boolean negate) {
            return new Checker<>(_expected, negate);
        }

        public Checker<EXPECTED> not() {
            return not(true);
        }

        public static <EXPECTED> Checker<EXPECTED> of(EXPECTED expected) {
            return new Checker<>(expected);
        }

        @Override
        public AssertionChecker<EXPECTED> is(Predicate<EXPECTED> assertion) {
            return check(() -> assertingTrue(assertion.test(_expected)));
        }

        @Override
        public AssertionChecker<EXPECTED> is(Supplier<EXPECTED> supplier) {
            return check(() -> is(supplier.get()));
        }

        @Override
        public AssertionChecker<EXPECTED> is(EXPECTED actual) {
            return check(() -> _equalToChecked = handleEqualing(() -> assertingEqualsTo(_expected, actual), true));
        }

        @Override
        public AssertionChecker<EXPECTED> isNot(EXPECTED actual) {
            return check(() -> _notEqualToChecked = handleEqualing(() -> assertingNotEqualsTo(_expected, actual), false));
        }

        @Override
        public AssertionChecker<EXPECTED> isTrue() {
            return check(() -> assertingTrue((boolean) _expected));
        }

        @Override
        public AssertionChecker<EXPECTED> contains(EXPECTED actual) {
            return check(() -> assertingTrue(((List<?>) _expected).contains(actual)));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <PREXPECTED> AssertionChecker<EXPECTED> anyMatches(Predicate<PREXPECTED> assertion) {
            return check(() -> assertingTrue(
                    ((List<PREXPECTED>) _expected).stream()
                            .anyMatch(assertion)
            ));
        }

        @Override
        public AssertionChecker<EXPECTED> inCase(boolean condition, Predicate<EXPECTED> assertion) {
            return check(() -> {
                if (condition)
                    assertingTrue(assertion.test(_expected));
            });
        }

        @Override
        public AssertionChecker<EXPECTED> isNotNull() {
            return check(() -> assertingNotNull(_expected));
        }

        private AssertionChecker<EXPECTED> check(Runnable action) {
            return check(this, action);
        }

        public static <PREXPECTED> AssertionChecker<PREXPECTED> check(AssertionChecker<PREXPECTED> checker, Runnable action) {
            action.run();
            return checker;
        }

        public void compare(EXPECTED actual) {
            if (actual == null)
                fail(new NullPointerException("When comparing expected with actual, actual was null!"));

            Map<String, Field> actualFields = Arrays.stream(actual.getClass().getDeclaredFields())
                    .collect(Collectors.toMap(Field::getName, field -> field));

            Arrays.stream(_expected.getClass().getDeclaredFields()).forEach(field -> {
                try {
                    assertEquals(field.get(_expected), actualFields.get(field.getName()).get(actual));
                } catch (IllegalAccessException e) {
                    fail(e.getMessage());
                }
            });
        }

        private void assertingEqualsTo(EXPECTED expected, EXPECTED actual) {
            if (!_negate)
                assertEquals(expected, actual);
            else
                assertNotEquals(expected, actual);
        }

        private void assertingNotEqualsTo(EXPECTED expected, EXPECTED actual) {
            if (!_negate)
                assertNotEquals(expected, actual);
            else
                assertEquals(expected, actual);
        }

        private void assertingTrue(boolean condition) {
            assertTrue(condition != _negate);
        }

        private void assertingFalse(boolean condition) {
            assertFalse(condition != _negate);
        }

        private void assertingNotNull(EXPECTED expected) {
            if (!_negate)
                assertNotNull(expected);
            else
                assertNull(expected);
        }

        private void assertingNull(EXPECTED expected) {
            if (!_negate)
                assertNull(expected);
            else
                assertNotNull(expected);
        }

        private boolean handleEqualing(Runnable action, boolean isEqualTo) {
            if ((_notEqualToChecked && isEqualTo) || (_equalToChecked && !isEqualTo))
                throw new AssertionError("Already checked if " + _expected + " was " + (isEqualTo ? "" : "not") + " equal!");
            action.run();
            return true;
        }
    }
}
