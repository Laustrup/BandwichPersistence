package laustrup.bandwichpersistence.core.repositories.queries;

import laustrup.bandwichpersistence.core.persistence.Query;
import lombok.Getter;

import java.util.stream.Stream;

public class UserDetailsQueries {

    private static final String _selectAll = /*language=mysql*/ """
            select
                *
            from
                contact_info
                    left join artists
                        on contact_info.id = artists.contact_info_id
                    left join band_memberships
                        on artists.id = band_memberships.artist_id
                    left join bands
                        on band_memberships.band_id = bands.id
                    left join organisation_employees
                        on contact_info.id = organisation_employees.contact_info_id
            """;

    public static Query selectAllForLogin = new Query(/*language=mysql*/ _selectAll + """
            where
                contact_info.email = %s and
                (artists.password = %s || organisation_employees.password = %s)
            """,
            Stream.of(
                    new Query.Parameter(Parameter.CONTACT_INFO_EMAIL.get_key()),
                    new Query.Parameter(Parameter.PASSWORD.get_key()),
                    new Query.Parameter(Parameter.PASSWORD.get_key())
            )
    );

    public static Query selectAllForLogins = new Query(_selectAll);

    @Getter
    public enum Parameter {
        CONTACT_INFO_EMAIL("contact_info_email"),
        PASSWORD("password");

        private final String _key;

        Parameter(String key) {
            _key = Query.formatKey(key);
        }
    }
}
