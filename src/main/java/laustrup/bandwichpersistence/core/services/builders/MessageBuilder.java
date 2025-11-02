package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;

import java.util.UUID;

public class MessageBuilder extends BuilderService<Message> {

  private static MessageBuilder _instance;

  public static MessageBuilder get_instance() {
    if (_instance == null)
      _instance = new MessageBuilder();

    return _instance;
  }

  private MessageBuilder() {

  }

  @Override
  protected void completion(Message collective, Message part) {

  }

  @Override
  protected Message construct() {
    return new Message(
        new Message.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Message.Fields._author),
        get_field(Message.Fields._content),
        get_field(Message.Fields._sent),
        get_field(Message.Fields._edited),
        get_field(Message.Fields._read),
        get_field(Model.Fields._timestamp)
    );
  }
}
