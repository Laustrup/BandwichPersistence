package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;

import java.util.UUID;

public class ChatRoomBuilder extends BuilderService<ChatRoom> {

  private static ChatRoomBuilder _instance;

  public static ChatRoomBuilder get_instance() {
    if (_instance == null)
      _instance = new ChatRoomBuilder();

    return _instance;
  }

  private ChatRoomBuilder() {

  }

  @Override
  protected void completion(ChatRoom collective, ChatRoom part) {
    combine(collective.get_messages(), part.get_messages());
    combine(collective.get_chatters(), part.get_chatters());
  }

  @Override
  protected ChatRoom construct() {
    return new ChatRoom(
        new ChatRoom.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Model.Fields._title),
        get_field(ChatRoom.Fields._messages),
        get_field(ChatRoom.Fields._chatters),
        get_field(Model.Fields._timestamp)
    );
  }
}
