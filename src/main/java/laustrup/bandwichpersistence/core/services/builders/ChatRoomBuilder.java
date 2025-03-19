package laustrup.bandwichpersistence.core.services.builders;

import jdk.jshell.spi.ExecutionControl;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;

import java.sql.ResultSet;

public class ChatRoomBuilder {

    public static ChatRoom build(ResultSet resultSet) {
        try {
            throw new ExecutionControl.NotImplementedException("ChatRoom build not yet implemented");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChatRoom.Template buildTemplate(ResultSet resultSet) {
        try {
            throw new ExecutionControl.NotImplementedException("ChatRoom build not yet implemented");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }
}
