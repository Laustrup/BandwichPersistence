package laustrup.bandwichpersistence.core.persistence.services;

import java.util.List;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public abstract class DatabaseTableService {

    public static String defineTitle(String target, String common) {
        return String.join("_", List.of(pluralToSingular(target), common));
    }

    public static String defineTitle(String entity) {
        return entity.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    public static String pluralToSingular(String title) {
        if (title == null || title.length() < 2)
            throw new IllegalArgumentException("Entity title is empty when trying to make it singular");

        String ending = title.substring(title.length() - 3);
        return stating(title.length() > 4 && ending.equals("ies"))
                .then(title.substring(0, title.length() - 3) + "y")
                .or(title.substring(0, title.length() - 2), ignored -> ending.endsWith("ses"))
                .orElse(() -> stating(ending.endsWith("s"))
                        .then(title.substring(0, title.length() - 1))
                        .orElse(title)
                );
    }

    public static String defineIdReference(String title) {
        return defineIdReference(title, "");
    }

    public static String defineIdReference(String title, String idReference) {
        return idReference != null && !idReference.isEmpty()
                ? idReference
                : fieldToColumnName(pluralToSingular(title), "id");
    }
}
