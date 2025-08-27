package laustrup.bandwichpersistence.core.repositories.common.queries;

import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityConfigurations;

public abstract class CommonQueries {

    protected static DatabaseEntityConfigurations.Data fromField(Class<?> clazz, String fieldName) {
        try {
            return DatabaseEntityConfigurations.Data.of(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected static DatabaseEntityConfigurations.Data conjunction(
            DatabaseEntityConfigurations.Data target,
            DatabaseEntityConfigurations.Data common
    ) {
        return DatabaseEntityConfigurations.Data.of(target, common);
    }

    public abstract static class DatabasePropertiesCollection {}
}
