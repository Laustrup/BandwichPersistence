package laustrup.bandwichpersistence.utilities;

import laustrup.bandwichpersistence.Tester;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;

import laustrup.bandwichpersistence.utilities.collections.Liszt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LisztTeszt extends Tester {

    private Liszt<Object> _liszt;

    @ParameterizedTest
    @CsvSource(value = {"true", "false"}, delimiter = '|')
    public void constructorTest(boolean isEmptyDataTemplate) {
        // ACT
        if (isEmptyDataTemplate) {
            begin();
            _liszt = new Liszt<>();
            calculatePerformance();

            // ASSERT
            assertTrue(_liszt.isEmpty());
        }
        else {
            _liszt = new Liszt(new Object[]{true,false});
            calculatePerformance();

            // ASSERT
            assertEquals(true, _liszt.get(1));
            assertEquals(false, _liszt.get(2));
        }

    }

    @Test
    public void canAddSingleElement() {
        // ARRANGE
        _liszt = new Liszt<>();
        Object element = 666;

        // ACT
        begin();
        _liszt.add(element);

        // ASSERT
        assertEquals(element, _liszt.get(1));
    }

    @ParameterizedTest
    @CsvSource(value = {"true","false"})
    public void canAddAnArray(boolean constructorWithArgument) {
        // ARRANGE
        if (constructorWithArgument) begin();
        _liszt = constructorWithArgument ?
                new Liszt<>(_items.get_bands())
                : new Liszt<>();

        // ACT
        if (!constructorWithArgument) {
            begin();
            _liszt.add(_items.get_bands());
        }
        calculatePerformance();

        // ASSERT
        for (Band band : _items.get_bands())
            assertEquals(band, _liszt.get(band.toString()));

        for (int i = 1; i <= _items.get_bands().length; i++)
            assertEquals(_items.get_bands()[i-1], _liszt.get(i));
    }

    @Test
    public void canRemove() {
        //ARRANGE
        User user = _items.generateUser();
        _liszt = new Liszt<>(new Object[]{user});

        // ACT
        begin();
        _liszt.remove(user);
        calculatePerformance();

        // ASSERT
        assertFalse(_liszt.contains(user));
        assertTrue(_liszt.isEmpty());
    }

    @Test
    public void canRemoveMultiple() {
        // ARRANGE
        Band[] bands = _items.get_bands();
        _liszt = new Liszt<>(bands);

        // ACT
        begin();
        _liszt.remove(bands);
        calculatePerformance();

        // ASSERT
        assertTrue(_liszt.isEmpty());
    }

    /*
    @Test
    public void canReplaceByIndex() {
        do {
            // ARRANGE
            Band[] bands = _items.get_bands();
            int index = _random.nextInt(bands.length);
            _liszt = new Liszt<>(bands);

            Band original = bands[index];
            Band replacement = new Band(original.get_primaryId(), original.get_username(),
                    original.get_description(), original.get_contactInfo(),
                    original.get_albums(), original.get_ratings(), original.get_events(), original.get_gigs(),
                    original.get_chatRooms(), original.get_subscription(), original.get_bulletins(),
                    original.get_members(), original.get_runner(),
                    original.get_fans(), original.get_idols(), original.get_timestamp());
            replacement.set_runner("This is a replacement!");

            try {
                // ACT
                begin();
                _liszt.replace(replacement, index + 1);
                calculatePerformance();

                // ASSERT
                assertEquals(bands.length, _liszt.size());
                assertEquals(replacement, _liszt.get(replacement.toString()));
                assertFalse(_liszt.contains(original.toString()) || _liszt.contains(original.hashCode()));

                break;
            } catch (ClassNotFoundException e) {
                Printer.get_instance().print("Liszt can't find the object to replace...", e);
            }
        } while (true);
    }


    @Test
    public void canReplaceByKey() {
        do {
            // ARRANGE
            Band[] bands = _items.get_bands();
            _liszt = new Liszt<>(bands);

            Band original = bands[_random.nextInt(bands.length)];
            Band replacement = new Band(original.get_primaryId(), original.get_username(),
                    original.get_description(), original.get_contactInfo(),
                    original.get_albums(), original.get_ratings(), original.get_events(), original.get_gigs(),
                    original.get_chatRooms(), original.get_subscription(), original.get_bulletins(),
                    original.get_members(), original.get_runner(),
                    original.get_fans(), original.get_idols(), original.get_timestamp());
            replacement.set_runner("This is a replacement!");

            try {
                // ACT
                begin();
                _liszt.replace(replacement,original);
                calculatePerformance();

                // ASSERT
                assertEquals(bands.length, _liszt.size());
                assertTrue(_liszt.contains(replacement.toString()) || _liszt.contains(replacement.toString()));
                assertFalse(_liszt.contains(original.toString()) || _liszt.contains(original.toString()));

                break;
            } catch (ClassNotFoundException e) {
                Printer.get_instance().print("Liszt can't find the object to replace...", e);
            }
        } while (true);
    }

     */

    @Test
    void canSet() {
        //ARRANGE
        Band[] bands = _items.get_bands();
        _liszt = new Liszt<>(bands);
        bands[4].set_runner("This is a new runner");

        //ACT
        begin();
        _liszt.set(3,bands[4]);
        calculatePerformance();

        //ASSERT
        assertEquals(((Band) _liszt.get(3)).get_runner(),bands[4].get_runner());
        assertEquals(((Band) _liszt.get(bands[4].toString())).get_runner(),bands[4].get_runner());
    }
}