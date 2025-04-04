package laustrup.bandwichpersistence.core.repositories.queries;

import laustrup.bandwichpersistence.core.persistence.models.Query;
import lombok.Getter;

public class UserDetailsQueries {

    private static final String _selectAll = /*language=mysql*/ """
            select
                *
            from
                contact_info
                    left join phones
                        on phones.contact_info_id = contact_info.id
                    left join addresses
                        on addresses.id = contact_info.address_id
                    left join countries
                        on countries.id = contact_info.country_id
                    left join artists
                        on contact_info.id = artists.contact_info_id
                    left join band_memberships
                        on artists.id = band_memberships.artist_id
                    left join bands
                        on band_memberships.band_id = bands.id
                    left join organisation_employees
                        on contact_info.id = organisation_employees.contact_info_id
                    left join organisation_employments
                        on organisation_employees.id = organisation_employments.organisation_employee_id
                    left join organisation_employee_authorities
                        on organisation_employees.id = organisation_employee_authorities.organisation_employee_id
                    left join artist_authorities
                        on artists.id = artist_authorities.artist_id
                    left join authorities
                        on authorities.id = artist_authorities.authority_id || organisation_employee_authorities.authority_id = authorities.id
                    left join subscriptions
                        on artists.subscription_id = subscriptions.id || organisation_employees.subscription_id = subscriptions.id
                    left join organisation_employee_chat_rooms
                        on organisation_employees.id = organisation_employee_chat_rooms.organisation_employee_id
                    left join artist_chat_rooms
                        on artists.id = artist_chat_rooms.artist_id
                    left join chat_rooms
                        on artist_chat_rooms.chat_room_id = chat_rooms.id || organisation_employee_chat_rooms.chat_room_id = chat_rooms.id
                    left join messages
                        on chat_rooms.id = messages.chat_room_id
            """;

    public static Query selectAllForLogins = new Query(_selectAll);

    public static Query selectAllForLogin = new Query(/*language=mysql*/ _selectAll + """
            where contact_info.email = %s
            """,
            new Query.Parameter(Parameter.CONTACT_INFO_EMAIL.get_key())
    );

    @Getter
    public enum Parameter {
        CONTACT_INFO_EMAIL("contact_info_email");

        private final String _key;

        Parameter(String key) {
            _key = Query.formatKey(key);
        }
    }
}
