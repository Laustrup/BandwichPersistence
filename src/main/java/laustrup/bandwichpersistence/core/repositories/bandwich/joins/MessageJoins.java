package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class MessageJoins {

    public static final Join
            LEFT_TO_CHAT_ROOM = left(MESSAGE.get_title(), Condition.equals(Field.of(MESSAGE, CHAT_ROOM.get_idReference()), Field.of(CHAT_ROOM)));
}
