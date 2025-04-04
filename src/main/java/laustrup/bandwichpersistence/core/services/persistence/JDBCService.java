package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
                    field.is_key()
                            ? UUID.nameUUIDFromBytes(_resultSet.getBytes(toDatabaseColumn(field.get_content())))
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T get(Timestamp timestamp, Function<Timestamp, T> function) {
        return timestamp == null
                ? null
                : function.apply(timestamp);
    }

    public static <T> T get(
            Function<String, T> function,
            String... columnTitle
    ) {
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

    private static <T> T perform(String field, Function<String, T> function) {
        try {
            return function.apply(field);
        } catch (Exception e) {
            return function.apply(field.split("\\.")[1]);
        }
    }

    public static String specifyColumn(String... columnTitle) {
        return String.join(".", columnTitle);
    }

    public static <T> T ifColumnExists(
            Function<String, T> function,
            String... columns
    ) {
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
        return perform(toDatabaseColumn(column), title -> {
            try {
                return _resultSet.getString(title);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String getString(Field field) {
        return perform(toDatabaseColumn(field.get_content()), title -> {
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
        });
    }

    public static UUID getUUID(Field field) {
        return getUUID(field.get_content());
    }

    public static UUID getUUID(String column) {
        return perform(toDatabaseColumn(column), title -> {
            try {
                return UUID.nameUUIDFromBytes(_resultSet.getBytes(title));
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public static Integer getInteger(String column) {
        return perform(toDatabaseColumn(column), title -> {
            try {
                return _resultSet.getInt(title);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Long getLong(String column) {
        return perform(toDatabaseColumn(column), title -> {
            try {
                return _resultSet.getLong(title);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Boolean getBoolean(String column) {
        return perform(toDatabaseColumn(column), title -> {
            try {
                return _resultSet.getBoolean(title);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static <T> T getTimestamp(String column, Function<Timestamp, T> function) {
        return perform(toDatabaseColumn(column), title -> {
            try {
                return get(_resultSet.getTimestamp(title), function);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Instant getInstant(Field field) {
        return getInstant(field.get_content());
    }

    public static Instant getInstant(String column) {
        return perform(toDatabaseColumn(column), title -> get(
                name -> getTimestamp(name, Timestamp::toInstant),
                title
        ));
    }

    /**
     * Looks into the resultset to find a targeted information.
     * Will go one line forward and then move all the way back.
     * @param resultSet The table of gathered results.
     * @param supply The algorithm to reach the target information.
     * @param logging In case of a SQLException, how should this be logged?
     * @return The target information.
     * @param <T> The target information type.
     */
    public static <T> T peek(
            ResultSet resultSet,
            Supplier<T> supply,
            Consumer<SQLException> logging
    ) {
        T type = null;
        _resultSet = resultSet;

        try {
            if (_resultSet.next())
                type = supply.get();

            moveBackwards(true);
        } catch (SQLException exception) {
            logging.accept(exception);
        }

        return type;
    }

    public static <T> T peek(
            ResultSet resultSet,
            Function<ResultSet, T> action,
            Consumer<SQLException> logging
    ) {
        T type = null;
        _resultSet = resultSet;

        try {
            if (_resultSet.next())
                type = action.apply(_resultSet);

            moveBackwards(true);
        } catch (SQLException exception) {
            logging.accept(exception);
        }

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
            build(
                    resultSet,
                    runnable,
                    breaker,
                    primaries
            );
        } catch (SQLException sqlException) {
            exception.accept(sqlException);
        }
    }

    public static void build(
            ResultSet resultSet,
            Runnable runnable,
            UUID primary
    ) throws SQLException {
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
        _resultSet = resultSet;
        boolean allowNextRow = false;
        if (_currentRow == null) {
            allowNextRow = true;
            _resultSet.next();
            _currentRow = _resultSet.getRow();
        }

        do {
            if (isDoneBuilding(breaker, primaries)) {
                if (allowNextRow)
                    moveBackwards(false);
                break;
            }
            runnable.run();
        } while (allowNextRow && _resultSet.next());

        if (allowNextRow)
            _currentRow = null;
    }

    public static <K, V> void putIfAbsent(
            Map<K, V> map,
            K key,
            V value
    ) {
        if (key != null)
            map.putIfAbsent(key, value);
    }

    public static <T> List<Query> prepareQueries(Collection<T> collection, Function<Integer, Query> function) {
        if (collection == null || collection.isEmpty())
            return new ArrayList<>();

        List<Query> queries = new ArrayList<>();

        for (int i = 0; i < collection.size(); i++)
            queries.add(function.apply(i));

        return queries;
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

        return databaseColumn.toString();
    }

    private static void moveBackwards(boolean toStart) throws SQLException {
        if (!_resultSet.isClosed())
            do
                _resultSet.previous();
            while (toStart && !_resultSet.isBeforeFirst());
    }

    @Getter
    public static class Field {

        private final String _table;

        private final String _row;

        public Field(String table, String row) {
            _table = table;
            _row = row;
        }

        public static Field of(String table, String row) {
            return new Field(table, row);
        }

        public String get_content() {
            return String.format("%s.%s", _table, _row);
        }

        public boolean is_key() {
            return _row.contains(".id") || _row.contains("_id");
        }
    }
}
