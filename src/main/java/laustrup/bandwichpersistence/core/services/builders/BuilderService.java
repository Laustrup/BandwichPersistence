package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.services.ModelService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

abstract class BuilderService<E> {

    private final Class<E> _class;

    private final String _tableName;

    private final Logger _logger;

    private static final Seszt<Character> pluralEndingCharacters = new Seszt<>(new Character[]{'y'});

    protected BuilderService(Class<E> clazz, Logger logger) {
        _class = clazz;
        _tableName = toTableName(clazz);
        _logger = logger;
    }

    protected BuilderService(Class<E> clazz, String tableName, Logger logger) {
        _class = clazz;
        _tableName = toTableName(tableName);
        _logger = logger;
    }

    protected BuilderService(Class<E> clazz, Supplier<String> tableName, Logger logger) {
        _class = clazz;
        _tableName = tableName.get();
        _logger = logger;
    }
    protected static String classToTableName(Class<?>... classes) {
        return classToTableName(Arrays.stream(classes).map(Class::getSimpleName).toArray(String[]::new));
    }

    protected static String classToTableName(String... titles) {
        return String.join("", titles);
    }

    static <M> void printError(Class<?> origin, AtomicReference<M> id, Exception exception, Logger logger) throws RuntimeException {
        printError(origin, id.get(), exception, logger);
    }

    static <M> void printError(Class<?> origin, M id, Exception exception, Logger logger) throws RuntimeException {
        printError(origin.getSimpleName(), id, exception, logger);
    }

    static <M> void printError(String _simpleClassName, M id, Exception exception, Logger logger) {
        logger.warning(String.format(
                "Could not build %s of %s:\n%s",
                _simpleClassName,
                id,
                exception.getMessage()
        ));
        throw new RuntimeException(exception);
    }

    protected <M> void printError(AtomicReference<M> id, Exception exception) throws RuntimeException {
        printError(id.get(), exception);
    }

    protected <M> void printError(M id, Exception exception) throws RuntimeException {
        printError(_class.getSimpleName(), id, exception, _logger);
    }

    protected E handle(Function<Function<String, Field>, E> action) {
        return handle(_tableName, action);
    }

    static <E> E handle(String table, Function<Function<String, Field>, E> action) {
        return action.apply(row -> Field.of(table, row));
    }

    static String toTableName(Class<?> clazz) {
        return toTableName(clazz.getSimpleName());
    }

    static String toTableName(String table) {
        if (table == null)
            throw new IllegalArgumentException("Table name cannot be null");

        return table.charAt(table.length() - 1) == 's' ? table : (
                pluralEndingCharacters.contains(table.charAt(table.length() - 1))
                        ? table.substring(0, table.length() - 1) + "ies" : table + "s"
        );
    }

    public E build(ResultSet resultSet) {
        return handle(logic(resultSet));
    }

    public void complete(AtomicReference<E> reference, ResultSet resultSet) {
        this.complete(reference, this.build(resultSet));
    }

    public void complete(AtomicReference<E> reference, E object) {
        if (reference.get() == null) {
            reference.set(object);
            return ;
        }

        completion(reference.get(), object);
    }

    protected abstract void completion(E reference, E object);

    protected abstract Function<Function<String, Field>, E> logic(ResultSet resultSet);

    protected <M> void combine(Seszt<M> collection, Seszt<M> entities) {
        entities.forEach(entity -> combine(collection, entity));
    }

    public <M> Seszt<M> combine(Seszt<M> collection, M entity) {
        AtomicBoolean isIdentical = new AtomicBoolean(true);
        AtomicInteger counter = new AtomicInteger(0);

        collection.forEach(m -> {
            if (ModelService.equals(m, entity)) {
                collection.get_data()[counter.get()] = entity;
                return ;
            }
            counter.incrementAndGet();
        });

        if (!isIdentical.get())
            collection.add(entity);

        return collection;
    }

    protected void interaction(ResultSet resultSet, Runnable action, AtomicReference<UUID> id) {
        interaction(resultSet, action, id.get());
    }

    protected void interaction(ResultSet resultSet, Runnable action, UUID id) {
        try {
            JDBCService.build(resultSet, action, id);
        } catch (SQLException exception) {
            printError(id, exception);
        }
    }

    @SuppressWarnings("unchecked")
    public <M> void interaction(ResultSet resultSet, Runnable action, Function<M, Boolean> breaker, M... id) {
        try {
            JDBCService.build(resultSet, action, breaker, id);
        } catch (SQLException exception) {
            printError(id, exception);
        }
    }

    public void interaction(ResultSet resultSet, Runnable action) {
        JDBCService.build(resultSet, () -> {
            action.run();
            return null;
        });
    }
}
