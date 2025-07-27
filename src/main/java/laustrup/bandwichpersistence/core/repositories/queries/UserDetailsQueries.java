package laustrup.bandwichpersistence.core.repositories.queries;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Clause.Clausement;
import lombok.Getter;

import java.util.Optional;

import static laustrup.bandwichpersistence.core.persistence.models.ConjunctionTable.*;
import static laustrup.bandwichpersistence.core.persistence.models.DatabaseTable.*;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.services.TableAnnotationService.*;

public class UserDetailsQueries {

    private static String selectAll(Optional<Clausement> where) {
        String
                id = "id",
                contactInfoId = "contact_info_id",
                addressId = "address_id",
                countryId = "country_id",
                artistId = "artist_id",
                bandId = "band_id",
                employeeId = "organisation_employee_id",
                authorityId = "authority_id",
                subscriptionId = "subscription_id",
                chatRoomId = "chat_room_id";

        return selecting(new Properties(CONTACT_INFO.get_title(), true, where))
                .addJoins(
                        left(PHONE, Condition.equals(Field.of(PHONE, contactInfoId), Field.of(CONTACT_INFO, id))),
                        left(ADDRESS, Condition.equals(Field.of(ADDRESS, id), Field.of(CONTACT_INFO, addressId))),
                        left(COUNTRY, Condition.equals(Field.of(COUNTRY, id), Field.of(CONTACT_INFO, countryId))),
                        left(ARTIST, Condition.equals(Field.of(ARTIST, contactInfoId), Field.of(CONTACT_INFO, id))),
                        left(BAND_MEMBERSHIP, Condition.equals(Field.of(BAND_MEMBERSHIP, artistId), Field.of(ARTIST, id))),
                        left(BAND, Condition.equals(Field.of(BAND_MEMBERSHIP, bandId), Field.of(BAND, id))),
                        left(ORGANISATION_EMPLOYEE, Condition.equals(
                                Field.of(ORGANISATION_EMPLOYEE, contactInfoId),
                                Field.of(CONTACT_INFO, id)
                        )),
                        left(ORGANISATION_EMPLOYMENT, Condition.equals(
                                Field.of(ORGANISATION_EMPLOYMENT, employeeId),
                                Field.of(ORGANISATION_EMPLOYEE, id)
                        )),
                        left(EMPLOYEE_AUTHORIZATIONS, Condition.equals(
                                Field.of(EMPLOYEE_AUTHORIZATIONS, employeeId),
                                Field.of(ORGANISATION_EMPLOYEE, id)
                        )),
                        left(ARTIST_AUTHORIZATIONS, Condition.equals(Field.of(ARTIST_AUTHORIZATIONS, artistId), Field.of(ARTIST, id))),
                        left(
                                AUTHORITIES,
                                Condition.equals(Field.of(AUTHORITIES, id), Field.of(ARTIST, authorityId)),
                                Condition.equals(Field.of(AUTHORITIES, id), Field.of(ORGANISATION_EMPLOYEE, authorityId))
                        ),
                        left(
                                SUBSCRIPTION,
                                Condition.equals(Field.of(ARTIST, subscriptionId), Field.of(SUBSCRIPTION, id)),
                                Condition.equals(Field.of(ORGANISATION_EMPLOYEE, subscriptionId), Field.of(SUBSCRIPTION, id))
                        ),
                        left(ORGANISATION_EMPLOYEE_CHAT_ROOM, Condition.equals(
                                Field.of(ORGANISATION_EMPLOYEE_CHAT_ROOM, employeeId),
                                Field.of(ORGANISATION_EMPLOYEE, id)
                        )),
                        left(ARTIST_CHAT_ROOMS, Condition.equals(Field.of(ARTIST_CHAT_ROOMS, artistId), Field.of(ARTIST, id))),
                        left(
                                CHAT_ROOM,
                                Condition.equals(Field.of(CHAT_ROOM, id), Field.of(ARTIST, chatRoomId)),
                                Condition.equals(Field.of(CHAT_ROOM, id), Field.of(ORGANISATION_EMPLOYEE_CHAT_ROOM, chatRoomId))
                        ),
                        left(MESSAGE, Condition.equals(Field.of(MESSAGE, chatRoomId), Field.of(CHAT_ROOM, id)))
                )
                .select();
    }

    public static Query selectAllForLogin(String email) {
        return new Query(selectAll(
                Optional.of(complying().which(Condition.equals(
                        Field.of(get_tableTitle(ContactInfo.class), "email"),
                        email
                )))
        ));
    }

    @Getter
    public enum Parameter {
        CONTACT_INFO_EMAIL("contact_info_email");

        private final String _key;

        Parameter(String key) {
            _key = Query.formatKey(key);
        }
    }
}
