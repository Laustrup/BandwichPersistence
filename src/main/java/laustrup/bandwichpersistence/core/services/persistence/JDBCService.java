package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.Query;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class JDBCService {

    private static ResultSet _resultSet;

    private static Integer _currentRow = null;

    public static Instant getInstant(String column) {
        return get(
                name -> getTimestamp(name, Timestamp::toInstant),
                column
        );
    }

    public static <T> T get(Timestamp timestamp, Function<Timestamp, T> function) {
        return timestamp == null ? null : function.apply(timestamp);
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

    public static String specifyColumn(String... columnTitle) {
        return String.join(".", columnTitle);
    }

    public static <T> T ifColumnExists(
            Function<String, T> function,
            String... columns
    ) {
        for (String column : columns) {
            try {
                T t = function.apply(column);
                if (t != null)
                    return t;
            } catch (Exception ignored) {}
        }

        return null;
    }

    public static String getString(String column) {
        try {
            return _resultSet.getString(column);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UUID getUUID(String column) {
        try {
            String value = _resultSet.getString(column);

            return value == null ? null : UUID.fromString(value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getInteger(String column) {
        try {
            return _resultSet.getInt(column);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLong(String column) {
        try {
            return _resultSet.getLong(column);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean getBoolean(String column) {
        try {
            return _resultSet.getBoolean(column);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getTimestamp(String column, Function<Timestamp, T> function) {
        try {
            return get(_resultSet.getTimestamp(column), function);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getFromNextRow(
            ResultSet resultSet,
            Supplier<T> supply,
            Consumer<SQLException> logging
    ) {
        T type = null;

        try {
            if (resultSet.next())
                type = supply.get();

            resultSet.previous();
        } catch (SQLException exception) {
            logging.accept(exception);
        }

        return type;
    }

    public static <T> Stream<T> build(ResultSet resultSet, Supplier<T> supplier) {
        _resultSet = resultSet;
        Liszt<T> ts = new Liszt<>();

        try {
            while (_resultSet.next()) {
                ts.add(supplier.get());
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
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
                    resultSet.previous();
                break;
            }
            runnable.run();
        } while (allowNextRow && _resultSet.next());

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

        for (char c : field.replace("_", " ").toCharArray())
            databaseColumn
                    .append(Character.isUpperCase(c) ? '_' : "")
                    .append(Character.toLowerCase(c));

        return databaseColumn.toString();
    }
}
