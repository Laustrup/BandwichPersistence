package laustrup.bandwichpersistence.core.services.builders;

import jdk.jshell.spi.ExecutionControl;
import laustrup.bandwichpersistence.core.models.chats.Request;

import java.sql.ResultSet;

//TODO
public class RequestBuilder {

    public static Request build(ResultSet resultSet) {
        try {
            throw new ExecutionControl.NotImplementedException("Request build not yet implemented");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }
}
