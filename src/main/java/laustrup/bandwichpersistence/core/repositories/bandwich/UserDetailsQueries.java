package laustrup.bandwichpersistence.core.repositories.bandwich;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Clause.Clausement;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.repositories.bandwich.joins.*;
import lombok.Getter;

import java.util.Optional;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.CONTACT_INFO;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.get_tableTitle;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.toAlias;

public class UserDetailsQueries extends BandwichCommonQueries {

    private static String selectAll(Optional<Clausement> where) {
        return selecting(new Properties(CONTACT_INFO.get_title(), true, where))
                .addJoins(
                        ContactInfoJoins.LEFT_PHONE_TO_CONTACT_INFO,
                        ContactInfoJoins.LEFT_ADDRESS_TO_CONTACT_INFO,
                        ContactInfoJoins.LEFT_COUNTRY_TO_CONTACT_INFO,
                        ArtistJoins.LEFT_ARTIST_TO_CONTACT_INFO,
                        BandJoins.LEFT_BAND_MEMBERSHIP_TO_ARTIST,
                        BandJoins.LEFT_BAND_TO_BAND_MEMBERSHIP,
                        OrganisationJoins.LEFT_ORGANISATION_EMPLOYEE_TO_CONTACT_INFO,
                        OrganisationJoins.LEFT_ORGANISATION_EMPLOYMENT_TO_EMPLOYEE,
                        AuthorizationJoins.LEFT_ORGANISATION_EMPLOYEE_AUTHORIZATIONS,
                        AuthorizationJoins.LEFT_ORGANISATION_ARTIST_AUTHORIZATIONS,
                        AuthorizationJoins.LEFT_AUTHORITIES,
                        SubscriptionJoins.LEFT_SUBSCRIPTION,
                        ChatRoomJoins.LEFT_ORGANISATION_EMPLOYEE,
                        ChatRoomJoins.LEFT_ARTIST,
                        ChatRoomJoins.LEFT,
                        MessageJoins.LEFT_TO_CHAT_ROOM
                )
                .select();
    }

    public static Query selectAllForLogin(String email) {
        return new Query(selectAll(
                Optional.of(complying().which(Condition.equals(
                        Field.of(toAlias(get_tableTitle(ContactInfo.class)), "email"),
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
