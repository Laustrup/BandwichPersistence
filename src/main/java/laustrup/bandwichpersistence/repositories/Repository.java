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
                PreparedStatement statement = connection().prepareStatement(sql);
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
                PreparedStatement statement = connection().prepareStatement(sql);
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
                PreparedStatement statement = connection().prepareStatement(sql,
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
                this._connector.createConnection();
                return true;
            }
        } catch (SQLException e) { Printer.get_instance().print("Couldn't open connection...",e); }

        return false;
    }

    /**
     * Will close the database connection of this instance.
     * @return The success of the closing as a Plato. Will be undefined, if there is a SQLException and null if the connection is null.
     */
    public Plato closeConnection() { return this._connector.closeConnection(); }

    /**
     * Will determine if the Connection of this Repository is closed.
     * @return True if it is closed, false if it is open and undefined.
     *         All as Plato type.
     */
    public Plato connectionIsClosed() {
        return DbGate.get_instance().isClosed();
    }
    /**
     * Defines the database connection of this repository by getting it from its DBConnector.
     * @return The database connection from the DBConnector.
     */
    private Connection connection() { return DbGate.get_instance().get_connection(); }

    /**
     * Will do a select * of a table, do a where of id and check if the ResultSet contains any rows.
     * Does not close connection.
     * @param id The id that should exist.
     * @param table The table that the id should exist on.
     * @param column The column that should contain the id.
     * @return True if the id exists on table.
     */
    public boolean exists(long id, String table, String column) {
        ResultSet set = read("SELECT * FROM " + table + " WHERE " + column + " = " + id + ";");
        try {
            return !set.isAfterLast();
        } catch (SQLException e) {
            Printer.get_instance().print("ResultSet error in id exists...",e);
        }
        return false;
    }

    /**
     * Will do a select * of a table, do a where of ids and check if the ResultSet contains any rows.
     * Does not close connection.
     * @param table The table that the id should exist on.
     * @param primaryId An id that should exist.
     * @param primaryColumn A column that should contain an id.
     * @param secondaryId Another id that should exist.
     * @param secondaryColumn Another column that should contain an id.
     * @return True if the ids exists on table.
     */
    public boolean exists(String table, long primaryId, String primaryColumn,
                          long secondaryId, String secondaryColumn) {
        ResultSet set = read("SELECT * FROM " + table + " WHERE " + primaryColumn + " = " + primaryId + " AND " +
                secondaryColumn + " = " + secondaryId + ";");
        try {
            return !set.isAfterLast();
        } catch (SQLException e) {
            Printer.get_instance().print("ResultSet error in ids exists...",e);
        }
        return false;
    }

    /**
     * Will delete rows where the id matches the column of the table.
     * Checks if rows exists.
     * @param id The id of the rows that will be deleted.
     * @param table The table that the id should be deleted from.
     * @param column The column that should contain the id.
     * @param closeConnection Will close Connection if true.
     * @return True if connection is closed if intended and the rows doesn't exist.
     */
    protected boolean delete(long id, String table, String column, boolean closeConnection) {
        if (edit("DELETE FROM " + table + " WHERE " + column + " = " + id + ";",false)) {
            boolean doesExists = exists(id, table, column);
            return !doesExists && (closeConnection ? closeConnection().get_truth() : true);
        }
        return (closeConnection ? closeConnection().get_truth() : true);
    }

    /**
     * Will delete rows where the two id matches two columns of a table.
     * Checks if rows exists.
     * @param table The table that the ids should be deleted from.
     * @param primaryId An id of the rows that will be deleted.
     * @param primaryColumn A column that should contain an id.
     * @param secondaryId Another id of the rows that will be deleted.
     * @param secondaryColumn Another column that should contain an id.
     * @param closeConnection Will close Connection if true.
     * @return True if connection is closed if intended and the rows doesn't exist.
     */
    protected boolean delete(String table, long primaryId, String primaryColumn,
                             long secondaryId, String secondaryColumn,
                             boolean closeConnection) {
        if (edit("DELETE FROM " + table +
                " WHERE " + primaryColumn + " = " + primaryId + " AND " +
                secondaryColumn + " = " + secondaryId + ";",false)) {
            boolean doesExists = exists(table, primaryId, primaryColumn, secondaryId, secondaryColumn);
            return !doesExists && (closeConnection ? closeConnection().get_truth() : true);
        }
        return (closeConnection ? closeConnection().get_truth() : true);
    }

    /**
     * Are handling the connections of databases and this application.
     * May only be used for the abstract Repository.
     */
    private class DBConnector {

        /**
         * Will create a connection, if it is closed at the moment.
         * @return If it opens the connection, it will return the opened connection, else it will return null.
         */
        public Connection createConnection() {
            openConnection();
            return DbGate.get_instance().get_connection();
        }

        /**
         * Opens the connection with the DriverManager and the Crate information.
         * @throws SQLException Will be thrown if there is a problem with the connection.
         */
        private void openConnection() {
            if (DbGate.get_instance().isClosed().get_truth())
                DbGate.get_instance().isOpen();
        }

        /**
         * If the connection isn't already closed, it will close it.
         * @return The success of the closing as a Plato. Will be undefined, if there is a SQLException and null if the connection is null.
         */
        public Plato closeConnection() {
            return DbGate.get_instance().close();
        }
    }
}
