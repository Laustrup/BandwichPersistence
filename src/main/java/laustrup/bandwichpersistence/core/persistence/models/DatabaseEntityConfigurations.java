package laustrup.bandwichpersistence.core.persistence.models;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Map;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.getColumns;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineIdReference;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineTitle;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.get_tableTitle;

public interface DatabaseEntityConfigurations {

    default String explicitIdReference() {
        return "";
    }

    default Map<Field, String> get_explicits() {
        return null;
    }

    default Map<Field, String> get_exclusions() {
        return null;
    }

    default Map<Field, String> get_additions() {
        return null;
    }

    // DO NOT DELETE THIS METHOD, IT IS BEING USED!
    default DatabaseEntityConfigurations get_databaseEntityConfigurations() {
        return this;
    }

    @Getter
    class Data {

        private final String _title;
        private final String _idReference;
        private final Map<Field, String> _columns;

        public Data(DatabaseEntityConfigurations configurations, String title) {
            _title = title;
            _idReference = defineIdReference(title, defineIdReference(title, configurations.explicitIdReference()));
            _columns = getColumns(configurations);
        }

        public static Data of(Field field) {
            String title = get_tableTitle(field);

            return new Data(new DTODatabaseConfigurations(defineIdReference(title)), title);
        }

        public static Data of(DatabaseEntityConfigurations.Data target, DatabaseEntityConfigurations.Data common) {
            String title = defineTitle(target.get_title(), common.get_title());

            return new Data(
                    new DTODatabaseConfigurations(defineIdReference(title)),
                    defineTitle(target.get_title(), common.get_title())
            );
        }
    }
}
