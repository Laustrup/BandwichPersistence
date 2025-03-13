package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.ProgramInitializer;
import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseManager {

    private static final Logger _logger = Logger.getLogger(ProgramInitializer.class.getSimpleName());

    public static ResultSet results(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.getResultSet();
        } catch (SQLException exception) {
            _logger.warning("Could not get result set\n" + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public static ResultSet read(Query query) {
        PreparedStatement preparedStatement = handle(query, Action.READ);

        return preparedStatement != null ? results(preparedStatement) : null;
    }

    public static PreparedStatement read(Query query, Stream<DatabaseParameter> databaseParameters) {
        return handle(query, Action.READ, databaseParameters);
    }

    public static PreparedStatement upsert(Query query) {
        return upsert(query, new ArrayList<>().stream().map(datum -> (DatabaseParameter) datum));
    }

    public static PreparedStatement upsert(Query query, Stream<DatabaseParameter> parameters) {
        return handle(query, Action.CUD, parameters);
    }

    public static PreparedStatement create(Query query) {
        return handle(query, Action.CREATE);
    }

    public static PreparedStatement create(Query query, Stream<DatabaseParameter> parameters) {
        return handle(query, Action.CREATE, parameters);
    }

    private static PreparedStatement handle(Query query, Action action) {
        try {
            return execute(query, action);
        } catch (SQLException e) {
            return null;
        }
    }

    private static PreparedStatement handle(
            Query query,
            Action action,
            Stream<DatabaseParameter> parameters
    ) {
        try {
            return execute(query, action, parameters);
        } catch (SQLException e) {
            return null;
        }
    }

    public static PreparedStatement execute(Query query, Action action) throws SQLException {
        return execute(query, action, Objects.requireNonNull(DatabaseLibrary.get_urlPath()));
    }

    public static PreparedStatement execute(Query query, Action action, String url) throws SQLException {
        return execute(query, action, new ArrayList<>().stream().map(datum -> (DatabaseParameter) datum), url);
    }

    public static PreparedStatement execute(Query query, Action action, Stream<DatabaseParameter> parameters) throws SQLException {
        return execute(query, action, parameters, Objects.requireNonNull(DatabaseLibrary.get_urlPath()));
    }

    public static PreparedStatement execute(Query query, Action action, DatabaseParameter parameter) throws SQLException {
        return execute(query, action, parameter, Objects.requireNonNull(DatabaseLibrary.get_urlPath()));
    }

    public static PreparedStatement execute(
            Query query,
            Action action,
            DatabaseParameter parameter,
            String url
    ) throws SQLException {
        return execute(query, action, Stream.of(parameter), url);
    }

    public static PreparedStatement execute(
            Query query,
            Action action,
            Stream<DatabaseParameter> parameters,
            String url
    ) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = prepareStatement(
                    query,
                    action,
                    parameters,
                    url
            );

            switch (action) {
                case READ -> preparedStatement.executeQuery();
                case CREATE, UPDATE, DELETE, CUD -> preparedStatement.executeUpdate();
                default -> preparedStatement.execute();
            }
        } catch (Exception exception) {
            _logger.log(
                    Level.WARNING,
                    String.format(
                            """
                            Couldn't execute query:
                            %s
                            
                            With action: %s
                            """,
                            preparedStatement == null
                                    ? query.get_script()
                                    : preparedStatement.toString(),
                            action
                    ),
                    exception
            );

            throw exception;
        }

        return preparedStatement;
    }

    private static String prepareTransaction(String sql) {
        boolean insertSemicolon = !sql
                .replace(" ", "")
                .replace("\n", "")
                .endsWith(";");

        return /*language=mysql*/
                "\nstart transaction;\n\n" +
                        sql +
                        (insertSemicolon ? ";" : "") +
                        "\ncommit;";
    }

    private static PreparedStatement prepareStatement(
            Query query,
            Action action, Stream<DatabaseParameter> parameters,
            String url
    ) {
        PreparedStatement preparedStatement;

        try {
            int keyIndex = 0,
                parameterIndex = 1;

            Map<String, DatabaseParameter> parametersByKey = parameters
                    .collect(Collectors.toMap(DatabaseParameter::get_key, parameter -> parameter));
            String script = prepareScript(query.get_script(), action);

            for (char character : script.toCharArray()) {
                if (character == Query.get_identifier()) {
                    String key = script.substring(
                            keyIndex,
                            script.indexOf(Query.get_endExpression(), keyIndex)
                    ) + Query.get_endExpression();
                    DatabaseParameter parameter = parametersByKey.get(key);

                    if (parameter == null) {
                        String message = "Unknown parameter: " + key;
                        _logger.log(Level.WARNING, message);
                        throw new IllegalArgumentException(message);
                    }

                    parameter.get_indexes().add(parameterIndex);
                    parameterIndex++;
                }

                keyIndex++;
            }

            Map<Integer, DatabaseParameter> databaseParametersByIndex = new HashMap<>();
            for (DatabaseParameter parameter : parametersByKey.values()) {
                script = script.replace(parameter.get_key(), "?");
                for (Integer databaseParameterIndex : parameter.get_indexes())
                    databaseParametersByIndex.put(databaseParameterIndex, parameter);
            }

            long inputCount = script.chars().filter(character -> character == '?').count();
            if (databaseParametersByIndex.size() != inputCount) {
                throw new IllegalArgumentException(String.format("""
                        Issue when preparing query:
                        %n%s
                        
                        Amount of inputs are %s and parameters to input are %s.
                        """,
                        script,
                        inputCount,
                        databaseParametersByIndex.size()
                ));
            }

            preparedStatement = Objects.requireNonNull(DatabaseGate.getConnection(url)).prepareStatement(
                    script,
                    Statement.RETURN_GENERATED_KEYS
            );

            for (Integer key : databaseParametersByIndex.keySet()) {
                DatabaseParameter parameter = databaseParametersByIndex.get(key);

                if (parameter.get_type() != null)
                    preparedStatement.setObject(key, parameter.get_value(), parameter.get_type());
                else
                    preparedStatement.setObject(key, parameter.get_value());
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return preparedStatement;
    }

    private static String prepareScript(String script, Action action) {
        return action != Action.READ
                ? prepareTransaction(script)
                : script;
    }

    public enum Action {
        /**
         * Used when only row(s) are inserted.
         * Executes the executeUpdate().
         */
        CREATE,
        /**
         * Used when only reading and no mutation is meant to happen.
         * Executes the executeQuery().
         */
        READ,
        /**
         * Used when only row(s) are updated.
         * Executes the executeUpdate().
         */
        UPDATE,
        /**
         * Used when only row(s) are removed.
         * Executes the executeUpdate().
         */
        DELETE,
        /**
         * Used when row(s) are either inserted, updated or deleted.
         * Executes the executeUpdate().
         */
        CUD,
        /**
         * Used when migrating a sql script to the database within the schema.
         * Executes the execute().
         */
        MIGRATION,
        /**
         * Used when operating in the root level of directives.
         * Executes the execute().
         */
        ROOT_PATH
    }
}
