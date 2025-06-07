package laustrup.bandwichpersistence.quality_assurance;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface AssertionChecker<E> {

    AssertionChecker<E> isEqualTo(E actual);

    AssertionChecker<E> isNotEqualTo(E actual);

    AssertionChecker<E> is(Predicate<E> assertion);

    AssertionChecker<E> isTrue();

    AssertionChecker<E> is(Supplier<E> supplier);

    AssertionChecker<E> contains(E actual);

    <W> AssertionChecker<E> anyMatches(Predicate<W> assertion);

    AssertionChecker<E> inCase(boolean condition, Predicate<E> assertion);

    AssertionChecker<E> isNotNull();
}
