package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.persistence.models.DTODatabaseConfigurations;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityConfigurations;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static laustrup.bandwichpersistence.core.services.MethodService.invoke;

public class DatabaseEntityConfigurationsService {

    public static String get_tableTitle(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, clazz.getAnnotation(DatabaseEntity.class).title());
    }

    public static String get_tableTitle(Field field) {
        if (!field.isAnnotationPresent(DatabaseEntity.class))
            throw new IllegalStateException(String.format(
                    "Field %s is not annotated with @Table and therefore can't get table!",
                    field.getName()
            ));

        return field.getAnnotation(DatabaseEntity.class).title();
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

    public static DatabaseEntityConfigurations.Data getDatabaseEntityFromEnum(Class<? extends Enum<?>> clazz) {
        return ifAnnotationIsPresent(
                clazz,
                new DatabaseEntityConfigurations.Data(
                        new DTODatabaseConfigurations(get_tableTitle(clazz)),
                        get_tableTitle(clazz)
                )
        );
    }

    public static DatabaseEntityConfigurations.Data get_databaseEntityData(
            Class<? extends DatabaseEntityConfigurations> configurations
    ) {
        return new DatabaseEntityConfigurations.Data(get_configurations(configurations), get_tableTitle(configurations));
    }

    public static DatabaseEntityConfigurations get_configurations(
            Class<? extends DatabaseEntityConfigurations> configurations
    ) {
        return invoke(configurations, "get_databaseEntityConfigurations");
    }

    private static <RETURN> RETURN ifAnnotationIsPresent(
            Field field,
            Supplier<RETURN> logic
    ) {
        if (!field.isAnnotationPresent(DatabaseEntity.class))
            throw new IllegalStateException(String.format(
                    "Field %s is not annotated with @Table and therefore can't get table!",
                    field.getName()
            ));

        return logic.get();
    }

    private static <RETURN> RETURN ifAnnotationIsPresent(
            Class<?> clazz,
            RETURN element
    ) {
        if (!clazz.isAnnotationPresent(DatabaseEntity.class))
            throw new IllegalStateException(String.format(
                    "Class %s is not annotated with @Table and therefore can't get table!",
                    clazz.getSimpleName()
            ));

        return element;
    }
}
