package laustrup.bandwichpersistence.quality_assurance;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface AssertionChecker<EXPECTED> {

  AssertionChecker<EXPECTED> is(EXPECTED actual);

  AssertionChecker<EXPECTED> isNot(EXPECTED actual);

  AssertionChecker<EXPECTED> is(Predicate<EXPECTED> assertion);

  AssertionChecker<EXPECTED> isTrue();

  AssertionChecker<EXPECTED> is(Supplier<EXPECTED> supplier);

  AssertionChecker<EXPECTED> contains(EXPECTED actual);

  <W> AssertionChecker<EXPECTED> anyMatches(Predicate<W> assertion);

  AssertionChecker<EXPECTED> inCase(boolean condition, Predicate<EXPECTED> assertion);

  AssertionChecker<EXPECTED> isNotNull();

  AssertionChecker<EXPECTED> compare(EXPECTED actual);

  interface CollectiveAssertionChecker<EXPECTED extends Collection<EXPECTED_ELEMENT>, EXPECTED_ELEMENT> extends AssertionChecker<EXPECTED> {

    CollectiveAssertionChecker<EXPECTED, EXPECTED_ELEMENT> isNotEmpty();

    CollectiveAssertionChecker<EXPECTED, EXPECTED_ELEMENT> isEmpty();

    Asserter.Checker.CollectiveChecker<EXPECTED, EXPECTED_ELEMENT> allMatches(Predicate<EXPECTED_ELEMENT> predication);
  }

  enum Clearance {
    ALL_ARE_NULL,
    ALL_ARE_EMPTY,
    ACCEPTED;

    static class Exception extends RuntimeException {

      private Exception(String message) {
        super(message);
      }

      public static Exception isNotAccepted(String detail) {
        return new Exception("The asserter clearance was not accepted, since:\n\n" + detail);
      }
    }
  }
}
