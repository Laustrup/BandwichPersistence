package laustrup.bandwichpersistence.core.persistence.worm.services;

import laustrup.bandwichpersistence.core.persistence.models.members.CommonField;
import laustrup.bandwichpersistence.core.persistence.models.members.InheritanceField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseEntity;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseJunction;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseTableService.defineTitle;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getDeclared;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

@Slf4j
public class DatabaseDefinitionService {

    public static DatabaseEntity get_databaseEntity(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, DatabaseEntity.class, clazz.getAnnotation(DatabaseEntity.class));
    }

    public static DatabaseEntity.Column get_entityColumn(Class<?> entity, String columnName) {
        return get_entityColumn(getDeclared(entity, columnName));
    }

    public static DatabaseEntity.Column get_entityColumn(Member member) {
        if (member == null) {
            log.warn("Member is null when getting the entity column, which it shouldn't!");
            return null;
        }
        return ifAnnotationIsPresent(member, fieldOfMember(member).getAnnotation(DatabaseEntity.Column.class));
    }

    public static Field fieldOfMember(Member member) {
        if (member instanceof InheritanceField)
            return ((InheritanceField) member).get_reflection();
        else if (member instanceof CommonField)
            return ((CommonField) member).get_reflection();
        else
            return (Field) member;
    }

    public static DatabaseJunction get_databaseJunction(Class<?> clazz) {
        return ifAnnotationIsPresent(clazz, DatabaseJunction.class, clazz.getAnnotation(DatabaseJunction.class));
    }

    public static String get_tableTitle(Class<?> clazz) {
        return ifNotEmpty(get_databaseEntity(clazz).value())
                .otherwise(defineTitle(clazz.getSimpleName()));
    }

    public static String get_idReference(Class<?> clazz) {
        return get_idReference(get_databaseEntity(clazz));
    }

    public static String get_idReference(DatabaseEntity entity) {
        String idReference = entity.idReference().title();

        return ifNotEmpty(idReference)
                .otherwise(String.join("_", entity.value(), "_id"));
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
        if (tableName == null) {
            log.warn("Table name is null when creating alias, which it should not be!");
            return null;
        }

        boolean underscoreReached = false;
        String alias = tableName;

        for (int i = 0; i < alias.length(); i++) {
            char character = alias.charAt(i);
            if (character == '_') {
                underscoreReached = true;
                alias = alias.substring(0, i) + alias.substring(i + 1);
                i-= 1;
            } else if (underscoreReached) {
                alias = alias.substring(0, i) + String.valueOf(character).toUpperCase() + alias.substring(i + 1);
                underscoreReached = false;
            }
        }

        if (alias.equals(tableName)) {
            char[] chars = alias.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            alias = "_" + new String(chars);
        }

        return alias;
    }

    private static DatabaseEntity.Column ifAnnotationIsPresent(Member member, DatabaseEntity.Column element) {
        return stating(fieldOfMember(member).isAnnotationPresent(DatabaseEntity.Column.class))
                .then(element)
                .orElse(get_defaultDatabaseEntityColumn(member.getName()));
    }

    private static DatabaseEntity.Column get_defaultDatabaseEntityColumn(String memberName) {
        return new DatabaseEntity.Column() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return DatabaseEntity.Column.class;
            }

            @Override
            public String value() {
                return fieldToColumnName(memberName);
            }

            @Override
            public boolean isPrimary() {
                return false;
            }
        };
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
