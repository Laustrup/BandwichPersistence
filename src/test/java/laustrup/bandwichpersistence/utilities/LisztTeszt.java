package laustrup.bandwichpersistence.utilities;

import laustrup.bandwichpersistence.JTest;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LisztTeszt extends JTest {

    private Liszt<Object> liszt;

    @ParameterizedTest
    @CsvSource(value = {"true", "false"}, delimiter = '|')
    public void constructorTest(boolean isEmptyDataTemplate) {
        // ACT
        if (isEmptyDataTemplate) {
            liszt = new Liszt<>();
            calculatePerformance();

            // Assert
            assertTrue(liszt.isEmpty());
        }
        else {
            liszt = new Liszt(new Object[]{true,false});
            calculatePerformance();

            // Assert
            assertEquals(true,liszt.get(1));
            assertEquals(false,liszt.get(2));
        }

    }

    @Test
    public void canAddSingleElement() {
        // Arrange
        liszt = new Liszt<>();
        Object element = 666;

        // Act
        begin();
        liszt.add(element);

        // Assert
        assertEquals(element,liszt.get(1));
    }

    @ParameterizedTest
    @CsvSource(value = {"true","false"}, delimiter = '|')
    public void canAddAnArray(boolean constructorWithArgument) {
        // Arrange
        if (constructorWithArgument) begin();
        liszt = constructorWithArgument ?
                new Liszt<>(_items.get_bands())
                : new Liszt<>();

        // Act
        if (!constructorWithArgument) {
            begin();
            liszt.add(_items.get_bands());
        }
        calculatePerformance();

        // Assert
        for (Band band : _items.get_bands())
            assertEquals(band,liszt.get(band.toString()));

        for (int i = 1; i <= _items.get_bands().length; i++)
            assertEquals(_items.get_bands()[i-1], liszt.get(i));
    }
}