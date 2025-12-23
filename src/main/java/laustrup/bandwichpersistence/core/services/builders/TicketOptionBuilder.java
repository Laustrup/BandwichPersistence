package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketOptionBuilder extends BandwichBuilderService<Ticket.Option> {

  private static TicketOptionBuilder _instance;

  public static TicketOptionBuilder get_instance() {
    if (_instance == null)
      _instance = new TicketOptionBuilder();

    return _instance;
  }

  @Override
  protected void completion(Ticket.Option reference, Ticket.Option object) {

  }
}
