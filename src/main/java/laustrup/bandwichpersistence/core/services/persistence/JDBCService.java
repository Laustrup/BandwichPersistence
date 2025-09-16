package laustrup.bandwichpersistence.core.services.persistence;

import laustrup.bandwichpersistence.core.persistence.DataType;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;

import javax.naming.NameNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.DatabaseService.columnOf;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations.Mode.NEUTRAL;

public class JDBCService {

    private static final Logger _logger = Logger.getLogger(JDBCService.class.getName());

    private static ResultSet _resultSet;

    private static Integer _currentRow = null;

    public static AtomicReference<?> set(
            Map<? extends Member, AtomicReference<?>> fields,
            Map.Entry<? extends Member, DatabaseField> dataEntry
    ) {
        return set(fields.get(dataEntry.getKey()), dataEntry.getValue());
    }

    public static <T> AtomicReference<T> set(AtomicReference<T> reference, DatabaseField databaseField) {
        return ResultSetService.set(new Configurations(databaseField, _resultSet), reference);
    }

    public static <T> Collection<T> add(Collection<T> collection, DatabaseField databaseField) {
        return ResultSetService.add(new Configurations(databaseField, _resultSet), collection);
    }

    public static <T> T get(DatabaseField databaseField, Class<T> type) {
        return ResultSetService.get(new Configurations(databaseField, _resultSet), type);
    }

    public static String getString(DatabaseField databaseField) {
        return ResultSetService.getString(new Configurations(databaseField, _resultSet));
    }

    public static UUID getUUID(DatabaseField databaseField) {
        return ResultSetService.getUUID(new Configurations(databaseField, _resultSet));
    }

    public static Long getLong(DatabaseField databaseField) {
        return ResultSetService.getLong(new Configurations(databaseField, _resultSet));
    }

    public static Boolean getBoolean(DatabaseField databaseField) {
        return ResultSetService.getBoolean(new Configurations(databaseField, _resultSet));
    }

    public static <T> T getTimestamp(DatabaseField databaseField, Function<Timestamp, T> function) {
        return ResultSetService.getTimestamp(new Configurations(databaseField, _resultSet), function);
    }

