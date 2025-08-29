package laustrup.bandwichpersistence.core.repositories.common.queries;

import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityData;

public abstract class CommonQueries {

    protected static DatabaseEntityData conjunction(
            DatabaseEntityData target,
            DatabaseEntityData common
    ) {
        return DatabaseEntityData.of(target, common);
    }

    public abstract static class DatabasePropertiesCollection {}
}
