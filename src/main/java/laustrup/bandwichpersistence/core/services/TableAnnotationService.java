package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Table;

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
}
