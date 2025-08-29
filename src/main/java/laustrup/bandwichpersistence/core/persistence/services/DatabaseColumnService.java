package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseRow;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.get_databaseRows;
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

    public static AbstractMap.SimpleEntry<Field, String> fieldToColumnEntry(Field field) {
        return new AbstractMap.SimpleEntry<>(field, fieldToColumnName(field.getName()));
    }

    public static Map<Member, String> getColumns(Class<?> clazz) {
        Function<Field, String> fieldsMapKey = field -> stating(field.isAnnotationPresent(DatabaseRow.class))
                .then(field.getAnnotation(DatabaseRow.class).title())
                .orElse(field.getName());
        Map<String, Field> fields = Arrays.stream(clazz.getFields())
                .collect(Collectors.toMap(fieldsMapKey, Function.identity()));
        Seszt<DatabaseRow> databaseRows = get_databaseRows(clazz);

        Map<Field, String>
                explicits = databaseRows.stream()
                        .filter(row -> row.title() != null && !row.title().isEmpty())
                        .collect(Collectors.toMap(row -> fields.get(row.title()), DatabaseRow::title)),
                mappings = new HashMap<>();

        Function<Field, Stream<Map.Entry<Field, String>>> mapping = field -> {
            mappings.entrySet().add(stating(explicits.containsKey(field))
                    .then(new AbstractMap.SimpleEntry<>(field, explicits.get(field)))
                    .orElse(fieldToColumnEntry(field))
            );

            return mappings.entrySet().stream();
        };

        return Arrays.stream(clazz.getDeclaredFields())
                .filter(filter -> stating(filter.isAnnotationPresent(DatabaseRow.class))
                        .then(!filter.getAnnotation(DatabaseRow.class).exclude())
                        .orElse(true)
                ).flatMap(mapping)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
