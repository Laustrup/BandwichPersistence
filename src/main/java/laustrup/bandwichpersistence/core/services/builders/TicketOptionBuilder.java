package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Ticket;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;

import java.sql.ResultSet;
import java.util.function.Function;
import java.util.logging.Logger;

public class TicketOptionBuilder extends BuilderService<Ticket.Option> {

    private static final Logger _logger = Logger.getLogger(TicketOptionBuilder.class.getSimpleName());

    private static TicketOptionBuilder _instance;

    public static TicketOptionBuilder get_instance() {
        if (_instance == null)
            _instance = new TicketOptionBuilder();

        return _instance;
    }

    protected TicketOptionBuilder() {
        super(Ticket.Option.class, _logger);
    }

    @Override
    protected void completion(Ticket.Option reference, Ticket.Option object) {

    }

    @Override
    protected Function<Function<String, Field>, Ticket.Option> logic(ResultSet resultSet) {
        return null;
    }
}
