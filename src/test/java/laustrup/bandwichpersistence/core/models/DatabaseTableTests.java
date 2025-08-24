package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.BandwichTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

public class DatabaseTableTests extends BandwichTester {

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

            String actual = act(() -> DatabaseTable.Properties.defineTitle(target, common));

            asserting(expected)
                    .is(actual);
        });
    }
}
