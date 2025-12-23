package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageBuilder extends BandwichBuilderService<Message> {

  private static MessageBuilder _instance;

  public static MessageBuilder get_instance() {
    if (_instance == null)
      _instance = new MessageBuilder();

    return _instance;
  }

  @Override
  protected void completion(Message collective, Message part) {

  }
}
