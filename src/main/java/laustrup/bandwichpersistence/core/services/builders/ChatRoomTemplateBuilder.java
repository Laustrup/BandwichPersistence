package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.ChatRoom;

public class ChatRoomTemplateBuilder extends BandwichBuilderService<ChatRoom.Template> {

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
}
