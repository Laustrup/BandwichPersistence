package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.BandwichTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JDBCServiceTests extends BandwichTester {

    @ParameterizedTest
    @CsvSource(value = {
            "Table.Column",
            "Table.ColumnTest"
    }, delimiter = _delimiter)
    void canTranslateToDatabaseColumn(String field, String expectation) {
        test(() -> {
            arrange(() -> field);

            String actual = act(() -> toDatabaseColumn(field));

            assertEquals(expectation, actual);
        });
    }
}