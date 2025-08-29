package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityData;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseRow;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineIdReference;

public class DatabaseEntityConfigurationsService {

    private static DatabaseEntity get_databaseEntityAnnotation(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, clazz.getAnnotation(DatabaseEntity.class));
    }

    public static String get_tableTitle(Class<?> clazz) {
        return get_databaseEntityAnnotation(clazz).title();
    }

    public static String get_idReference(Class<?> clazz) {
        return defineIdReference(get_databaseEntityAnnotation(clazz).idReference());
    }

    public static Seszt<DatabaseRow> get_databaseRows(Class<?> clazz) {
        return new Seszt<>(Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DatabaseRow.class))
                .map(field -> field.getAnnotation(DatabaseRow.class))
        );
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

    public static DatabaseEntityData getDatabaseEntityFromEnum(Class<? extends Enum<?>> clazz) {
        return ifAnnotationIsPresent(clazz, new DatabaseEntityData(clazz));
    }

    private static <RETURN> RETURN ifAnnotationIsPresent(
            Field field,
            Class<? extends Annotation> annotation,
            RETURN element
    ) {
        if (!field.isAnnotationPresent(annotation))
            throw new IllegalStateException(String.format(
                    "Field %s is not annotated with @Table and therefore can't get table!",
                    field.getName()
            ));

        return element;
    }

    private static <RETURN> RETURN ifAnnotationIsPresent(Class<?> clazz, RETURN element) {
        if (!clazz.isAnnotationPresent(DatabaseEntity.class))
            throw new IllegalStateException(String.format(
                    "Class %s is not annotated with @Table and therefore can't get table!",
                    clazz.getSimpleName()
            ));

        return element;
    }

    public static Map<DatabaseField, String> classFieldToDatabaseField(Map<Member, String> fields) {
        return fields.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(DatabaseField.of(entry), entry.getValue()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }
}
