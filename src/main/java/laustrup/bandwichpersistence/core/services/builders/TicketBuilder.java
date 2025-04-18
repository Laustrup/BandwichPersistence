package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;
import laustrup.bandwichpersistence.core.persistence.Field;

import java.sql.ResultSet;
import java.util.function.Function;
import java.util.logging.Logger;

//TODO
public class TicketBuilder extends BuilderService<Ticket> {

    private static final Logger _logger = Logger.getLogger(TicketBuilder.class.getName());

    private static TicketBuilder _instance;

    public static TicketBuilder get_instance() {
        if (_instance == null)
            _instance = new TicketBuilder();

        return _instance;
    }

    private TicketBuilder() {
        super(Ticket.class, _logger);
    }

    @Override
    protected void completion(Ticket reference, Ticket object) {

    }

    @Override
    protected Function<Function<String, Field>, Ticket> logic(ResultSet resultSet) {
        return null;
    }
}
