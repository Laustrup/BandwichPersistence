package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.DataType;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;

import javax.naming.NameNotFoundException;
import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class JDBCService {

    private static final Logger _logger = Logger.getLogger(JDBCService.class.getName());

    private static ResultSet _resultSet;

    private static Integer _currentRow = null;

    @SuppressWarnings("unchecked")
    public static <T> AtomicReference<T> set(AtomicReference<T> reference, Field field) {
        try {
            return (AtomicReference<T>) reference.getAndSet((T) (
                    field.is_key() && getType(field) == DataType.BINARY
                            ? getUUID(toDatabaseColumn(field.get_content()))
                            : _resultSet.getObject(toDatabaseColumn(field.get_content()))
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> add(Collection<T> collection, Field field) {
        try {
            collection.add((T) _resultSet.getObject(toDatabaseColumn(field.get_content())));
            return collection;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> T get(Timestamp timestamp, Function<Timestamp, T> function) {
        return timestamp == null
                ? null
                : function.apply(timestamp);
    }

    public static <T> T get(Function<String, T> function, String... columnTitle) {
        for (int i = 0; i < columnTitle.length; i++)
            columnTitle[i] = toDatabaseColumn(specifyColumn(Arrays.stream(columnTitle)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow()
            ));

        return ifColumnExists(function, columnTitle);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Field field, Class<T> type) {
        return switch (type.getSimpleName()) {
            case "String" -> (T) getString(field.get_content());
            case "Instant" -> (T) getInstant(field.get_content());
            case "Boolean" -> (T) getBoolean(field.get_content());
            case "UUID" -> (T) getUUID(field.get_content());
            case "Integer" -> (T) getInteger(field.get_content());
            case "Long" -> (T) getLong(field.get_content());
            default -> null;
        };
    }

    public static String specifyColumn(String... columnTitle) {
        return String.join(".", columnTitle);
    }

    public static <T> T ifColumnExists(Function<String, T> function, String... columns) {
        for (String column : columns) {
            try {
                T t = function.apply(toDatabaseColumn(column));
                if (t != null)
                    return t;
            } catch (Exception ignored) {}
        }

        return null;
    }

    public static String getString(String column) {
        try {
            return _resultSet.getString(toDatabaseColumn(column));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(Field field) {
        String title = toDatabaseColumn(field.get_content());

        while (true) {
            try {
                return _resultSet.getString(title);
            } catch (SQLException e) {
                if (title.contains("."))
                    title = title.substring(title.indexOf("."));
                else
                    throw new RuntimeException(e);
            }
        }
    }

    public static UUID getUUID(Field field) {
        return getUUID(field.get_content());
    }

    public static UUID getUUID(String column) {
        try {
            return UUID.nameUUIDFromBytes(_resultSet.getBytes(toDatabaseColumn(column)));
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Integer getInteger(String column) {
        try {
            return _resultSet.getInt(toDatabaseColumn(column));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLong(String column) {
        try {
            return _resultSet.getLong(toDatabaseColumn(column));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean getBoolean(String column) {
        try {
            return _resultSet.getBoolean(toDatabaseColumn(column));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getTimestamp(String column, Function<Timestamp, T> function) {
        try {
            return get(_resultSet.getTimestamp(toDatabaseColumn(column)), function);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Instant getInstant(Field field) {
        return getInstant(field.get_content());
    }

    public static Instant getInstant(String column) {
        return get(
                name -> getTimestamp(name, Timestamp::toInstant),
                toDatabaseColumn(column)
        );
    }

    /**
     * Looks into the resultSet to find a targeted information.
     * Will go one line forward and then move all the way back.
     * @param resultSet The table of gathered results.
     * @param supply The algorithm to reach the target information.
     * @param logging In case of a SQLException, how should this be logged?
     * @return The target information.
     * @param <T> The target information type.
     */
    public static <T> T peek(ResultSet resultSet, Supplier<T> supply, Consumer<SQLException> logging) {
        T type = null;
        _resultSet = resultSet;

        try {
            if (_resultSet.next())
                type = supply.get();
            else
                throw new SQLException("No more rows when peeking");

            moveBackwards(true);
        } catch (SQLException exception) {
            logging.accept(exception);
        }

        reset();

        return type;
    }

    /**
     * Looks into the resultSet to find a targeted information.
     * Will go one line forward and then move all the way back.
     * @param resultSet The table of gathered results.
     * @param action The algorithm to reach the target information.
     * @param logging In case of a SQLException, how should this be logged?
     * @return The target information.
     * @param <T> The target information type.
     */
    public static <T> T peek(ResultSet resultSet, Function<ResultSet, T> action, Consumer<SQLException> logging) {
        T type = null;
        _resultSet = resultSet;

        try {
            if (_resultSet.next())
                type = action.apply(_resultSet);
            else
                throw new SQLException("No more rows when peeking");

            moveBackwards(true);
        } catch (SQLException exception) {
            logging.accept(exception);
        }

        reset();

        return type;
    }

    public static <T> Stream<T> build(ResultSet resultSet, Supplier<T> supplier) {
        _resultSet = resultSet;
        Liszt<T> ts = new Liszt<>();

        try {
            while (_resultSet.next())
                ts.add(supplier.get());
        } catch (Exception exception) {
            _logger.warning(exception.getMessage());
        }

        reset();

        return ts.stream();
    }

    public static <T> void build(
            ResultSet resultSet,
            Runnable runnable,
            Function<T, Boolean> breaker,
            Consumer<Exception> exception,
            T... primaries
    ) {
        try {
            build(resultSet, runnable, breaker, primaries);
        } catch (SQLException sqlException) {
            exception.accept(sqlException);
        }
    }

    public static void build(ResultSet resultSet, Runnable runnable, AtomicReference<UUID> primary) throws SQLException {
        build(resultSet, runnable, primary.get());
    }

    public static void build(ResultSet resultSet, Runnable runnable, UUID primary) throws SQLException {
        build(
                resultSet,
                runnable,
                id -> !get(
                        JDBCService::getUUID,
                        Model.ModelDTO.Fields.id
                ).equals(id),
                primary
        );
    }

    public static <T> void build(
            ResultSet resultSet,
            Runnable runnable,
            Function<T, Boolean> breaker,
            T... primaries
    ) throws SQLException {
        _resultSet = _resultSet == null ? resultSet : _resultSet;
        boolean isFirst = false;
        if (!isBuilding()) {
            isFirst = true;
            _resultSet.next();
            _currentRow = _resultSet.getRow();
        }

        try {
            do {
                if (isDoneBuilding(breaker, primaries)) {
                    if (isFirst)
                        moveBackwards(false);
                    break;
                }
                runnable.run();
            } while (isFirst && _resultSet.next());
        } catch (Exception exception) {
            reset();
            throw exception;
        }

        if (isFirst)
            reset();
    }

    public static boolean isBuilding() {
        return _currentRow != null;
    }

    public static void set_resultSet(ResultSet resultSet) throws IllegalStateException {
        if (!isBuilding())
            _resultSet = resultSet;
        else
            throw new IllegalStateException("ResultSet of JDBC Service cannot be mutated, since a build is already in progress!");
    }

    private static <T> boolean isDoneBuilding(Function<T, Boolean> breaker, T... primaries) {
        for (T primary : primaries)
            if (primary != null && breaker.apply(primary))
                return true;
        return false;
    }

    public static String toDatabaseColumn(String field) {
        StringBuilder databaseColumn = new StringBuilder();

        for (char c : field.toCharArray())
            databaseColumn
                    .append(Character.isUpperCase(c) ? '_' : "")
                    .append(Character.toLowerCase(c));

        if (databaseColumn.charAt(0) == ' ' || databaseColumn.charAt(0) == '_')
            databaseColumn.deleteCharAt(0);

        return databaseColumn.toString()
                .replace("._", ".");
    }

    private static void moveBackwards(boolean toStart) throws SQLException {
        if (!_resultSet.isClosed() && _resultSet.getRow() > 0)
            do
                _resultSet.previous();
            while (toStart && _resultSet.getRow() > 0 && !_resultSet.isBeforeFirst());
    }

    private static void reset() {
        _resultSet = null;
        _currentRow = null;
    }

    private static DataType getType(Field field) {
        try {
            return DataType.valueOf(_resultSet.getMetaData().getColumnType(columnIndex(field)));
        } catch (SQLException | NameNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static int columnIndex(Field field) throws NameNotFoundException {
        try {
            ResultSetMetaData metaData = _resultSet.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++)
                if (metaData.getColumnName(i).equals(field.get_content().toLowerCase()))
                    return i;

            throw new NameNotFoundException("Could not find column " + field.get_content() + " when finding its index.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
