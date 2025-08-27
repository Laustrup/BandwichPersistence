package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityConfigurations;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static Map<Field, String> getColumns(DatabaseEntityConfigurations configurations) {
        if (configurations == null)
            throw new NullPointerException("Can't get columns of a configurations that are null...");

        Map<Field, String>
                explicits = configurations.get_explicits(),
                exclusions = configurations.get_exclusions(),
                additions = configurations.get_additions(),
                mappings = new HashMap<>();

        Function<Field, Stream<Map.Entry<Field, String>>> mapping = field -> {
            mappings.entrySet().add(stating(explicits != null && explicits.containsKey(field))
                    .then(new AbstractMap.SimpleEntry<>(field, explicits.get(field)))
                    .orElse(fieldToColumnEntry(field))
            );

            return mappings.entrySet().stream();
        };

        Map<Field, String> columns = Arrays.stream(configurations.getClass().getDeclaredFields())
                .flatMap(mapping)
                .filter(exclusions::containsKey)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        columns.putAll(additions);

        return columns;
    }
}
