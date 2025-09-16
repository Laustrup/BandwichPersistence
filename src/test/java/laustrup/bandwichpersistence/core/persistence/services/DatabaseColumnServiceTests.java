package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.DatabaseField.Column;
import laustrup.bandwichpersistence.core.persistence.DatabaseField.Table;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Member;
import java.util.Map;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.get_columns;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class DatabaseColumnServiceTests extends BandwichTester {

    @ParameterizedTest
    @ValueSource(classes = {Organisation.class})
    void canGetColumns(Class<?> clazz) {
        test(() -> {
            Map<? extends Member, DatabaseField> expected = arrange(switch (clazz.getSimpleName()) {
                case  "Organisation" -> {
                    try {
                        yield Map.ofEntries(
                                Map.entry(
                                        Model.class.getDeclaredField(Model.Fields._identity),
                                        new DatabaseField(new Table(Organisation.class), new Column("id", "id"))
                                )
                        );
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
            });

            Map<? extends Member, DatabaseField> actual = act(get_columns(clazz));

            expected.forEach((key, value) -> {
                asserting(value.equals(actual.get(key)));
            });
        });
    }
}