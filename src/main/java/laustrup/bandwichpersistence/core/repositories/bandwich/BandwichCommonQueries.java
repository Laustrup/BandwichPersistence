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

  public static class BandwichDatabasePropertiesCollection extends DatabasePropertiesCollection {
    public static final DatabaseDefinition.Entity
        CONTACT_INFO = _dataCollection.entityOf(ContactInfo.class),
        PHONE = _dataCollection.entityOf(ContactInfo.Phone.class),
        ADDRESS = _dataCollection.entityOf(ContactInfo.Address.class),
        COUNTRY = _dataCollection.entityOf(ContactInfo.Country.class),
        ARTIST = _dataCollection.entityOf(Artist.class),
        BAND = _dataCollection.entityOf(Band.class),
        ORGANISATION_EMPLOYEE = _dataCollection.entityOf(Organisation.Employee.class),
        ORGANISATION_EMPLOYMENT = _dataCollection.entityOf(Organisation.Employee.Role.class),
        ORGANISATION_EMPLOYEE_AUTHORIZATION = _dataCollection.entityOf(Organisation.Employee.Authority.class),
        ARTIST_AUTHORIZATION = _dataCollection.entityOf(Artist.Authority.class),
        AUTHORITY = _dataCollection.entityOf(Authority.class),
        SUBSCRIPTION = _dataCollection.entityOf(Subscription.class),
        CHAT_ROOM = _dataCollection.entityOf(ChatRoom.class),
        MESSAGE = _dataCollection.entityOf(Message.class),
        BAND_MEMBERSHIP = _dataCollection.entityOf(Band.Membership.class);

    public static final DatabaseDefinition.Conjunction
        ORGANISATION_EMPLOYEE_CHAT_ROOM = _dataCollection.conjunctionOf(Organisation.Employee.class, ChatRoom.class),
        ARTIST_CHAT_ROOM = _dataCollection.conjunctionOf(Artist.class, ChatRoom.class);
  }
}
