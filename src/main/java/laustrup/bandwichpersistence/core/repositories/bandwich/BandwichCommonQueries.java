package laustrup.bandwichpersistence.core.repositories.bandwich;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityData;
import laustrup.bandwichpersistence.core.repositories.common.queries.CommonQueries;

import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.getDatabaseEntityFromEnum;

public abstract class BandwichCommonQueries extends CommonQueries {

    public static class BandwichDatabasePropertiesCollection extends DatabasePropertiesCollection {
        public static final DatabaseEntityData
                CONTACT_INFO = new DatabaseEntityData(ContactInfo.class),
                PHONE = new DatabaseEntityData(ContactInfo.Phone.class),
                ADDRESS = new DatabaseEntityData(ContactInfo.Address.class),
                COUNTRY = new DatabaseEntityData(ContactInfo.Country.class),
                ARTIST = new DatabaseEntityData(Artist.class),
                BAND = new DatabaseEntityData(Band.class),
                ORGANISATION_EMPLOYEE = new DatabaseEntityData(Organisation.Employee.class),
                ORGANISATION_EMPLOYMENT = getDatabaseEntityFromEnum(Organisation.Employee.Role.class),
                ORGANISATION_EMPLOYEE_AUTHORIZATION = getDatabaseEntityFromEnum(Organisation.Employee.Authority.class),
                ARTIST_AUTHORIZATION = getDatabaseEntityFromEnum(Artist.Authority.class),
                AUTHORITY = DatabaseEntityData.of("authorities"),
                SUBSCRIPTION = new DatabaseEntityData(Subscription.class),
                CHAT_ROOM = new DatabaseEntityData(ChatRoom.class),
                ORGANISATION_EMPLOYEE_CHAT_ROOM = conjunction(ORGANISATION_EMPLOYEE, CHAT_ROOM),
                ARTIST_CHAT_ROOM = conjunction(ARTIST, CHAT_ROOM),
                MESSAGE = new DatabaseEntityData(Message.class),
                BAND_MEMBERSHIP = new DatabaseEntityData(Band.Membership.class)
        ;
    }
}
