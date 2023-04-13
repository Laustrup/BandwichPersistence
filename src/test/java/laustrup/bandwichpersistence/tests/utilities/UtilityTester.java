package laustrup.bandwichpersistence.tests.utilities;

import laustrup.bandwichpersistence.tests.Tester;
import laustrup.bandwichpersistence.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.utilities.collections.sets.Seszt;

import org.junit.jupiter.api.AfterEach;

/**
 * A testing type class, that extends Tester class with both types as Object.
 * Contains collections, that can be used for test purposes.
 */
public class UtilityTester extends Tester<Object, Object> {

    /**
     * A Seszt that can be used for testing purposes,
     * Will be reset after each test.
     */
    protected Seszt<Object> _seszt;

    /**
     * A Liszt that can be used for testing purposes,
     * Will be reset after each test.
     */
    protected Liszt<Object> _liszt;

    @AfterEach
    void afterEach() {
        _seszt = new Seszt<>();
        _liszt = new Liszt<>();
    }
}
