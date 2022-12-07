package laustrup.bandwichpersistence.repositories;

import laustrup.bandwichpersistence.miscs.Crate;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;
import lombok.Getter;

import java.sql.*;

/**
 * An abstract class that takes SQLs and performs them through JDBC methods.
 * Are also including a private class for handling database connections.
 * The database connection that is being handled is a connection specifically for this class.
 */
public abstract class Repository {

    /**
     * The connector that is used to handle the database connection of this Repository.
     */
    private final DBConnector _connector = new DBConnector();

    /**
     * Opens the connection, if it isn't already open.
     * Is used for reading database executions for actions such as selecting/getting.
     * Is not used for creating changes to the database.
     * Uses the executeQuery() of PreparedStatement to execute a specified SQL statement.
     * Will NOT automatically close connection.
     * @param sql The specified SQL statement, that specifies the action intended for the database.
     * @return The ResultSet gathered from the PreparedStatement, if something unexpected happened, it returns null.
     */
    protected ResultSet read(String sql) {
        if (handleConnection()) {
            try {
                PreparedStatement statement = _connector.get_connection().prepareStatement(sql);
                return statement.executeQuery();
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't read sql statement...",e);
            }
            return null;
        }
        Printer.get_instance().print("Couldn't handle database connection...", new SQLException());
        return null;
    }

    /**
     * Opens the connection, if it isn't already open.
     * Are used for making changes such as Update and Delete to the database.
     * Uses the executeUpdate() of PreparedStatement to execute a specified SQL statement.
     * Can automatically close connection.
     * @param sql The specified SQL statement, that specifies the action intended for the database.
     * @param doClose If true the connection will be closed after action.
     * @return The boolean answer of the database success.
     */
    protected boolean edit(String sql, boolean doClose) {
        if (handleConnection()) {
            try {
                PreparedStatement statement = _connector.get_connection().prepareStatement(sql);
                boolean success = statement.executeUpdate() > 0;

                if (doClose)
                    closeConnection();
                return success;
            } catch (SQLException e) { Printer.get_instance().print("Couldn't execute update...",e); }
        }
        return false;
    }

    /**
     * Opens the connection, if it isn't already open.
     * Are used for making changes such as Create to the database.
     * Uses the executeUpdate() of PreparedStatement to execute a specified SQL statement.
     * Will NOT automatically close connection.
     * @param sql The specified SQL statement, that specifies the action intended for the database.
     * @return The PreparedStatement that is executed with the GENERATED KEY.
     */
    protected PreparedStatement create(String sql) {
        if (handleConnection()) {
            try {
                PreparedStatement statement = _connector.get_connection().prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS);

                statement.executeUpdate();
                return statement;
            } catch (SQLException e) { Printer.get_instance().print("Couldn't execute update...",e); }
        }
        return null;
    }

    /**
     * If connection is closed, it will open it, otherwise not.
     * @return True if it has opened it and false if not.
     */
    public boolean handleConnection() {
        try {
            if (connection().isClosed()) {
                _connector.createConnection();
                return true;
            }
        } catch (SQLException e) { Printer.get_instance().print("Couldn't open connection...",e); }

        return false;
    }

    /**
     * Will close the database connection of this instance.
     * @return The success of the closing as a Plato. Will be undefined, if there is a SQLException and null if the connection is null.
     */
    public Plato closeConnection() { return _connector.closeConnection(); }

    /**
     * Will determine if the Connection of this Repository is closed.
     * @return True if it is closed, false if it is open and undefined.
     *         All as Plato type.
     */
    public Plato connectionIsClosed() {
        try {
            return new Plato(connection().isClosed());
        } catch (SQLException e) {
            Printer.get_instance().print("Trouble determine if the connection is closed...",e);
        }
        return new Plato();
    }
    /**
     * Defines the database connection of this repository by getting it from its DBConnector.
     * @return The database connection from the DBConnector.
     */
    private Connection connection() { return _connector.get_connection(); }

    /**
     * Are handling the connections of databases and this application.
     * May only be used for the abstract Repository.
     */
    private class DBConnector {

        /**
         * The database connection for this entity's connector.
         * Must be closed after use, for better performance.
         */
        @Getter
        private Connection _connection;

        /**
         * Will create a connection, if it is closed at the moment.
         * @return If it opens the connection, it will return the opened connection, else it will return null.
         */
        public Connection createConnection() {
            try {
                if (_connection.isClosed()) {
                    openConnection();
                    return _connection;
                }
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't create connection...",e);
            }
            return null;
        }

        /**
         * Opens the connection with the DriverManager and the Crate information.
         * @throws SQLException Will be thrown if there is a problem with the connection.
         */
        private void openConnection() throws SQLException {
            Crate crate = Crate.get_instance();
            _connection = DriverManager.getConnection(crate.get_dbPath(), crate.get_dbUser(), crate.get_dbPassword());
        }

        /**
         * If the connection isn't already closed, it will close it.
         * @return The success of the closing as a Plato. Will be undefined, if there is a SQLException and null if the connection is null.
         */
        public Plato closeConnection() {
            if (_connection != null) {
                try {
                    if (!_connection.isClosed()) {
                        _connection.close();
                        return new Plato(true);
                    }
                } catch (SQLException e) {
                    Printer.get_instance().print("Couldn't close connection...",e);
                    return new Plato();
                }
            }
            return null;
        }
    }
}
