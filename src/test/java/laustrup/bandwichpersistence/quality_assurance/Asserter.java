package laustrup.bandwichpersistence.quality_assurance;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getValue;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class Asserter {

  public static <EXPECTED> Checker<EXPECTED> asserting(EXPECTED expected) {
    return Checker.of(expected);
  }

  public static <EXPECTED extends Collection<EXPECTED_ELEMENT>, EXPECTED_ELEMENT>
      Checker.CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> asserting(EXPECTED expected) {
    return Checker.CollectiveChecker.of(expected);
  }

  public static class Checker<EXPECTED> implements AssertionChecker<EXPECTED> {

    protected final EXPECTED _expected;

    protected final boolean _negate;

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
      return check(() -> _equalToChecked = handleEqualing(actual, () -> assertingEqualsTo(_expected, actual), true));
    }

    @Override
    public AssertionChecker<EXPECTED> isNot(EXPECTED actual) {
      return check(() -> _notEqualToChecked = handleEqualing(actual, () -> assertingNotEqualsTo(_expected, actual), false));
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
          ((Collection<PREXPECTED>) _expected).stream()
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


    protected AssertionChecker<EXPECTED> check(Runnable action) {
      return check(this, action);
    }

    public static <PREXPECTED> AssertionChecker<PREXPECTED> check(AssertionChecker<PREXPECTED> checker, Runnable action) {
      action.run();
      return checker;
    }

    @Override
    public AssertionChecker<EXPECTED> compare(EXPECTED actual) {
      if (!Clearance.ACCEPTED.equals(clearanceCheck(actual)))
        throw Clearance.Exception.isNotAccepted(ifNotNull(actual)
            .then("Expected")
            .orElse("Actual") + " is null"
        );

      return check(() -> {
        Map<String, Field> actualFields = Arrays.stream(actual.getClass().getDeclaredFields())
            .collect(Collectors.toMap(Field::getName, identity()));

        Function<String, IllegalStateException> exceptionHandler = fieldName ->
            new IllegalStateException(String.format("No field of %s in %s",
                fieldName,
                _expected.getClass().getSimpleName()
            ));

        Consumer<Field> comparing = field -> {
          Object actualValue = getValue(actual, actualFields.get(field.getName()))
              .orElseThrow(() -> exceptionHandler.apply(field.getName()));

          if (actualValue == null)
            throw exceptionHandler.apply(field.getName());

          assertEquals(getValue(_expected, field).orElseThrow(), actualValue);
        };

        Arrays.stream(_expected.getClass().getDeclaredFields()).forEach(comparing);
      });
    }

    protected Clearance clearanceCheck(EXPECTED actual) {
      if (actual == null && _expected != null)
        fail(new NullPointerException("When comparing expected with actual, actual was null!"));
      else if (_expected == null && actual != null)
        fail(new NullPointerException("When comparing expected with actual, expected was null!"));
      else if (_expected == null && actual == null)
        return Clearance.ALL_ARE_NULL;

      return Clearance.ACCEPTED;
    }

    private void assertingEqualsTo(EXPECTED expected, EXPECTED actual) {
      if (Collection.class.isAssignableFrom(expected.getClass()))
        assertTrue(!_negate == ((Collection<?>) expected).stream().anyMatch(actual::equals) || (((Collection<?>) expected).isEmpty()));
      else if (!_negate)
        assertEquals(expected, actual);
      else
        assertNotEquals(expected, actual);
    }

    private void assertingNotEqualsTo(EXPECTED expected, EXPECTED actual) {
      if (Collection.class.isAssignableFrom(expected.getClass()))
        assertTrue(_negate == ((Collection<?>) expected).stream().anyMatch(actual::equals));
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

    private boolean handleEqualing(EXPECTED actual, Runnable action, boolean isEqualTo) {
      Boolean handled = handleNull(
          actual,
          () -> {
            if ((_notEqualToChecked && isEqualTo) || (_equalToChecked && !isEqualTo))
              throw new AssertionError("Already checked if " + _expected + " was " + (isEqualTo ? "" : "not") + " equal!");

            action.run();

            return true;
          },
          isEqualTo
      );

      return handled == null || handled;
    }

    private <SUPPLYMENT> SUPPLYMENT handleNull(EXPECTED actual, Supplier<SUPPLYMENT> action, boolean isEqualTo) {
      if (isEqualTo) {
        if (_expected != null && actual == null)
          fail(String.format("Expected %s but actual was null!", _expected));
        if (_expected == null && actual != null)
          fail("Expected null but actual was not null!");
      } else if (_expected == null && actual == null)
        fail("Expected and actual was not suppose to both be null!");

      return !(_expected == null && actual == null) ? action.get() : null;
    }

    public static class CollectiveChecker<EXPECTED extends Collection<EXPECTED_ELEMENT>, EXPECTED_ELEMENT> extends Checker<EXPECTED>
        implements CollectiveAssertionChecker<EXPECTED, EXPECTED_ELEMENT> {

      public CollectiveChecker(EXPECTED expected) {
        super(expected);
      }

      public static <EXPECTED extends Collection<EXPECTED_ELEMENT>, EXPECTED_ELEMENT>
          CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> of(EXPECTED expected) {
        return new CollectiveChecker<>(expected);
      }

      //region super class' methods
      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> is(EXPECTED actual) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.is(actual);
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> isNot(EXPECTED actual) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.isNot(actual);
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> is(Predicate<EXPECTED> assertion) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.is(assertion);
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> isTrue() {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.isTrue();
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> is(Supplier<EXPECTED> supplier) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.is(supplier);
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> contains(EXPECTED actual) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.contains(actual);
      }

      @Override
      public <W> CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> anyMatches(Predicate<W> assertion) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.anyMatches(assertion);
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> inCase(boolean condition, Predicate<EXPECTED> assertion) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.inCase(condition, assertion);
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> isNotNull() {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) super.isNotNull();
      }
      //endregion

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> isNotEmpty() {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) check(() -> assertFalse(_expected.isEmpty()));
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> isEmpty() {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) check(() -> assertTrue(_expected.isEmpty()));
      }

      @Override
      public CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> allMatches(Predicate<EXPECTED_ELEMENT> predication) {
        return (CollectiveChecker<EXPECTED, EXPECTED_ELEMENT>) check(() -> assertTrue(_expected.stream()
            .allMatch(predication)
        ));
      }
    }
  }
}
