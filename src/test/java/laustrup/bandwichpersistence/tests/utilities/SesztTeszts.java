package laustrup.bandwichpersistence.tests.utilities;

import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.utilities.collections.sets.Seszt;

import org.junit.jupiter.api.Test;

public class SesztTeszts extends UtilityTester {

    @Test
    void canGetByIndex() {
        test(t -> {
            Artist expected = (Artist) arrange(e -> {
                _seszt = new Seszt<>(_adding);
                return _adding;
            });

            Artist actual = (Artist) act(e -> _seszt.get(0));

            asserting(expected,actual);

            actual = (Artist) act(e -> _seszt.Get(1));

            asserting(expected,actual);

            return end("canGetByIndex");
        });
    }

    @Test
    void canGetByKey() {
        test(t -> {
            Artist expected = (Artist) arrange(e -> {
                _seszt = new Seszt<>(_adding);
                return _adding;
            });

            Artist actual = (Artist) act(e -> _seszt.get(expected.toString()));

            asserting(expected,actual);

            return end("canGetByKey");
        });
    }

    @Test
    void canGet() {
        test(t -> {
            Artist expected = (Artist) arrange(e -> {
                _seszt = new Seszt<>(_adding);
                return _adding;
            });

            Artist actual = (Artist) act(e -> _seszt.get(expected));

            asserting(expected,actual);

            return end("canGet");
        });
    }

    @Test
    void canContain() {
        test(t -> {
            arrange(e -> _seszt = new Seszt<>(new Object[]{ _adding }));

            asserting((boolean) act(e -> _seszt.contains(_adding)));

            return end("canContain");
        });
    }

    @Test
    void canContainMultiple() {
        test(t -> {
            arrange(e -> _seszt = new Seszt<>(_addings));

            asserting((boolean) act(e -> _seszt.contains(_addings)));

            return end("canContainMultiple");
        });
    }

    @Test
    void canInitiateWithInputs() {
        test(t -> {
            Object[] expectations = (Object[]) arrange(e -> _addings);

            act(e -> _seszt = new Seszt<>(expectations));

            asserting(_seszt.contains(expectations));

            return end("canInitiateWithInputs");
        });
    }

    @Test
    void canAdd() {
        test(t -> {
            Object expected = arrange(e -> {
                _seszt = new Seszt<>();
                return _adding;
            });

            act(e -> _seszt.add(expected));

            asserting(_seszt.size() == 1);

            return end("canAdd");
        });
    }

    @Test
    void canAddUniques() {
        test(t -> {
            Artist[] expectations = (Artist[]) arrange(e -> {
                _seszt = new Seszt<>(new Artist[]{_items.get_tir(), _items.get_laust()});
                return new Artist[]{_items.get_tir(), _items.get_laust()};
            });

            act(e -> _seszt.add(expectations));

            asserting(_seszt.contains(expectations));

            return end("canAddUniques");
        });
    }

    @Test
    void canOnlyAddUnique() {
        test(t -> {
            Artist[] arrangement = (Artist[]) arrange(e -> {
                _seszt = new Seszt<>(new Artist[]{_items.get_carlos(), _items.get_carlos()});
                return new Artist[]{_items.get_carlos(), _items.get_carlos()};
            });

            act(e -> _seszt.add(arrangement));

            asserting(_seszt.size() == 1);

            return end("canNotAddSame");
        });
    }

    @Test
    void canClear() {
        test(t -> {
            arrange(e -> _seszt = new Seszt<>(_addings));
            boolean elementsIsAdded = !_seszt.isEmpty();

            act(e -> {
                _seszt.clear();
                return null;
            });

            asserting(_seszt.size() == 0 && elementsIsAdded);

            return end("canClear");
        });
    }

    @Test
    void canRemove() {
        test(t -> {
            arrange(e -> _seszt = new Seszt<>(_addings));

            act(e -> _seszt.remove(_addings[1]));

            asserting(!_seszt.contains(_addings[1]));

            return end("canRemove");
        });
    }

    @Test
    void canRemoveMultiple() {
        test(t -> {
            arrange(e -> _seszt = new Seszt<>(_addings));

            act(e -> _seszt.remove(new Object[]{_addings[0],_addings[2]}));

            asserting(!_seszt.contains(_addings[0]) && !_seszt.contains(_addings[2]));

            return end("canRemoveMultiple");
        });
    }
}
