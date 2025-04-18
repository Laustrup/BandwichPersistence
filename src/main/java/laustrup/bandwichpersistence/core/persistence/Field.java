package laustrup.bandwichpersistence.core.persistence;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class Field {

    private final String _table;

    private final String _row;

    private final String[] _idIndicators = new String[]{"id", "_id"};

    public Field(String table, String row) {
        _table = table;
        _row = row;
    }

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
