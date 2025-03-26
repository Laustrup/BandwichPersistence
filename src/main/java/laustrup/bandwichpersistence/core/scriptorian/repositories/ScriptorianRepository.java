package laustrup.bandwichpersistence.core.scriptorian.repositories;

import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.persistence.Query;
import laustrup.bandwichpersistence.core.persistence.queries.ScriptorianQueries;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.*;

public class ScriptorianRepository {

    public static void createDefaultSchemaIfNotExists() {
        create(ScriptorianQueries.createTableIfNotExists);
    }

    public static ResultSet findScriptoriesWithoutSuccess() {
        return read(ScriptorianQueries.findScriptoriesWithoutSuccess);
    }

    public static ResultSet findAllScriptories() {
        return read(ScriptorianQueries.findAllScriptories);
    }

    public static void putScriptory(Stream<DatabaseParameter> parameters) throws SQLException {
        execute(ScriptorianQueries.insertIntoScriptories, Action.MIGRATION, parameters);
    }

    public static void executeScript(String content) {
        try {
            execute(
                    new Query(content),
                    Action.MIGRATION
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
