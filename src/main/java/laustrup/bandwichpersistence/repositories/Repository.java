package laustrup.bandwichpersistence.repositories;

import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Repository {

    private final DBConnector _connector = new DBConnector();

    public ResultSet read(String sql) {
        if (handleConnection()) {
            try {
                PreparedStatement statement = _connector.get_connection().prepareStatement(sql);
                return statement.executeQuery();
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't read sql statement...",e);
            }
            return null;
        }
        Printer.get_instance().print("Couldn't read sql statement...", new Exception());
        return null;
    }

    /* Create, Update and Delete
     * Will automatically close connection
     */
    public boolean cud(String sql) {
        if (handleConnection()) {
            try {
                PreparedStatement statement = _connector.get_connection().prepareStatement(sql);
                boolean success = statement.executeUpdate() != 0;

                closeConnection();
                return success;
            } catch (SQLException e) { Printer.get_instance().print("Couldn't execute update...",e); }
        }
        return false;
    }

    // Checks whether connection is closed or not, if it is closed, it will open it
    public boolean handleConnection() {
        try {
            if (connection().isClosed()) {
                _connector.createConnection();
                return true;
            }
        } catch (SQLException e) { Printer.get_instance().print("Couldn't open connection...",e); }

        return false;
    }
    public boolean closeConnection() { return _connector.closeConnection(); }
    private Connection connection() { return _connector.get_connection(); }
}
