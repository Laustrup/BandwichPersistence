package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomTemplateBuilder extends BandwichBuilderService<ChatRoom.Template> {

  private static ChatRoomTemplateBuilder _instance;

  public static ChatRoomTemplateBuilder get_instance() {
    if (_instance == null)
      _instance = new ChatRoomTemplateBuilder();

    return _instance;
  }

  @Override
  protected void completion(ChatRoom.Template collective, ChatRoom.Template part) {
    combine(collective.get_chatters(), part.get_chatters());
  }
}
