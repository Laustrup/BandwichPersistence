package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;

import java.util.UUID;

public class ChatRoomTemplateBuilder extends BuilderService<ChatRoom.Template> {

  private static ChatRoomTemplateBuilder _instance;

  public static ChatRoomTemplateBuilder get_instance() {
    if (_instance == null)
      _instance = new ChatRoomTemplateBuilder();

    return _instance;
  }

  private ChatRoomTemplateBuilder() {

  }

  @Override
  protected void completion(ChatRoom.Template collective, ChatRoom.Template part) {
    combine(collective.get_chatters(), part.get_chatters());
  }

  @Override
  protected ChatRoom.Template construct() {
    return new ChatRoom.Template(
        new ChatRoom.Template.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Model.Fields._title),
        get_field(ChatRoom.Template.Fields._chatters),
        get_field(Model.Fields._timestamp)
    );
  }
}
