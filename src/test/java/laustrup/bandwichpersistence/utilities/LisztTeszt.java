package laustrup.bandwichpersistence.utilities;

import laustrup.bandwichpersistence.JTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LisztTeszt extends JTest {

    private List<Object> liszt;

    @ParameterizedTest
    @CsvSource(value = {"true", "false"}, delimiter = '|')
    public void constructorTest(boolean isEmptyDataTemplate) {
        // ACT
        if (isEmptyDataTemplate) {
            liszt = new Liszt<>();

            // Assert
            assertTrue(liszt.isEmpty());
        }
        else {
            liszt = new Liszt(new Object[]{true,false});

            // Assert
            assertEquals(true,liszt.get(1));
            assertEquals(false,liszt.get(2));
        }

    }

    @Test
    public void addSingleTest() {
        // Arrange
        liszt = new Liszt<>();
        Object element = 666;

        // Act
        liszt.add(element.toString());

        // Assert
        assertEquals(element.toString(),liszt.stream().findFirst().toString());
    }

    @ParameterizedTest
    @CsvSource(value = {}, delimiter = '|')
    public void addArrayTest(Object[] elements) {
        // Arrange
        liszt = new Liszt<>();

        // Act
        liszt.add(elements);

        // Assert
        for (int i = 0; i < elements.length; i++) { assertEquals(elements[i],liszt.get(1)); }
    }
}