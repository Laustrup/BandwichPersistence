package laustrup.bandwichpersistence.core.persistence.services;

public class DatabaseColumnService {

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
}
