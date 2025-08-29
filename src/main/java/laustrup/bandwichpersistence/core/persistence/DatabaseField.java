package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityData;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;

import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.toAlias;

public record DatabaseField(String alias, String row) {

    private static final String[] _idIndicators = new String[]{"id", "_id"};

    public static DatabaseField of(DatabaseEntityData configurationProperties) {
        return of(toAlias(configurationProperties.get_title()), "id");
    }

    public static DatabaseField of(DatabaseEntityData configurationsProperties, String referenceId) {
        return of(toAlias(configurationsProperties.get_title()), referenceId);
    }

    public static DatabaseField of(String alias, String row) {
        return new DatabaseField(alias, row);
    }

    public static DatabaseField of(String row) {
        return new DatabaseField(null, row);
    }

    public static DatabaseField of(Map.Entry<Member, String> entry) {
        return new DatabaseField(toAlias(entry.getKey().getName()), entry.getValue());
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
