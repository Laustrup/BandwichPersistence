package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Album;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseRow;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.services.DatabaseEntityDataService.*;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class DatabaseEntityDataServiceTests extends BandwichTester {

    @ParameterizedTest
    @ValueSource(classes = {Organisation.class, Album.Media.class})
    void canGetTableTitle(Class<?> clazz) {
        test(() -> {
            String expected = arrange(switch (clazz.getSimpleName()) {
                case "Organisation" -> "organisations";
                case "Media" -> "album_media";
                default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
            });

            String actual = act(get_tableTitle(clazz));

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

            String actual = act(get_idReference(clazz));

            asserting(expected)
                    .is(actual);
        });
    }

    @ParameterizedTest
    @ValueSource(classes = {Organisation.class})
    //TODO Make more detailed
    void canGetDatabaseRows(Class<?> clazz) {
        test(() -> {
            Seszt<DatabaseRow> expectations = arrange(switch (clazz.getSimpleName()) {
                case "Organisation" -> new Seszt<>(Stream.of());
                default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
            });

            Seszt<DatabaseRow> actual = act(get_databaseRows(clazz));

            asserting(expectations)
                    .is(actual);
        });
    }
}