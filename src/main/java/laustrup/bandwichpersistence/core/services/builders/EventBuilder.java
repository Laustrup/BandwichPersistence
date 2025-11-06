package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Event;

public class EventBuilder extends BandwichBuilderService<Event> {

  private static EventBuilder _instance;

  public static EventBuilder get_instance() {
    if (_instance == null)
      _instance = new EventBuilder();

    return _instance;
  }

  private EventBuilder() {

  }

  @Override
  protected void completion(Event collective, Event part) {
    combine(collective.get_ticketOptions(), part.get_ticketOptions());
    combine(collective.get_tickets(), part.get_tickets());
    combine(collective.get_gigs(), part.get_gigs());
    combine(collective.get_organisations(), part.get_organisations());
    combine(collective.get_requests(), part.get_requests());
    combine(collective.get_posts(), part.get_posts());
    combine(collective.get_albums(), part.get_albums());
  }
}
