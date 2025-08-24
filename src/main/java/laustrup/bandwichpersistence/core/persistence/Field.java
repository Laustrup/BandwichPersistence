package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.models.DatabaseTable;

import java.util.Arrays;

import static laustrup.bandwichpersistence.core.services.DatabaseTableAnnotationService.toAlias;

public record Field(String alias, String row) {

    private static final String[] _idIndicators = new String[]{"id", "_id"};

    public static Field of(DatabaseTable.Properties databaseTableProperties) {
        return of(databaseTableProperties.get_title(), "id");
    }

    public static Field of(DatabaseTable.Properties databaseTableProperties, String referenceId) {
        return of(toAlias(databaseTableProperties.get_title()), referenceId);
    }

    public static Field of(String alias, String row) {
        return new Field(alias, row);
    }

    public static Field of(String row) {
        return new Field(null, row);
    }

    public String get_content() {
        return alias != null
                ? String.format("%s.%s", alias, row)
                : row
        ;
    }

    public boolean is_key() {
        return Arrays.stream(_idIndicators).anyMatch(row::contains);
    }
}
