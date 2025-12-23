package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatRoomBuilder extends BandwichBuilderService<ChatRoom> {

  private static ChatRoomBuilder _instance;

  public static ChatRoomBuilder get_instance() {
    if (_instance == null)
      _instance = new ChatRoomBuilder();

    return _instance;
  }

  @Override
  protected void completion(ChatRoom collective, ChatRoom part) {
    combine(collective.get_messages(), part.get_messages());
    combine(collective.get_chatters(), part.get_chatters());
  }
}
