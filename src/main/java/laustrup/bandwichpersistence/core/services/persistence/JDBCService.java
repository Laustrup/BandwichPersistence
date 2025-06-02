package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.DataType;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations;
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

    public static <T> AtomicReference<T> set(AtomicReference<T> reference, Field field) {
        return ResultSetService.set(
                new Configurations(
                        field.get_content(),
                        _resultSet
                ), reference
        );
    }

    public static <T> Collection<T> add(Collection<T> collection, Field field) {
        return ResultSetService.add(new Configurations(field, _resultSet), collection);
    }

    public static <T> T get(Field field, Class<T> type) {
        return get(field.get_content(), type);
    }

    public static <T> T get(String field, Class<T> type) {
        return ResultSetService.get(new Configurations(field, _resultSet), type);
    }

    public static String getString(String column) {
        return ResultSetService.getString(new Configurations(column, _resultSet));
    }

    public static String getString(Field field) {
        return ResultSetService.getString(new Configurations(field, _resultSet));
    }

    public static UUID getUUID(Field field) {
        return ResultSetService.getUUID(new Configurations(field.get_content(), _resultSet));
    }

    public static UUID getUUID(String column) {
        return ResultSetService.getUUID(new Configurations(column, _resultSet));
    }

    public static Integer getInteger(String column) {
        return ResultSetService.getInteger(new Configurations(column, _resultSet));
    }

    public static Long getLong(String column) {
        return ResultSetService.getLong(new Configurations(column, _resultSet));
    }

    public static Boolean getBoolean(String column) {
        return ResultSetService.getBoolean(new Configurations(column, _resultSet));
    }

    public static <T> T getTimestamp(String column, Function<Timestamp, T> function) {
        return ResultSetService.getTimestamp(new Configurations(column, _resultSet), function);
    }

    public static Instant getInstant(Field field) {
        return ResultSetService.getInstant(new Configurations(field.get_content(), _resultSet));
    }

    public static Instant getInstant(String column) {
        return ResultSetService.getInstant(new Configurations(column, _resultSet));
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
                id -> !ResultSetService.get(
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
                        ResultSetService.moveBackwards(_resultSet, false);
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

    private static void reset() {
        _resultSet = null;
        _currentRow = null;
    }

    private static DataType getType(Field field) {
        return ResultSetService.getType(new Configurations(field, _resultSet));
    }

    public static class ResultSetService {

        @SuppressWarnings("unchecked")
        public static <T> AtomicReference<T> set(Configurations configurations, AtomicReference<T> reference) {
            try {
                Field field = configurations.getField();
                
                return (AtomicReference<T>) reference.getAndSet((T) (
                        field.is_key() && getType(configurations) == DataType.BINARY
                                ? getUUID(configurations)
                                : configurations.resultSet.getObject(field.get_content())
                ));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> Collection<T> add(Configurations configurations, Collection<T> collection) {
            try {
                collection.add((T) configurations.resultSet.getObject(configurations.field()));
                return collection;
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> T get(Configurations configurations, Class<T> type) {
            return switch (type.getSimpleName()) {
                case "String" -> (T) getString(configurations);
                case "Instant" -> (T) getInstant(configurations);
                case "Boolean" -> (T) getBoolean(configurations);
                case "UUID" -> (T) getUUID(configurations);
                case "Integer" -> (T) getInteger(configurations);
                case "Long" -> (T) getLong(configurations);
                default -> null;
            };
        }

        public static <T> T get(Function<String, T> function, String... columnTitle) {
            for (int i = 0; i < columnTitle.length; i++)
                columnTitle[i] = DatabaseService.specifyColumn(Arrays.stream(columnTitle)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElseThrow()
                );

            return ifColumnExists(function, columnTitle);
        }

        public static <T> T get(Timestamp timestamp, Function<Timestamp, T> function) {
            return timestamp == null ? null : function.apply(timestamp);
        }

        private static DataType getType(Configurations configurations) {
            try {
                return DataType.valueOf(configurations.resultSet.getMetaData().getColumnType(columnIndex(configurations.getField())));
            } catch (SQLException | NameNotFoundException exception) {
                throw new RuntimeException(exception);
            }
        }

        public static void restart(ResultSet resultSet) {
            try {
                moveBackwards(resultSet, true);
            } catch (SQLException e) {
                _logger.warning(String.format("Failed to restart ResultSet: %s", e.getMessage()));
            }
        }

        public static String getString(Configurations configurations) {
            return handleGet(configurations, () -> {
                try {
                    return configurations.resultSet.getString(configurations.field());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static UUID getUUID(Configurations configurations) {
            return handleGet(configurations, () -> {
                try {
                    return UUID.nameUUIDFromBytes(configurations.resultSet.getBytes(configurations.field()));
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            });
        }

        public static Integer getInteger(Configurations configurations) {
            return handleGet(configurations, () -> {
                try {
                    return configurations.resultSet.getInt(configurations.field());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static Long getLong(Configurations configurations) {
            return handleGet(configurations, () -> {
                try {
                    return configurations.resultSet.getLong(configurations.field());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static Boolean getBoolean(Configurations configurations) {
            return handleGet(configurations, () -> {
                try {
                    return configurations.resultSet.getBoolean(configurations.field());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static Instant getInstant(Configurations configurations) {
            return getTimestamp(configurations, Timestamp::toInstant);
        }

        public static <T> T getTimestamp(Configurations configurations, Function<Timestamp, T> function) {
            return handleGet(configurations, () -> {
                try {
                    return get(configurations.resultSet.getTimestamp(configurations.field()), function);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private static <T> T handleGet(Configurations configurations, Supplier<T> supplier) {
            try {
                return switch (configurations.mode) {
                    case NEUTRAL -> supplier.get();
                    case START -> {
                        configurations.startResultSet();
                        yield supplier.get();
                    }
                    case PEEK -> {
                        configurations.startResultSet();
                        T generic = supplier.get();
                        restart(configurations.resultSet);
                        yield generic;
                    }
                };
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public static <T> T ifColumnExists(Function<String, T> function, String... columns) {
            for (String column : columns) {
                try {
                    T generic = function.apply(column);
                    if (generic != null)
                        return generic;
                } catch (Exception ignored) {}
            }

            return null;
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

        private static void moveBackwards(ResultSet resultSet, boolean toStart) throws SQLException {
            if (!resultSet.isClosed() && resultSet.getRow() > 0)
                do
                    resultSet.previous();
                while (toStart && resultSet.getRow() > 0 && !resultSet.isBeforeFirst());
        }

        public record Configurations(String field, ResultSet resultSet, Mode mode) {

            public Configurations(Field field, ResultSet resultSet, Mode mode) {
                this(field.get_content(), resultSet, mode);
            }

            public Configurations(Field field, ResultSet resultSet) {
                this(field.get_content(), resultSet, Mode.NEUTRAL);
            }
            
            public Configurations(String field, ResultSet resultSet) {
                this(field, resultSet, Mode.NEUTRAL);
            }
            
            public Field getField() {
                String[] content = field.split("\\.");
                return new Field(content[0], content[1]);
            }
            
            public String field() {
                return DatabaseService.toDatabaseColumn(field);
            }
            
            public void startResultSet() throws SQLException {
                if (resultSet.isBeforeFirst())
                    resultSet.next();
            }

            public enum Mode {
                /**
                 * Just acts on the resultset without either starting nor peeking.
                 */
                NEUTRAL,
                /**
                 * Before acting it start the resultset and afterward puts it back.
                 */
                PEEK,
                /**
                 * Only starts the resultset before acting.
                 */
                START
            }
        }
    }
    
    public static class DatabaseService {

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

        public static String specifyColumn(String... columnTitle) {
            return String.join(".", columnTitle);
        }
    }
}
