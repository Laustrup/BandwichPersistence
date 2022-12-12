package laustrup.bandwichpersistence.repositories;

import laustrup.bandwichpersistence.miscs.Crate;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbGate {

    /**
     * Singleton instance of the Repository.
     */
    private static DbGate _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static DbGate get_instance() {
        if (_instance == null) _instance = new DbGate();
        return _instance;
    }

    /**
     * This is the only connection to the database, that is to be used.
     */
    @Getter
    private static Connection _connection;

    private DbGate() {}

    /**
     * Will open connection.
     * @return True if the connection could open, otherwise false.
     */
    public boolean open() {
        if (isClosed()) {
            Crate crate = Crate.get_instance();
            try {
                _connection = DriverManager.getConnection(crate.get_dbPath(), crate.get_dbUser(), crate.get_dbPassword());
                return !isClosed();
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't open connection...",e);
            }
        }
        return false;
    }

    /**
     * Will close connection.
     * @return The success of the closing as a Plato. Will be undefined, if there is a SQLException and null if the connection is null.
     */
    public Plato close() {
        if (_connection != null) {
            try {
                if (isOpen()) {
                    _connection.close();
                    return new Plato(true);
                }
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't close connection...",e);
                Plato plato = new Plato();
                plato.set_message("Couldn't close connection...");
                return plato;
            }
        }
        return null;
    }

    /**
     * Determine whether the connection is open.
     * @return True if it is open, false if it is closed.
     */
    public boolean isOpen() {
        return !isClosed();
    }

    /**
     * Determine whether the connection is closed.
     * @return True if it is closed, false if it is open.
     */
    public boolean isClosed() {
        try {
            return _connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
