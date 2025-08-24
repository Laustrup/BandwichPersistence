package laustrup.bandwichpersistence.core.repositories.bandwich;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.DatabaseTable;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.repositories.common.queries.CommonQueries;

import static laustrup.bandwichpersistence.core.services.DatabaseTableAnnotationService.getDatabaseTableProperties;

public abstract class BandwichCommonQueries extends CommonQueries {

    public static class BandwichDatabasePropertiesCollection extends DatabasePropertiesCollection {
        public static final DatabaseTable.Properties
                CONTACT_INFO = getDatabaseTableProperties(ContactInfo.class),
                PHONE = getDatabaseTableProperties(ContactInfo.Phone.class),
                ADDRESS = getDatabaseTableProperties(ContactInfo.Address.class),
                COUNTRY = getDatabaseTableProperties(ContactInfo.Country.class),
                ARTIST = getDatabaseTableProperties(Artist.class),
                BAND = getDatabaseTableProperties(Band.class),
                ORGANISATION_EMPLOYEE = getDatabaseTableProperties(Organisation.Employee.class),
                ORGANISATION_EMPLOYMENT = fromField(Organisation.Employee.class, Organisation.Employee.Fields._roles),
                ORGANISATION_EMPLOYEE_AUTHORIZATION = getDatabaseTableProperties(Organisation.Employee.Authority.class),
                ARTIST_AUTHORIZATION = getDatabaseTableProperties(Artist.Authority.class),
                AUTHORITY = new DatabaseTable.Properties("authorities", ""),
                SUBSCRIPTION = getDatabaseTableProperties(Subscription.class),
                CHAT_ROOM = getDatabaseTableProperties(ChatRoom.class),
                ORGANISATION_EMPLOYEE_CHAT_ROOM = conjunction(ORGANISATION_EMPLOYEE, CHAT_ROOM),
                ARTIST_CHAT_ROOM = conjunction(ARTIST, CHAT_ROOM),
                MESSAGE = getDatabaseTableProperties(Message.class),
                BAND_MEMBERSHIP = getDatabaseTableProperties(Band.Membership.class)
        ;
    }
}
