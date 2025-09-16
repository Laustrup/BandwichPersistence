package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class ChatRoomJoins {

    public static final Join
            LEFT_ORGANISATION_EMPLOYEE = left(ORGANISATION_EMPLOYEE_CHAT_ROOM, ORGANISATION_EMPLOYEE),
            LEFT_ARTIST = left(ARTIST_CHAT_ROOM, ARTIST),
            LEFT = left(CHAT_ROOM, ARTIST_CHAT_ROOM, ORGANISATION_EMPLOYEE_CHAT_ROOM);
}
