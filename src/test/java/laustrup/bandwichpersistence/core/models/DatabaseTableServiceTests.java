package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.BandwichTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseTableService.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

public class DatabaseTableServiceTests extends BandwichTester {

  @ParameterizedTest
  @CsvSource(value = {
      "organisation_employees" + _delimiter + "chat_rooms",
      "countries" + _delimiter + "chat_rooms",
      "addresses" + _delimiter + "chat_rooms"
  }, delimiter = _delimiter)
  void canDefineTitle(String target, String common) {
    test(() -> {
      String expected = arrange(() -> switch (target) {
        case "organisation_employees" -> "organisation_employee_chat_rooms";
        case "countries" -> "country_chat_rooms";
        case "addresses" -> "address_chat_rooms";
        default -> throw new IllegalStateException("Unexpected value for define title test: " + target);
      });

      String actual = act(() -> defineTableTitle(target, common));

      asserting(expected)
          .is(actual);
    });
  }

  @ParameterizedTest
  @CsvSource(value = {
        "table" + _delimiter + "tables",
        "try" + _delimiter + "tries",
        "class" + _delimiter + "classes"
    },
    delimiter = _delimiter
  )
  void canPluralToSingular(String singular, String plural) {
    test(() -> {
      String actual = act(() -> pluralToSingular(plural));

      asserting(singular)
          .is(actual);
    });
  }

  @ParameterizedTest
  @CsvSource(value = {
        "table" + _delimiter + "tables",
        "try" + _delimiter + "tries",
        "class" + _delimiter + "classes"
    },
    delimiter = _delimiter
  )
  void canSingularToPlural(String singular, String plural) {
    test(() -> {
      String actual = act(() -> singularToPlural(singular));

      asserting(plural)
          .is(actual);
    });
  }
}
