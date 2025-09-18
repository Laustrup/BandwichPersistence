package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.services.builders.OrganisationBuilder;
import laustrup.bandwichpersistence.core.services.builders.OrganisationEmployeeBuilder;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.DatabaseService.toDatabaseColumn;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations.Mode.PEEK;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.build;
import static laustrup.bandwichpersistence.items.TestItems.generateResultSet;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JDBCServiceTests extends BandwichTester {

    @ParameterizedTest
    @CsvSource(value = {
            "Table.Column" + _delimiter + "table.column",
            "Table.ColumnTest" + _delimiter + "table.column_test"
    }, delimiter = _delimiter)
    void canTranslateToDatabaseColumn(String field, String expectation) {
        mocked(() -> {
            arrange(() -> field);

            String actual = act(() -> toDatabaseColumn(field));

            assertEquals(expectation, actual);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"true", "false"})
    void canSetReference(boolean isBinary) {
        mocked(() -> {
            ResultSet resultSet = generateResultSet();
            AtomicReference<String> reference = isBinary ? null : arrange(new AtomicReference<>());
            AtomicReference<UUID> uuidReference = isBinary ? arrange(AtomicReference::new) : null;

            Consumer<AtomicReference<?>> action = atomicReference ->
                    act(() -> ResultSetService.set(
                            new Configurations(
                                    DatabaseField.of(new DatabaseField.Configuration(
                                            Organisation.class,
                                            isBinary ? Model.Fields._identity : Model.Fields._title
                                    )),
                                    resultSet,
                                    PEEK
                            ),
                            atomicReference
                    ));
            action.accept(isBinary ? uuidReference : reference);

            asserting((isBinary ? uuidReference : reference).get())
                    .isNotNull();
        });
    }

    @Test
    void canBuildMultiple() {
        mocked(() -> {
            ResultSet resultSet = generateResultSet();

            var actual = act(build(
                    resultSet,
                    () -> OrganisationBuilder.get_instance().combine(
                            new Seszt<>(),
                            OrganisationEmployeeBuilder.get_instance().build(resultSet)
                    )
            )).findFirst()
            .orElseThrow();

            asserting(actual)
                    .is(employees -> employees.size() > 1);
        });
    }
}