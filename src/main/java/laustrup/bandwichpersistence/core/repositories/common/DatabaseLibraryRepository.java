package laustrup.bandwichpersistence.core.repositories.common;

import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;
import laustrup.bandwichpersistence.core.persistence.DatabaseManager;
import laustrup.bandwichpersistence.core.persistence.queries.DatabaseLibraryQueries;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DatabaseLibraryRepository {

  public static void createSchemaIfNotExists(String schema) {
    try {
      DatabaseManager.execute(
          DatabaseLibraryQueries.createSchemaIfNotExists(schema),
          DatabaseManager.Action.ROOT_PATH,
          DatabaseLibrary.get_rootConnectionString(true)
      );
    } catch (SQLException e) {
      log.info("Error when trying to create schema \"{}\" when setting up database.",
          schema,
          e
      );
      throw new RuntimeException(e);
    }
  }
}
