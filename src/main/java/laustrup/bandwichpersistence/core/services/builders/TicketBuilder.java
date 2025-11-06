package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;

public class TicketBuilder extends BandwichBuilderService<Ticket> {

  private static TicketBuilder _instance;

  public static TicketBuilder get_instance() {
    if (_instance == null)
      _instance = new TicketBuilder();

    return _instance;
  }

  private TicketBuilder() {

  }

  @Override
  protected void completion(Ticket reference, Ticket object) {

  }
}
