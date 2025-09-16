package laustrup.bandwichpersistence.core.repositories.bandwich;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.core.persistence.services.SelectService;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties.Selections;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Clause.Clausement;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.repositories.bandwich.joins.*;
import lombok.Getter;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.CONTACT_INFO;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getDeclared;

public class UserDetailsQueries extends BandwichCommonQueries {

    private static final Selections SELECT_ALL_SECTIONS = Selections.of(
            ContactInfo.class,
            ContactInfo.Phone.class,
            ContactInfo.Address.class,
            ContactInfo.Country.class,
            Artist.class,
            Band.class,
            Organisation.class,
            Organisation.Employee.class,
            Subscription.class,
            ChatRoom.class,
            Message.class
    );

    private static String selectAll(Clausement where) {
        return selectAllSelector(where,
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
        ).select();
    }

    private static SelectService.Selecting selectAllSelector(Clausement where, Join... joins) {
        return selecting(new Properties(
                SELECT_ALL_SECTIONS,
                CONTACT_INFO.get_title(),
                where,
                true
        )).addJoins(joins);
    }

    public static Query selectAllForLogin(String email) {
        return new Query(selectAll(complying().which(Condition.equals(
                DatabaseField.of(getDeclared(
                        ContactInfo.class,
                        ContactInfo.Fields._email
                )),
                email
        ))));
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
