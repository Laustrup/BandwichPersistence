package laustrup.bandwichpersistence.core.services.persistence.queries;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class JoinTableScriptService {

    public static String insertIntoJoinTable(String table, Inputs inputs) {
        return /*language=mysql*/ String.format(
                "insert %s into %s(%s) values (%s);",
                inputs.is_ignore() ? "ignore" : "",
                table,
                inputs.get_columns(),
                inputs.get_values()
        );
    }

    public static class Inputs {

        private List<String> _columns;

        @Getter
        private boolean _ignore;

        public Inputs(List<String> columns) {
            this(columns, false);
        }

        public Inputs(List<String> columns, boolean ignore) {
            _columns = columns;
            _ignore = ignore;
        }

        public String get_columns() {
            return reduce(_columns);
        }

        public String get_values() {
            List<String> values = new ArrayList<>();

            for (String ignored : _columns)
                values.add("%s");

            return reduce(values);
        }

        private String reduce(List<String> collection) {
            return collection.stream()
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }
    }
}
