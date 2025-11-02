package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.CHAT_ROOM;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.MESSAGE;

public class MessageJoins {

  public static final Join
      LEFT_TO_CHAT_ROOM = left(MESSAGE, CHAT_ROOM);
}
