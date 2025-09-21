package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Album;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class DatabaseDefinitionServiceTests extends BandwichTester {

    @ParameterizedTest
    @ValueSource(classes = {Organisation.class, Album.Media.class})
    void canGetTableTitle(Class<?> clazz) {
        test(() -> {
            String expected = arrange(switch (clazz.getSimpleName()) {
                case "Organisation" -> "organisations";
                case "Media" -> "album_media";
                default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
            });

            String actual = act(getTableTitle(clazz));

            asserting(expected)
                    .is(actual);
        });
    }

    @ParameterizedTest
    @ValueSource(classes = {Organisation.class})
    void canGetIdReference(Class<?> clazz) {
        test(() -> {
            String expected = arrange(switch (clazz.getSimpleName()) {
                case "Organisation" -> "organisation_id";
                default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
            });

            String actual = act(getIdReference(clazz));

            asserting(expected)
                    .is(actual);
        });
    }

    @ParameterizedTest
    @ValueSource(classes = {Organisation.class})
    //TODO Make more detailed
    void canGetDatabaseColumns(Class<?> clazz) {
        test(() -> {
            Seszt<Table.Column> expectations = arrange(switch (clazz.getSimpleName()) {
                case "OrganisationTitle" -> new Seszt<Table.Column>();
                default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
            });

            Seszt<Table.Column> actual = act(getTableColumns(clazz));

            asserting(expectations)
                    .is(actual);
        });
    }
}