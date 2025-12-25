package laustrup.bandwichpersistence.core.repositories.bandwich;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.identification.Authority;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.models.EntityDataCollection;
import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;

import static laustrup.bandwichpersistence.core.services.EternaryService.Operator.Property.inCase;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;
import static laustrup.bandwichpersistence.core.services.collections.MapService.collectMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BandwichEntityDataCollection implements EntityDataCollection {

  public static BandwichEntityDataCollection _instance;

  public static BandwichEntityDataCollection get_instance() {
    if (_instance == null)
      _instance = new BandwichEntityDataCollection();

    return _instance;
  }

  public static final Map<String, DatabaseDefinition> DEFINITIONS = defineDefinitions(
      ClassDatabaseDefinition.of(ContactInfo.class),
      ClassDatabaseDefinition.of(ContactInfo.Phone.class),
      ClassDatabaseDefinition.of(ContactInfo.Address.class),
      ClassDatabaseDefinition.of(Artist.class),
      ClassDatabaseDefinition.of(Artist.class, ChatRoom.class),
      ClassDatabaseDefinition.of(Band.class),
      ClassDatabaseDefinition.of(Organisation.Employee.class),
      ClassDatabaseDefinition.of(Organisation.Employee.Role.class),
      ClassDatabaseDefinition.of(Organisation.Employee.class, ChatRoom.class),
      ClassDatabaseDefinition.of(Organisation.Employee.class, Organisation.Employee.Authority.class, Authority.class),
      ClassDatabaseDefinition.of(Artist.class, Artist.Authority.class, Authority.class),
      ClassDatabaseDefinition.of(Authority.class),
      ClassDatabaseDefinition.of(Subscription.class),
      ClassDatabaseDefinition.of(ChatRoom.class),
      ClassDatabaseDefinition.of(Message.class),
      ClassDatabaseDefinition.of(Band.Membership.class)
  );

  public static Map<String, DatabaseDefinition> defineDefinitions(ClassDatabaseDefinition... classDatabaseDefinitions) {
    return collectMap(Arrays.stream(classDatabaseDefinitions)
        .map(databaseDefinition -> databaseDefinition.toDatabaseDefinition().toEntry())
    );
  }

  @Override
  public Map<String, DatabaseDefinition> getAll() {
    return DEFINITIONS;
  }

  public record ClassDatabaseDefinition(Seszt<Class<?>> classes) {

    public static ClassDatabaseDefinition of(Class<?> clazz) {
      return new ClassDatabaseDefinition(new Seszt<>(clazz));
    }

    public static ClassDatabaseDefinition of(Class<?> target, Class<?> relation, Class<?> common) {
      return new ClassDatabaseDefinition(Seszt.of(target, relation, common));
    }

    public static ClassDatabaseDefinition of(Class<?> target, Class<?> relation) {
      return new ClassDatabaseDefinition(Seszt.of(target, relation));
    }

    public DatabaseDefinition toDatabaseDefinition() {
      try {
        return stating(Liszt.of(
            inCase(classes.size() == 1)
                .then(() -> new DatabaseDefinition.Entity(classes.Get(1))),
            inCase(classes.size() == 2)
                .then(() -> DatabaseDefinition.Conjunction.of(classes.Get(1), classes.Get(2))),
            inCase(classes.size() == 3)
                .then(() -> DatabaseDefinition.Conjunction.of(classes.Get(1), classes.Get(2), classes.Get(3)))
        )).orElseThrow(new IllegalArgumentException("At the moment Database Definition only supports 1 -> 3 classes for entities."));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
