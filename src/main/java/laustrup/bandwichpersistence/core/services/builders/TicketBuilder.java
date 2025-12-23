package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketBuilder extends BandwichBuilderService<Ticket> {

  private static TicketBuilder _instance;

  public static TicketBuilder get_instance() {
    if (_instance == null)
      _instance = new TicketBuilder();

    return _instance;
  }

  @Override
  protected void completion(Ticket reference, Ticket object) {

  }
}
