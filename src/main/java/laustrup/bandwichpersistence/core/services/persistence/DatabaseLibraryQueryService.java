package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;
import laustrup.bandwichpersistence.core.persistence.DatabaseManager;
import laustrup.bandwichpersistence.core.persistence.queries.DatabaseLibraryQueries;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseLibraryQueryService {

    private static Logger _logger = Logger.getLogger(DatabaseLibraryQueryService.class.getSimpleName());

    public static void createSchemaIfNotExists(String schema) {
        try {
            DatabaseManager.execute(
                    DatabaseLibraryQueries.createSchemaIfNotExists(schema),
                    DatabaseManager.Action.ROOT_PATH,
                    DatabaseLibrary.get_rootURLPath(true)
            );
        } catch (SQLException e) {
            _logger.log(
                    Level.CONFIG,
                    String.format("""
                            Error when trying to create schema "%s" when setting up database.
                            """,
                            schema
                    ),
                    e
            );
            throw new RuntimeException(e);
        }
    }
}
