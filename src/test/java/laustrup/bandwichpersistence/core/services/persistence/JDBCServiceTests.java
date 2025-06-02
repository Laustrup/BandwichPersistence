package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.persistence.Field;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static laustrup.bandwichpersistence.TestItems.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.DatabaseService.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;
import static org.junit.jupiter.api.Assertions.*;

class JDBCServiceTests extends BandwichTester {

    private <T> T arrangeJDBCService(ResultSet resultSet, Supplier<T> supplier) {
        JDBCService.set_resultSet(resultSet);
        return arrange(supplier);
    }

    private void arrangeJDBCService(ResultSet resultSet) {
        JDBCService.set_resultSet(resultSet);
        arrange();
    }

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

    @ParameterizedTest
    @CsvSource(value = {"true", "false"})
    void canSetReference(boolean isBinary) {
        test(() -> {
            Supplier<Object> supplyArrangement = () -> isBinary ? UUID.randomUUID() : (String) null;
            AtomicReference<Object> instance = arrangeJDBCService(
                    generateResultSet(),
                    () -> new AtomicReference<>(supplyArrangement.get())
            );

            act(() -> set(
                    instance,
                    Field.of(
                            Organisation.TABLE.title(),
                            isBinary ? "id" : "title"
                    )
            ));

            asserting(supplyArrangement.get())
                    .isNotEqualTo(instance);
        });
    }
}