package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Table;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;

public class TableAnnotationService {

    public static String get_tableTitle(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Table.class))
            throw new IllegalStateException(
                    String.format(
                            "Class %s is not annotated with @Table and therefore can't get table!",
                            clazz.getSimpleName()
                    ));

        return ContactInfo.class.getAnnotation(Table.class).title();
    }
}
