package laustrup.bandwichpersistence.core.repositories.bandwich;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.identification.Authority;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import laustrup.bandwichpersistence.core.repositories.common.queries.CommonQueries;

public abstract class BandwichCommonQueries extends CommonQueries {

  private static final BandwichEntityDataCollection _dataCollection = BandwichEntityDataCollection.get_instance();

  //TODO Use Bandwich entity data collection instead to eliminate redundancy
  public static class BandwichDatabasePropertiesCollection extends DatabasePropertiesCollection {
    public static final DatabaseDefinition
        CONTACT_INFO = _dataCollection.entityOf(ContactInfo.class),
        PHONE = _dataCollection.entityOf(ContactInfo.Phone.class),
        ADDRESS = _dataCollection.entityOf(ContactInfo.Address.class),
        ARTIST = _dataCollection.entityOf(Artist.class),
        ARTIST_CHAT_ROOM = _dataCollection.entityOf(Artist.class, ChatRoom.class),
        BAND = _dataCollection.entityOf(Band.class),
        ORGANISATION_EMPLOYEE = _dataCollection.entityOf(Organisation.Employee.class),
        ORGANISATION_EMPLOYMENT = _dataCollection.entityOf(Organisation.Employee.Role.class),
        ORGANISATION_EMPLOYEE_CHAT_ROOM = _dataCollection.entityOf(Organisation.Employee.class, ChatRoom.class),
        ORGANISATION_EMPLOYEE_AUTHORIZATION = _dataCollection.entityOf(Organisation.Employee.class, Organisation.Employee.Authority.class, Authority.class),
        ARTIST_AUTHORIZATION = _dataCollection.entityOf(Artist.class, Artist.Authority.class, Authority.class),
        AUTHORITY = _dataCollection.entityOf(Authority.class),
        SUBSCRIPTION = _dataCollection.entityOf(Subscription.class),
        CHAT_ROOM = _dataCollection.entityOf(ChatRoom.class),
        MESSAGE = _dataCollection.entityOf(Message.class),
        BAND_MEMBERSHIP = _dataCollection.entityOf(Band.Membership.class);
  }
}
