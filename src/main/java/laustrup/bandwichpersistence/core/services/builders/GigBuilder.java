package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Event;

public class GigBuilder extends BuilderService<Event.Gig> {

  private static GigBuilder _instance;

  public static GigBuilder get_instance() {
    if (_instance == null)
      _instance = new GigBuilder();

    return _instance;
  }

  private GigBuilder() {

  }

  @Override
  protected void completion(Event.Gig collective, Event.Gig part) {
    combine(collective.get_act(), part.get_act());
  }

}
