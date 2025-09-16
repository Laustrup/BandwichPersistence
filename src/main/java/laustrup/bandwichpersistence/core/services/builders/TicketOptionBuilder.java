package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;
import laustrup.bandwichpersistence.core.models.TicketBase;

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

    @Override
    protected Ticket.Option construct() {
        return new Ticket.Option(
                get_field(Ticket.Option.Fields._id),
                get_field(Ticket.Option.Fields._eventId),
                get_field(Ticket.Option.Fields._venueId),
                get_field(Ticket.Option.Fields._title),
                get_field(TicketBase.Fields._price),
                get_field(TicketBase.Fields._valuta),
                get_field(TicketBase.Fields._sitting),
                get_field(TicketBase.Fields._areas),
                get_field(TicketBase.Fields._timestamp)
        );
    }
}
