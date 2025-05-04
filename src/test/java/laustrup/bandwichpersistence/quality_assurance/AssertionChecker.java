package laustrup.bandwichpersistence.quality_assurance;

public interface AssertionChecker<E> {

    void isEqualTo(E actual);

    void isNotEqualTo(E actual);
}
