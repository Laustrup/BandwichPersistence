package laustrup.bandwichpersistence.items;

import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;
import laustrup.bandwichpersistence.core.persistence.DatabaseGate;
import laustrup.bandwichpersistence.items.ResultSetTestGenerator.MockedResultSetException.Level;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.lang.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static laustrup.bandwichpersistence.core.services.EternaryService.ifEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

@ExtendWith(MockitoExtension.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultSetTestGenerator {

  @Mock
  private Statement _statement;

  public static ResultSetTestGenerator RESULT_SET_GENERATOR = null;

  public static ResultSetTestGenerator getResultSetGenerator() {
    if (RESULT_SET_GENERATOR == null)
      RESULT_SET_GENERATOR = new ResultSetTestGenerator();

    return RESULT_SET_GENERATOR;
  }

  public ResultSet generate(/*language=MySQL*/ @Nullable String query) {
    try {
      Connection connection = DatabaseGate.getConnection(DatabaseLibrary.get_connectionString());
      _statement = connection.createStatement();

      return ifEmpty(query)
          .then(this::getResultSet)
          .orElse(() -> executeQuery(query));
    } catch (SQLException exception) {
      throw new MockedResultSetException(Level.BASE, exception);
    }
  }

  private ResultSet executeQuery(String query) {
    try {
      return _statement.executeQuery(query);
    } catch (SQLException exception) {
      throw new MockedResultSetException(Level.QUERY_EXECUTION, query, exception);
    }
  }

  private ResultSet getResultSet() {
    try {
      return _statement.getResultSet();
    } catch (SQLException exception) {
      throw new MockedResultSetException(Level.RESULTSET_RETRIEVE, exception);
    }
  }

  public static class MockedResultSetException extends RuntimeException {

    public MockedResultSetException(Level level, SQLException exception) {
      super(level.get_message(), exception);
    }

    public MockedResultSetException(Level level, String message, SQLException exception) {
      super(level.get_message(message), exception);
    }

    @AllArgsConstructor
    public enum Level {
      BASE(false),
      QUERY_EXECUTION(true),
      RESULTSET_RETRIEVE(false);

      private final boolean _needsDetail;

      private String get_message() {
        return get_message(null);
      }

      private String get_message(@Nullable String detail) {
        boolean noDetailsWhenNeeded = _needsDetail && detail == null;

        return switch (this) {
          case BASE -> "Something went wrong at base level";
          case QUERY_EXECUTION -> stating(noDetailsWhenNeeded)
              .then("Couldn't execute query")
              .orElse(() -> String.format("Couldn't execute query from the statement %s", detail));
          case RESULTSET_RETRIEVE -> "Couldn't retrieve result set";
        } + " when mocking resultset for test!";
      }
    }
  }
}
