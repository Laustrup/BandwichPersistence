package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;


import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

class BuilderService {

    static void printError(Class<?> origin, AtomicReference<UUID> id, Exception exception, Logger logger) throws RuntimeException {
        printError(origin, id.get(), exception, logger);
    }

    static void printError(Class<?> origin, UUID id, Exception exception, Logger logger) throws RuntimeException {
        logger.warning(String.format(
                "Could not build %s of %s:\n%s",
                origin.getSimpleName(),
                id,
                exception.getMessage()
        ));
        throw new RuntimeException(exception);
    }

    static <T> T handle(Class<T> clazz, Function<Function<String, Field>, T> action) {
        return handle(toTableName(clazz), action);
    }

    static <T> T handle(String table, Function<Function<String, Field>, T> action) {
        return action.apply(row -> Field.of(table, row));
    }

    static String toTableName(Class<?> clazz) {
        return toTableName(clazz.getSimpleName());
    }

    static String toTableName(String table) {
        return table + "s";
    }
}
