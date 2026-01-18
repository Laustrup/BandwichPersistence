package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.ClassFieldService.getField;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.DatabaseService.toDatabaseColumn;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations.Mode.PEEK;
import static laustrup.bandwichpersistence.items.ResultSetTestGenerator.getResultSetGenerator;
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
      ResultSet resultSet = getResultSetGenerator().generate("select * from organisations _organisations");
      AtomicReference<String> reference = isBinary ? null : arrange(new AtomicReference<>());
      AtomicReference<UUID> uuidReference = isBinary ? arrange(AtomicReference::new) : null;
      Function<AtomicReference<?>, AtomicReference<?>> acting = atomicReference -> {
        String fieldName = isBinary
            ? Model.Fields._identity
            : Organisation.Fields._title;

        return ResultSetService.set(
            new Configurations(
                DatabaseField.of(
                    Organisation.class,
                    getField(Organisation.class, fieldName)
                ),
                resultSet,
                PEEK
            ),
            atomicReference
        );
      };

      Consumer<AtomicReference<?>> action = atomicReference ->
          act(acting.apply(atomicReference));
      action.accept(isBinary ? uuidReference : reference);

      asserting((isBinary ? uuidReference : reference).get())
          .isNotNull();
    });
  }
}