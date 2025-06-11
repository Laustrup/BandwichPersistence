package laustrup.bandwichpersistence.core.persistence;

import java.util.Arrays;

public record Field(String table, String row) {

    private static final String[] _idIndicators = new String[]{"id", "_id"};

    public static Field of(String alias, String row) {
        return new Field(alias, row);
    }

    public static Field of(String row) {
        return new Field(null, row);
    }

    public String get_content() {
        return table != null
                ? String.format("%s.%s", table, row)
                : row
        ;
    }

    public boolean is_key() {
        return Arrays.stream(_idIndicators).anyMatch(row::contains);
    }
}
