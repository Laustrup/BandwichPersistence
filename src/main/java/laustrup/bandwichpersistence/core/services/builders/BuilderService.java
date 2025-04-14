package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.services.ModelService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

abstract class BuilderService<E> {

    private final BuilderService<E> _instance;

    private final Logger _logger;

    protected BuilderService(BuilderService<E> instance, Logger logger) {
        _instance = instance;
        _logger = logger;
    }

    static <M> void printError(Class<?> origin, AtomicReference<M> id, Exception exception, Logger logger) throws RuntimeException {
        printError(origin, id.get(), exception, logger);
    }

    static <M> void printError(Class<?> origin, M id, Exception exception, Logger logger) throws RuntimeException {
        logger.warning(String.format(
                "Could not build %s of %s:\n%s",
                origin.getSimpleName(),
                id,
                exception.getMessage()
        ));
        throw new RuntimeException(exception);
    }

    protected <M> void printError(AtomicReference<M> id, Exception exception) throws RuntimeException {
        printError(id.get(), exception);
    }

    protected <M> void printError(M id, Exception exception) throws RuntimeException {
        printError(_instance.getClass(), id, exception, _logger);
    }

    protected E handle(Function<Function<String, Field>, E> action) {
        return handle(toTableName(_instance.getClass()), action);
    }

    static <E> E handle(Class<E> clazz, Function<Function<String, Field>, E> action) {
        return handle(toTableName(clazz), action);
    }

    static <E> E handle(String table, Function<Function<String, Field>, E> action) {
        return action.apply(row -> Field.of(table, row));
    }

    static String toTableName(Class<?> clazz) {
        return toTableName(clazz.getSimpleName());
    }

    static String toTableName(String table) {
        return table + "s";
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

    protected <M> void combine(Seszt<M> collection, M entity) {
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

    public <M> void interaction(ResultSet resultSet, Runnable action) {
        JDBCService.build(resultSet, () -> {
            action.run();
            return null;
        });
    }
}
