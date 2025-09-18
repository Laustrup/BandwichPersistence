package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseJunction;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineTitle;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;

public class DatabaseDefinitionService {

    private static DatabaseEntity get_databaseEntityAnnotation(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, DatabaseEntity.class, clazz.getAnnotation(DatabaseEntity.class));
    }

    public static DatabaseEntity.Column get_databaseEntityColumn(Member member) {
        return ifAnnotationIsPresent(member, ((Field) member).getAnnotation(DatabaseEntity.Column.class));
    }

    public static DatabaseJunction get_databaseJunction(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, DatabaseJunction.class, clazz.getAnnotation(DatabaseJunction.class));
    }

    public static String get_tableTitle(Class<?> clazz) {
        return ifNotEmpty(get_databaseEntityAnnotation(clazz).value())
                .otherwise(defineTitle(clazz.getSimpleName()));
    }

    public static String get_idReference(Class<?> clazz) {
        DatabaseEntity databaseEntity = get_databaseEntityAnnotation(clazz);
        String idReference = databaseEntity.idReference().title();

        return ifNotEmpty(idReference)
                .otherwise(String.join("_", databaseEntity.value(), "_id"));
    }

    public static Seszt<DatabaseEntity.Column> get_databaseColumns(Class<?> clazz) {
        return new Seszt<>(Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DatabaseEntity.Column.class))
                .map(field -> field.getAnnotation(DatabaseEntity.Column.class))
        );
    }

    public static String get_columnTitle(Member member) {
        Field field = (Field) member;
        
        if (!field.isAnnotationPresent(DatabaseEntity.class))
            throw new IllegalStateException(String.format(
                    "Field %s is not annotated with @Table and therefore can't get table!",
                    member.getName()
            ));

        return field.getAnnotation(DatabaseEntity.class).value();
    }

    public static String get_tableTitle(Class<?> clazz, String tableName) {
        try {
            return get_columnTitle(clazz.getDeclaredField(tableName));
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

    private static <RETURN> RETURN ifAnnotationIsPresent(
            Member member,
            RETURN element
    ) {
        if (!((Field) member).isAnnotationPresent(DatabaseEntity.Column.class))
            new DatabaseEntity.Column() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return DatabaseEntity.Column.class;
                }

                @Override
                public String value() {
                    return fieldToColumnName(member.getName());
                }

                @Override
                public boolean isPrimary() {
                    return false;
                }
            };

        return element;
    }

    private static <RETURN> RETURN ifAnnotationIsPresent(Class<?> clazz, Class<? extends Annotation> annotation, RETURN element) {
        if (!clazz.isAnnotationPresent(annotation))
            throw new IllegalStateException(String.format(
                    "Class %s is not annotated with @Table and therefore can't get table!",
                    clazz.getSimpleName()
            ));

        return element;
    }
}
