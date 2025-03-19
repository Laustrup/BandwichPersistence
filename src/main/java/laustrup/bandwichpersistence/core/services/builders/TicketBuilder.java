package laustrup.bandwichpersistence.core.services.builders;

import jdk.jshell.spi.ExecutionControl;
import laustrup.bandwichpersistence.core.models.Ticket;

import java.sql.ResultSet;

//TODO
public class TicketBuilder {

    public static Ticket build(ResultSet resultSet) {
        try {
            throw new ExecutionControl.NotImplementedException("Ticket build not yet implemented");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Ticket.Option buildOption(ResultSet resultSet) {
        try {
            throw new ExecutionControl.NotImplementedException("Ticket build not yet implemented");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Ticket.Option.Template buildOptionTemplate(ResultSet resultSet) {
        try {
            throw new ExecutionControl.NotImplementedException("Ticket build not yet implemented");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }
}
