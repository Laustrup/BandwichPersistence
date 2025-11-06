package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;

public class TicketOptionBuilder extends BuilderService<Ticket.Option> {

  private static TicketOptionBuilder _instance;

  public static TicketOptionBuilder get_instance() {
    if (_instance == null)
      _instance = new TicketOptionBuilder();

    return _instance;
  }

  protected TicketOptionBuilder() {

  }

  @Override
  protected void completion(Ticket.Option reference, Ticket.Option object) {

  }
}
