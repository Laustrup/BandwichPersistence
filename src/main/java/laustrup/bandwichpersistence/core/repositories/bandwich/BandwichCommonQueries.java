package laustrup.bandwichpersistence.core.repositories.bandwich;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityConfigurations;
import laustrup.bandwichpersistence.core.persistence.models.SimpleDatabaseConfigurations;
import laustrup.bandwichpersistence.core.repositories.common.queries.CommonQueries;

import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.getDatabaseEntityFromEnum;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityConfigurationsService.get_databaseEntityData;

public abstract class BandwichCommonQueries extends CommonQueries {

    public static class BandwichDatabasePropertiesCollection extends DatabasePropertiesCollection {
        public static final DatabaseEntityConfigurations.Data
                CONTACT_INFO = get_databaseEntityData(ContactInfo.class),
                PHONE = get_databaseEntityData(ContactInfo.Phone.class),
                ADDRESS = get_databaseEntityData(ContactInfo.Address.class),
                COUNTRY = get_databaseEntityData(ContactInfo.Country.class),
                ARTIST = get_databaseEntityData(Artist.class),
                BAND = get_databaseEntityData(Band.class),
                ORGANISATION_EMPLOYEE = get_databaseEntityData(Organisation.Employee.class),
                ORGANISATION_EMPLOYMENT = fromField(Organisation.Employee.class, Organisation.Employee.Fields._roles),
                ORGANISATION_EMPLOYEE_AUTHORIZATION = getDatabaseEntityFromEnum(Organisation.Employee.Authority.class),
                ARTIST_AUTHORIZATION = getDatabaseEntityFromEnum(Artist.Authority.class),
                AUTHORITY = new DatabaseEntityConfigurations.Data(
                        new SimpleDatabaseConfigurations("authorities"),
                        "authorities"
                ),
                SUBSCRIPTION = get_databaseEntityData(Subscription.class),
                CHAT_ROOM = get_databaseEntityData(ChatRoom.class),
                ORGANISATION_EMPLOYEE_CHAT_ROOM = conjunction(ORGANISATION_EMPLOYEE, CHAT_ROOM),
                ARTIST_CHAT_ROOM = conjunction(ARTIST, CHAT_ROOM),
                MESSAGE = get_databaseEntityData(Message.class),
                BAND_MEMBERSHIP = get_databaseEntityData(Band.Membership.class)
        ;
    }
}
