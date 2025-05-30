package laustrup.bandwichpersistence.core.persistence.queries;

import laustrup.bandwichpersistence.core.persistence.models.Query;
import lombok.Getter;

import java.util.Arrays;

public class ScriptorianQueries {

    public static final Query createTableIfNotExists = new Query(/*language=MySQL*/ """
    create table if not exists scriptories(
        title varchar(128) not null,
        file_name varchar(256) not null,
        error_message mediumtext,
        content mediumtext not null,
        versionstamp datetime not null,
        successstamp datetime,
        createdstamp datetime,
        timestamp datetime not null default now(),

        constraint PK_scriptories
            primary key (file_name)
    );
    """);

    public static final Query findScriptoriesWithoutSuccess = new Query(/*language=MySQL*/ """
    select * from scriptories where successstamp is null;
    """);

    public static final Query findAllScriptories = new Query(/*language=MySQL*/ """
    select * from scriptories;
    """);

    public static final Query insertIntoScriptories = new Query(
            String.format(/*language=MySQL*/ """
                    insert ignore into scriptories(
                        title,
                        file_name,
                        error_message,
                        content,
                        versionstamp,
                        successstamp,
                        createdstamp
                    ) values %s;
                    """,
                    Query.valuesInsertCollection(Parameter.values().length)
            ),
            Arrays.stream(Parameter.values())
                    .map(parameter -> new Query.Parameter(parameter.get_key()))
    );

    @Getter
    public enum Parameter {
        TITLE("scriptorian_title"),
        FILE_NAME("scriptorian_file_name"),
        ERROR_MESSAGE("scriptorian_error_message"),
        CONTENT("scriptorian_content"),
        VERSIONSTAMP("scriptorian_versionstamp"),
        SUCCESSSTAMP("scriptorian_successstamp"),
        CREATEDSTAMP("scriptorian_createdstamp");

        private final String _key;

        Parameter(String key) {
            _key = Query.formatKey(key);
        }

        @Override
        public String toString() {
            return _key;
        }
    }
}
