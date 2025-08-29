package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class ChatRoomJoins {

    public static final Join
            LEFT_ORGANISATION_EMPLOYEE = left(ORGANISATION_EMPLOYEE_CHAT_ROOM.get_title(), Condition.equals(
                    DatabaseField.of(ORGANISATION_EMPLOYEE_CHAT_ROOM, ORGANISATION_EMPLOYEE.get_idReference()),
                    DatabaseField.of(ORGANISATION_EMPLOYEE)
            )), LEFT_ARTIST = left(ARTIST_CHAT_ROOM.get_title(), Condition.equals(
                    DatabaseField.of(ARTIST_CHAT_ROOM, ARTIST.get_idReference()),
                    DatabaseField.of(ARTIST)
            )), LEFT = left(
                    CHAT_ROOM.get_title(),
                    Condition.equals(DatabaseField.of(CHAT_ROOM), DatabaseField.of(ARTIST_CHAT_ROOM, CHAT_ROOM.get_idReference())),
                    Condition.equals(DatabaseField.of(CHAT_ROOM), DatabaseField.of(ORGANISATION_EMPLOYEE_CHAT_ROOM, CHAT_ROOM.get_idReference()))
            );
}
