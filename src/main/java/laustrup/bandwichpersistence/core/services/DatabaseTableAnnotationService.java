package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.DatabaseTable;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public class DatabaseTableAnnotationService {

    public static String get_tableTitle(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, () -> clazz.getAnnotation(DatabaseTable.class).title());
    }

    public static String get_tableTitle(Field field) {
        if (!field.isAnnotationPresent(DatabaseTable.class))
            throw new IllegalStateException(String.format(
                    "Field %s is not annotated with @Table and therefore can't get table!",
                    field.getName()
            ));

        return field.getAnnotation(DatabaseTable.class).title();
    }

    public static String get_tableTitle(Class<?> clazz, String tableName) {
        try {
            return get_tableTitle(clazz.getDeclaredField(tableName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toAlias(String tableName) {
        boolean underscoreReached = false;

        for (int i = 0; i < tableName.length(); i++) {
            char character = tableName.charAt(i);
            if (character == '_') {
                underscoreReached = true;
                tableName = tableName.substring(0, i) + tableName.substring(i + 1);
                i-= 1;
            } else if (underscoreReached) {
                tableName = tableName.substring(0, i) + String.valueOf(character).toUpperCase() + tableName.substring(i + 1);
                underscoreReached = false;
            }
        }

        return tableName;
    }

    public static String getDatabaseIdReference(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, () ->
            ifNotEmpty(clazz.getAnnotation(DatabaseTable.class).idReference())
                    .otherwise("")
        );
    }

    public static String getDatabaseIdReference(Field field) {
        return ifAnnotationIsPresent(field, () ->
                ifNotEmpty(field.getAnnotation(DatabaseTable.class).idReference())
                        .otherwise("")
        );
    }

    public static DatabaseTable.Properties getDatabaseTableProperties(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, () -> new DatabaseTable.Properties(
                clazz,
                get_tableTitle(clazz),
                getDatabaseIdReference(clazz)
        ));
    }

    public static DatabaseTable.Properties getDatabaseTableProperties(Field field) {
        return ifAnnotationIsPresent(field, () -> new DatabaseTable.Properties(
                field,
                get_tableTitle(field),
                getDatabaseIdReference(field)
        ));
    }

    private static <RETURN> RETURN ifAnnotationIsPresent(
            Field field,
            Supplier<RETURN> logic
    ) {
        if (!field.isAnnotationPresent(DatabaseTable.class))
            throw new IllegalStateException(String.format(
                    "Field %s is not annotated with @Table and therefore can't get table!",
                    field.getName()
            ));

        return logic.get();
    }

    private static <RETURN> RETURN ifAnnotationIsPresent(
            Class<?> clazz,
            Supplier<RETURN> logic
    ) {
        if (!clazz.isAnnotationPresent(DatabaseTable.class))
            throw new IllegalStateException(String.format(
                    "Class %s is not annotated with @Table and therefore can't get table!",
                    clazz.getSimpleName()
            ));

        return logic.get();
    }
}
