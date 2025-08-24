package laustrup.bandwichpersistence.core.repositories.common.queries;

import laustrup.bandwichpersistence.core.models.DatabaseTable;

import static laustrup.bandwichpersistence.core.services.DatabaseTableAnnotationService.getDatabaseTableProperties;

public abstract class CommonQueries {

    protected static DatabaseTable.Properties fromField(Class<?> clazz, String fieldName) {
        try {
            return getDatabaseTableProperties(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected static DatabaseTable.Properties fromField(Class<?> child, Class<?> inherited, String fieldName) {
        try {
            DatabaseTable.Properties fieldProperties = getDatabaseTableProperties(inherited.getDeclaredField(fieldName));
            return new DatabaseTable.Properties(
                    child,
                    fieldProperties.get_field().orElse(null),
                    fieldProperties.get_title(),
                    fieldProperties.get_idReference()
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected static DatabaseTable.Properties conjunction(DatabaseTable.Properties target, DatabaseTable.Properties common) {
        return DatabaseTable.Properties.of(target, common);
    }

    public abstract static class DatabasePropertiesCollection {}
}
