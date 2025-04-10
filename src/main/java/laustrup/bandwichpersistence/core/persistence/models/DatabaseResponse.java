package laustrup.bandwichpersistence.core.persistence.models;

import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseResponse {

    private final Logger _logger = Logger.getLogger(DatabaseResponse.class.getName());

    @Getter
    private final PreparedStatement _preparedStatement;

    @Getter
    private final Query _query;

    @Getter
    private final Exception _exception;

    public DatabaseResponse(
            PreparedStatement preparedStatement,
            Query query,
            Exception exception
    ) {
        _preparedStatement = preparedStatement;
        _query = query;
        _exception = exception;
    }

    public ResultSet get_resultSet() {
        try {
            return _preparedStatement != null ? _preparedStatement.getResultSet() : null;
        } catch (SQLException e) {
            _logger.warning("Couldn't get resultSet!\n" + e.getMessage() + (
                    _exception != null
                            ? "\n\n\tIt also had the following error:\n" + _exception.getMessage()
                            : ""
            ));
            throw new RuntimeException(e);
        }
    }
}
