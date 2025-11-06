package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.chats.Request;

public class RequestBuilder extends BandwichBuilderService<Request> {

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
}