    public static Instant getInstant(DatabaseField databaseField) {
        return ResultSetService.getInstant(new Configurations(databaseField, _resultSet));
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

    public static void build(ResultSet resultSet, Runnable runnable, Member primary) throws SQLException {
        build(resultSet, runnable, primary);
    }

    public static void build(ResultSet resultSet, Runnable runnable, Field primary) throws SQLException {
        build(
                resultSet,
                runnable,
                id -> !getUUID(DatabaseField.of(id)).equals(id),
                primary
        );
    }

    public static void build(ResultSet resultSet, Runnable runnable, Member... primaries) throws SQLException {
        build(
                resultSet,
                runnable,
                id -> !getUUID(DatabaseField.of(id)).equals(id),
                primaries
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

    public static class ResultSetService {

        @SuppressWarnings("unchecked")
        public static <T> AtomicReference<T> set(Configurations configurations, AtomicReference<T> reference) {
            return handleConfigurations(configurations, () -> {
                try {
                    DatabaseField databaseField = configurations.field();

                    return (AtomicReference<T>) reference.getAndSet((T) (
                            databaseField.is_key() && getType(Configurations.of(configurations, NEUTRAL)) == DataType.BINARY
                                    ? getUUID(Configurations.of(configurations, NEUTRAL))
                                    : configurations.resultSet.getObject(databaseField.get_tableColumn())
                    ));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @SuppressWarnings("unchecked")
        public static <T> Collection<T> add(Configurations configurations, Collection<T> collection) {
            return handleConfigurations(configurations, () -> {
                try {
                    collection.add((T) configurations.resultSet.getObject(configurations.field().get_columnAlias()));
                    return collection;
                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            });
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
            return handleConfigurations(configurations, () -> {
                try {
                    return DataType.valueOf(configurations.resultSet.getMetaData().getColumnType(columnIndex(configurations)));
                } catch (SQLException | NameNotFoundException exception) {
                    throw new RuntimeException(exception);
                }
            });
        }

        public static void restart(ResultSet resultSet) {
            try {
                moveBackwards(resultSet, true);
            } catch (SQLException e) {
                _logger.warning(String.format("Failed to restart ResultSet: %s", e.getMessage()));
            }
        }

        public static String getString(Configurations configurations) {
            return handleConfigurations(configurations, () -> {
                try {
                    return configurations.resultSet.getString(configurations.field().get_columnAlias());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static UUID getUUID(Configurations configurations) {
            return handleConfigurations(configurations, () -> {
                try {
                    Optional<byte[]> bytes = Optional.ofNullable(configurations.resultSet.getBytes(configurations.field().get_columnAlias()));
                    return bytes.isPresent() ? UUID.nameUUIDFromBytes(bytes.orElseThrow()) : null;
                } catch (SQLException ignored) {
                    try {
                        Optional<byte[]> bytes = Optional.ofNullable(configurations.resultSet.getBytes(columnOf(configurations.field().get_columnAlias())));
                        return bytes.isPresent() ? UUID.nameUUIDFromBytes(bytes.orElseThrow()) : null;
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                }
            });
        }

        public static Integer getInteger(Configurations configurations) {
            return handleConfigurations(configurations, () -> {
                try {
                    return configurations.resultSet.getInt(configurations.field().get_columnAlias());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static Long getLong(Configurations configurations) {
            return handleConfigurations(configurations, () -> {
                try {
                    return configurations.resultSet.getLong(configurations.field().get_columnAlias());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static Boolean getBoolean(Configurations configurations) {
            return handleConfigurations(configurations, () -> {
                try {
                    return configurations.resultSet.getBoolean(configurations.field().get_columnAlias());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public static Instant getInstant(Configurations configurations) {
            return getTimestamp(configurations, Timestamp::toInstant);
        }

        public static <T> T getTimestamp(Configurations configurations, Function<Timestamp, T> function) {
            return handleConfigurations(configurations, () -> {
                try {
                    return get(configurations.resultSet.getTimestamp(configurations.field().get_columnAlias()), function);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private static <T> T handleConfigurations(Configurations configurations, Supplier<T> supplier) {
            if (configurations.resultSet == null)
                configurations.log(() -> {
                    throw new RuntimeException("ResultSet can not be null in ResultSetService!");
                });

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

        private static int columnIndex(Configurations configurations) throws NameNotFoundException {
            try {
                ResultSetMetaData metaData = configurations.resultSet.getMetaData();

                for (int i = 1; i <= metaData.getColumnCount(); i++)
                    if ((metaData.getTableName(i) + "." + metaData.getColumnName(i)).equals(configurations.field().get_columnAlias().toLowerCase()))
                        return i;

                throw new NameNotFoundException(String.format(
                        "Could not find column %s when finding its index.",
                        configurations.field()
                ));
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

        public record Configurations(DatabaseField field, ResultSet resultSet, Mode mode, Optional<Runnable> logging) {

            public Configurations(DatabaseField field, ResultSet resultSet, Mode mode) {
                this(field, resultSet, mode, Optional.empty());
            }

            public Configurations(DatabaseField databaseField, ResultSet resultSet, Mode mode, Runnable logging) {
                this(databaseField, resultSet, mode, Optional.ofNullable(logging));
            }

            public Configurations(DatabaseField databaseField, ResultSet resultSet) {
                this(databaseField, resultSet, NEUTRAL, Optional.empty());
            }

            public Configurations(DatabaseField databaseField, ResultSet resultSet, Runnable logging) {
                this(databaseField, resultSet, NEUTRAL, Optional.ofNullable(logging));
            }

            public Configurations(DatabaseField databaseField, ResultSet resultSet, Runnable logging, Mode mode) {
                this(databaseField, resultSet, mode, Optional.ofNullable(logging));
            }
            
            public void startResultSet() throws SQLException {
                if (resultSet.isBeforeFirst())
                    resultSet.next();
            }

            public void log(Runnable orElse) {
                logging().ifPresentOrElse(Runnable::run, orElse);
            }

            public static Configurations of(Configurations configurations, Mode mode) {
                return new Configurations(
                        configurations.field,
                        configurations.resultSet,
                        mode,
                        configurations.logging
                );
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

        public static String columnOf(String field) {
            return field.split("\\.")[1];
        }
    }
}
