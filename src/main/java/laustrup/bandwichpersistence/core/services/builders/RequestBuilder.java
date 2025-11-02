package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.Request;

public class RequestBuilder extends BuilderService<Request> {

  private static RequestBuilder _instance;

  public static RequestBuilder get_instance() {
    if (_instance == null)
      _instance = new RequestBuilder();

    return _instance;
  }

  private RequestBuilder() {

  }

  @Override
  protected void completion(Request reference, Request object) {

  }

  @Override
  protected Request construct() {
    return new Request(
        get_field(Request.Fields._receiverId),
        get_field(Request.Fields._senderId),
        get_field(Request.Fields._event),
        get_field(Request.Fields._approved),
        get_field(Request.Fields._timestamp)
    );
  }
}
