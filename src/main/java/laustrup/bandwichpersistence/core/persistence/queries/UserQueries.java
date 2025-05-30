package laustrup.bandwichpersistence.core.persistence.queries;

import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.core.services.persistence.queries.FormatQueryService;
import lombok.Getter;

import java.util.stream.Stream;

public class UserQueries {

    private static final String _userId = "@user_id";

    public static final Query getAllUsers = new Query(/*language=mysql*/ """
            select
                *
            from
                users
                    inner join contact_infos
                        on contact_infos.id = users.id
                    left join addresses
                        on addresses.contact_info_id = contact_infos.id
                    left join user_stories
                        on user_stories.user_id = users.id
                    left join stories
                        on user_stories.story_id = stories.id
                    left join story_details
                        on stories.id = story_details.story_id
                    left join user_authorities
                        on users.id = user_authorities.user_id
            
            """
    );

    public static final Query getByEmail = new Query(getAllUsers.get_script() + /*language=mysql*/ """
            where contact_infos.email = %s
            """,
            new Query.Parameter(Parameter.CONTACT_INFO_EMAIL.get_key())
    );

    public static final Query getById = new Query(getAllUsers.get_script() + /*language=mysql*/ """
            where users.id = %s
            """,
            new Query.Parameter(Parameter.USER_ID.get_key())
    );

    public static final Query loginQuery = new Query(getAllUsers.get_script() + /*language=mysql*/ """
            where
                contact_infos.email = %s
                    and
                users.password = %s
            """,
            Stream.of(
                    new Query.Parameter(Parameter.CONTACT_INFO_EMAIL.get_key()),
                    new Query.Parameter(Parameter.USER_PASSWORD.get_key())
            )
    );

    public static final Query upsertContactInfoQuery = new Query(
            FormatQueryService.formatQuery(/*language=mysql*/ """
                    insert into contact_infos(
                        id,
                        name,
                        email
                    ) values (
                        %5,
                        %s,
                        %s
                    ) on duplicate key update
                        name = %s,
                        email = %s
                    ;
                    
                    """,
                    _userId
            ),
            Stream.of(
                    new Query.Parameter(Parameter.CONTACT_INFO_NAME.get_key()),
                    new Query.Parameter(Parameter.CONTACT_INFO_EMAIL.get_key()),
                    new Query.Parameter(Parameter.CONTACT_INFO_NAME.get_key()),
                    new Query.Parameter(Parameter.CONTACT_INFO_EMAIL.get_key())
            )
    );

    public static Query upsertAddressQuery(int index) {
        return new Query(
                FormatQueryService.formatQuery(
                        /*language=mysql*/ """
                        insert into addresses(
                            id,
                            contact_info_id,
                            street,
                            number,
                            floor,
                            postal_code,
                            city,
                            country
                        ) values (
                            ifnull(%s, uuid_to_bin(uuid())),
                            %5,
                            %s,
                            %s,
                            %s,
                            %s,
                            %s,
                            %s
                        ) on duplicate key update
                            street = %s,
                            number = %s,
                            floor = %s,
                            postal_code = %s,
                            city = %s,
                            country = %s
                        ;
                        
                        """,
                        _userId
                ),
            Stream.of(
                    new Query.Parameter(index, Parameter.ADDRESS_ID.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_STREET.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_NUMBER.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_FLOOR.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_POSTAL_CODE.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_CITY.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_COUNTRY.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_STREET.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_NUMBER.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_FLOOR.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_POSTAL_CODE.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_CITY.get_key()),
                    new Query.Parameter(index, Parameter.ADDRESS_COUNTRY.get_key())
            )
        );
    }

    public static final Query upsertUserQuery = new Query(
            FormatQueryService.formatQuery(
                    /*language=mysql*/ """
                    set %5 = ifnull(%s, uuid_to_bin(uuid()));
                    
                    insert into users(
                        id,
                        password,
                        zone_id
                    ) values (
                        %5,
                        %s,
                        %s
                    ) on duplicate key update
                        password = %s,
                        zone_id = %s
                    ;
                    """,
                    _userId,
                    _userId
            ),
            Stream.of(
                    new Query.Parameter(Parameter.USER_ID.get_key()),
                    new Query.Parameter(Parameter.USER_PASSWORD.get_key()),
                    new Query.Parameter(Parameter.ZONE_ID.get_key()),
                    new Query.Parameter(Parameter.USER_PASSWORD.get_key()),
                    new Query.Parameter(Parameter.ZONE_ID.get_key())
            )
    );

    public static Query insertIgnoreAuthorityQuery(int index) {
        return new Query(
                FormatQueryService.formatQuery(
                        /*language=mysql*/ """
                        insert ignore into user_authorities(
                            user_id,
                            authority
                        ) values (
                            %5,
                            %s
                        );
                        """,
                        _userId
                ),
                new Query.Parameter(index, Parameter.AUTHORITY.get_key())
        );
    }

    @Getter
    public enum Parameter {
        CONTACT_INFO_EMAIL("contact_info_email"),
        CONTACT_INFO_NAME("contact_info_name"),
        USER_PASSWORD("user_password"),
        USER_ID("user_id"),
        ZONE_ID("zone_id"),
        AUTHORITY("authority"),
        ADDRESS_ID("address_id"),
        ADDRESS_STREET("address_street"),
        ADDRESS_NUMBER("address_number"),
        ADDRESS_FLOOR("address_floor"),
        ADDRESS_POSTAL_CODE("address_postal_code"),
        ADDRESS_CITY("address_city"),
        ADDRESS_COUNTRY("address_country");

        private final String _key;

        Parameter(String key) {
            _key = Query.formatKey(key);
        }
    }
}
