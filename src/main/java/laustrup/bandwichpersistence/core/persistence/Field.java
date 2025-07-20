package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.models.ConjunctionTable;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseTable;

import java.util.Arrays;

public record Field(String alias, String row) {

    private static final String[] _idIndicators = new String[]{"id", "_id"};

    public static Field of(ConjunctionTable table, String row) {
        return of(table, row);
    }

    public static Field of(DatabaseTable table, String row) {
        return of(table.get_alias(), row);
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
