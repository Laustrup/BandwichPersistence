package laustrup.bandwichpersistence.core.scriptorian.repositories;

import laustrup.bandwichpersistence.core.persistence.DatabaseManager;
import laustrup.bandwichpersistence.core.persistence.DatabaseParameter;
import laustrup.bandwichpersistence.core.persistence.Query;
import laustrup.bandwichpersistence.core.persistence.queries.ScriptorianQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ScriptorianRepository {

    public static void createDefaultSchemaIfNotExists() {
        DatabaseManager.create(ScriptorianQueries.createTableIfNotExists);
    }

    public static ResultSet findScriptoriesWithoutSuccess() {
        return DatabaseManager.read(ScriptorianQueries.findScriptoriesWithoutSuccess);
    }

    public static ResultSet findAllScriptories() {
        return DatabaseManager.read(ScriptorianQueries.findAllScriptories);
    }

    public static void putScriptory(List<DatabaseParameter> parameters) {
        DatabaseManager.create(ScriptorianQueries.insertIntoScriptories, parameters);
    }

    public static void executeScript(String content) {
        try {
            DatabaseManager.execute(
                    new Query(content),
                    DatabaseManager.Action.CUD
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
