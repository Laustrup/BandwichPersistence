package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.util.UUID;

public class EventBuilder extends BuilderService<Event> {

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

  @Override
  protected Event construct() {
    return new Event(
        new Event.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Event.Fields._title),
        get_field(Event.Fields._description),
        get_field(Event.Fields._openDoors),
        get_field(Event.Fields._charity),
        get_field(Event.Fields._public),
        get_field(Event.Fields._cancelled),
        get_field(Event.Fields._soldOut),
        get_field(Event.Fields._location),
        get_field(Event.Fields._zoneId),
        get_field(Event.Fields._ticketOptions),
        get_field(Event.Fields._tickets),
        get_field(Event.Fields._contactInfo),
        get_field(Event.Fields._gigs),
        get_field(Event.Fields._organisations),
        get_field(Event.Fields._venue),
        get_field(Event.Fields._requests),
        new Seszt<>(),
        get_field(Event.Fields._posts),
        get_field(Event.Fields._albums),
        get_field(Model.Fields._timestamp)
    );
  }
}
