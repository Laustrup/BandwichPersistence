package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;
import laustrup.bandwichpersistence.core.models.TicketBase;

public class TicketBuilder extends BuilderService<Ticket> {

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

    @Override
    protected Ticket construct() {
        return new Ticket(
                get_field(Ticket.Fields._userId),
                get_field(Ticket.Fields._eventId),
                get_field(Ticket.Fields._seat),
                get_field(TicketBase.Fields._price),
                get_field(TicketBase.Fields._valuta),
                get_field(Ticket.Fields._arrived),
                get_field(TicketBase.Fields._sitting),
                get_field(TicketBase.Fields._areas),
                get_field(Ticket.Fields._optionId),
                get_field(TicketBase.Fields._timestamp)
        );
    }
}
