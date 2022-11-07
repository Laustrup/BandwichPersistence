package laustrup.bandwichpersistence.repositories;

import laustrup.bandwichpersistence.miscs.Crate;
import laustrup.bandwichpersistence.utilities.Printer;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    @Getter
    private Connection _connection;

    public Connection createConnection() {
        try {
            if (!_connection.isClosed()) {
                openConnection();
                return _connection;
            }
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't create connection...",e);
        }
        return null;
    }

    private void openConnection() throws SQLException {
        Crate crate = Crate.get_instance();
        this._connection = DriverManager.getConnection(crate.get_dbPath(), crate.get_dbUser(), crate.get_dbPassword());
    }

    public boolean closeConnection() {
        if (_connection != null) {
            try {
                if (!_connection.isClosed()) {
                    _connection.close();
                    return true;
                }
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't close connection...",e);
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
