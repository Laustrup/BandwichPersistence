package laustrup.bandwichpersistence.core.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.libraries.DatabaseLibrary.*;

public class DatabaseGate {

    private static Connection _connection;

    private static final Logger _logger = Logger.getLogger(DatabaseGate.class.getSimpleName());

    /**
     * Will describe how many milliseconds the period will be allowed for the attempt to create a connection.
     */
    public static final int CONNECTION_ATTEMPT_LIMIT = 10000;

    public static Connection getConnection(String url) {
        try {
            return connectionIsNotAvailable() ? createConnection(url) : _connection;
        } catch (Exception e) {
            _logger.log(
                    Level.WARNING,
                    "Couldn't get connection to database!",
                    e
            );
            return null;
        }
    }

    private static Connection createConnection(String url) throws SQLException {
        LocalDateTime start = LocalDateTime.now();
        SQLException exception = null;

        if (_connection == null)
            System.out.printf("Connecting to %s for the first time\n%n", url);

        Supplier<Long> countDuration = () -> Duration.between(start, LocalDateTime.now()).toMillis();
        int
                attempts = 0,
                sleepInterval = 350;

        while (connectionIsNotAvailable() && countDuration.get() <= CONNECTION_ATTEMPT_LIMIT)
            try {
                if (attempts > 0 || _connection == null)
                    Thread.sleep(sleepInterval);
                _connection = DriverManager.getConnection(url, get_DBProperties());
            } catch (SQLException sqlException) {
                attempts++;
                sleepInterval += 100;
                exception = sqlException;
                System.err.printf(
                        "\n\tFailed to get connection to database for %s attempt(s) and took %s milliseconds",
                        attempts,
                        countDuration.get()
                );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        if (connectionIsNotAvailable() && exception != null)
            throw exception;

        return _connection;
    }

    public static void closeConnection() {
        if (_connection != null) {
            try {
                _connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean connectionIsNotAvailable() {
        try {
            return _connection == null || _connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
