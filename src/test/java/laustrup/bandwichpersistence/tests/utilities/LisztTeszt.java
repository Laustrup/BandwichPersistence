package laustrup.bandwichpersistence.tests.utilities;

import laustrup.bandwichpersistence.tests.Tester;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.utilities.collections.Liszt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LisztTeszt extends Tester<Object, Object> {

    /** The Liszt that will be used in the tests for testing. */
    private Liszt<Object> _liszt;

    @ParameterizedTest
    @CsvSource(value = {"true", "false"}, delimiter = '|')
    void constructorTest(boolean isEmptyDataTemplate) {
        test(t -> {
            arrange();

            if (isEmptyDataTemplate) {
                act(e -> _liszt = new Liszt<>());

                asserting(_liszt.isEmpty());
            }
            else {
                act(e -> _liszt = new Liszt<>(new Object[]{true,false}));

                asserting(true, (_liszt.get(1)));
                asserting(false, (_liszt.get(2)));
            }

            return true;
        });
    }

    @Test
    void canAddSingleElement() {
        test(t -> {
            Object expected = arrange(e -> {
                _liszt = new Liszt<>();
                return 1;
            });

            act(expected, e -> _liszt.add(e));

            asserting(expected, _liszt.get(1));

            return true;
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"true","false"})
    void canAddAnArray(boolean constructorWithArgument) {
        test(t -> {
            arrange(constructorWithArgument, e -> {
                _liszt = (Boolean) e ?
                new Liszt<>(_items.get_bands())
                : new Liszt<>();
                return _liszt;
            });

            act(constructorWithArgument, e -> {
                if (!((Boolean) e))
                    _liszt.add(_items.get_bands());
                return _liszt;
            });

            for (Band band : _items.get_bands())
                asserting(band, _liszt.get(band.toString()));

            for (int i = 1; i <= _items.get_bands().length; i++)
                asserting(_items.get_bands()[i-1], _liszt.get(i));

            return true;
        });

    }

    @Test
    void canRemove() {
        test(t -> {
            User expected = (User) arrange(e -> {
                User user = _items.generateUser();
                _liszt = new Liszt<>(new Object[]{user});
                return user;
            });

            act(expected, e -> _liszt.remove(e));

            asserting(_liszt.isEmpty());

            return true;
        });
    }

    @Test
    void canRemoveMultiple() {
        test(t -> {
            Band[] expectations = (Band[]) arrange(e -> {
                Band[] bands = _items.get_bands();
                _liszt = new Liszt<>(bands);
                return bands;
            });

            act(expectations,element -> _liszt.remove(element));

            asserting(_liszt.isEmpty());

            return true;
        });
    }

    @Test
    void canSet() {
        test(t -> {
            Band[] expectations = (Band[]) arrange(e -> {
                Band[] bands = _items.get_bands();
                _liszt = new Liszt<>(bands);
                bands[4].set_runner("This is a new runner");
                return bands;
            });

            act(expectations[4],e -> _liszt.set(3,e));

            asserting(((Band) _liszt.get(3)).get_runner(),expectations[4].get_runner());
            asserting(((Band) _liszt.get(expectations[4].toString())).get_runner(),expectations[4].get_runner());

            return true;
        });
    }
}