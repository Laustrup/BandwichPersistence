package laustrup.bandwichpersistence.quality_assurance;

import java.util.function.Predicate;

public interface AssertionChecker<E> {

    void isEqualTo(E actual);

    void isNotEqualTo(E actual);

    void is(Predicate<E> predicate);

    void isNotNull();
}
