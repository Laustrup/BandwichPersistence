package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.services.DatabaseDefinitionService.get_databaseColumns;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public abstract class DatabaseColumnService {

    public static String fieldToColumnName(String... fields) {
        return fieldToColumnName(String.join("_", fields));
    }

    public static String fieldToColumnName(String field) {
        if (field == null)
            return null;

        if (field.charAt(0) == '_')
            field = field.substring(1);

        for (int i = 0; i < field.length(); i++) {
            char character = field.charAt(i);

            if (Character.isUpperCase(character)) {
                field = field.replace(String.valueOf(character), (i != 0 ? "_" : "") + Character.toLowerCase(character));
                if (i != 0)
                    i++;
            }
        }

        return field;
    }

    public static AbstractMap.SimpleImmutableEntry<Field, DatabaseField> memberToColumnEntry(Field field) {
        return new AbstractMap.SimpleImmutableEntry<>(field, DatabaseField.of(field));
    }

    public static Map<? extends Member, DatabaseField> get_columns(Class<?> clazz) {
        if (clazz == null)
            return null;
        if (clazz.getDeclaredFields().length == 0)
            return new HashMap<>();

        Function<Field, String> fieldsMapKey = field -> stating(field.isAnnotationPresent(DatabaseEntity.Column.class))
                .then(ifNotNull(field.getAnnotation(DatabaseEntity.Column.class))
                        .get(DatabaseEntity.Column::value)
                        .orElse(null)
                ).orElse(field.getName());
        Map<String, Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toMap(fieldsMapKey, Function.identity()));

        Map<Field, DatabaseField> explicits = get_databaseColumns(clazz).stream()
                .filter(column -> column.value() != null && !column.value().isEmpty())
                .collect(Collectors.toMap(
                        column -> fields.get(column.value()),
                        column -> DatabaseField.of(clazz, column)
                ));

        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> stating(field.isAnnotationPresent(DatabaseEntity.Column.class))
                        .then(!ifNotNull(field.getAnnotation(DatabaseEntity.ExcludedColumn.class))
                                .then(true)
                                .orElse(false))
                        .orElse(true)
                ).map(field -> stating(explicits.containsKey(field))
                        .then(new AbstractMap.SimpleImmutableEntry<>(field, explicits.get(field)))
                        .orElse(memberToColumnEntry(field))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
