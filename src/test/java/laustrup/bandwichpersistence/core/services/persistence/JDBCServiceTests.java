package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.BandwichTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;
import static org.junit.jupiter.api.Assertions.*;

class JDBCServiceTests extends BandwichTester {

    @ParameterizedTest
    @CsvSource(value = {
            "Table.Column" + _delimiter + "table.column",
            "Table.ColumnTest" + _delimiter + "table.column_test"
    }, delimiter = _delimiter)
    void canTranslateToDatabaseColumn(String field, String expectation) {
        test(() -> {
            arrange(() -> field);

            String actual = act(() -> toDatabaseColumn(field));

            assertEquals(expectation, actual);
        });
    }
}