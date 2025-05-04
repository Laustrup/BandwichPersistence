package laustrup.bandwichpersistence.core.persistence;

import java.util.Arrays;

public record Field(String _table, String _row) {

    private static final String[] _idIndicators = new String[]{"id", "_id"};

    public static Field of(String table, String row) {
        return new Field(table, row);
    }

    public String get_content() {
        return String.format("%s.%s", _table, _row);
    }

    public boolean is_key() {
        return Arrays.stream(_idIndicators).anyMatch(_row::contains);
    }
}
