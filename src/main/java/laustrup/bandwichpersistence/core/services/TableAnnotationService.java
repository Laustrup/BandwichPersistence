package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Table;

import java.lang.reflect.Field;

public class TableAnnotationService {

    public static String get_tableTitle(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Table.class))
            throw new IllegalStateException(
                    String.format(
                            "Class %s is not annotated with @Table and therefore can't get table!",
                            clazz.getSimpleName()
                    ));

        return clazz.getAnnotation(Table.class).title();
    }

    public static String get_tableTitle(Field field) {
        if (!field.isAnnotationPresent(Table.class))
            throw new IllegalStateException(
                    String.format(
                            "Field %s is not annotated with @Table and therefore can't get table!",
                            field.getName()
                    ));

        return field.getAnnotation(Table.class).title();
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
}
