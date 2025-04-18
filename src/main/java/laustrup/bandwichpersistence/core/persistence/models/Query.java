package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class Query {

    private final String _script;

    private Stream<Parameter> _parameters;

    @Getter
    private static final char _identifier = '€';

    @Getter
    private static final char _endExpression = ' ';

    public Query(String script) {
        this(script, Stream.empty());
    }

    public Query(String script, Parameter parameter) {
        this(script, Stream.of(parameter));
    }

    public Query(Collection<Query> queries) {
        this(
                queries.stream()
                        .map(Query::get_script)
                        .reduce(String::concat)
                        .orElseThrow()
        );
    }

    public Query(String script, Stream<Parameter> parameters) {
        _script = String.format(script, parameters.map(Parameter::get_key).toArray());
        _parameters = parameters;
    }

    public static String formatKey(String key) {
        return String.format(
                "%s%s%s",
                Query.get_identifier(),
                key,
                Query.get_endExpression()
        );
    }

    public static String valuesInsertCollection(int parameterCount) {
        List<String> parameters = new ArrayList<>();

        for (int i = 0; i < parameterCount; i++)
            parameters.add("%s");

        return String.format("(%s)", String.join(", ", parameters));
    }

    public static String formatIndexedKey(String parameter, int index) {
        return String.format("%s_%s", parameter, index).replace(" ", "") + " ";
    }

    public String get_script() {
        return DatabaseLibrary.isH2InMemory() ? h2Script() : _script;
    }

    private String h2Script() {
        return _script
                .replace("database", "schema");
    }

    @Getter
    public static class Parameter {

        private Integer _index;

        private String _title;

        private String _key;

        public Parameter(String title) {
            _title = title;
            _key = title.contains(String.valueOf(Query.get_identifier())) ? title : Query.formatKey(title);
        }

        public Parameter(Integer index, String parameter) {
            _index = index;
            _title = parameter;
            _key = _index == null ? _title : formatIndexedKey(parameter, _index);
        }

        @Override
        public String toString() {
            return _key;
        }
    }

    @Override
    public String toString() {
        return _script;
    }
}
