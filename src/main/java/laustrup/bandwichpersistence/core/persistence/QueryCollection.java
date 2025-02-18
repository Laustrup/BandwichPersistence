package laustrup.bandwichpersistence.core.persistence;

import lombok.Getter;

import java.util.List;

@Getter
public class QueryCollection {

    private List<Query> _queries;

    private List<DatabaseParameter> _parameters;

    public QueryCollection(List<Query> queries, List<DatabaseParameter> parameters) {
        queries = queries;
        _parameters = parameters;
    }
}
