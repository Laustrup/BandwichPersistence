package laustrup.bandwichpersistence.core.persistence.queries;

import laustrup.bandwichpersistence.core.persistence.models.Query;

public class DatabaseLibraryQueries {

    public static Query createSchemaIfNotExists(String schema) {
        return new Query(
                String.format(
                        /*language=MySQL*/ "create database if not exists %s;",
                        schema
                )
        );
    }
}
