package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.get;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.getFromNextRow;

public class ModelBuilder {

    private static final Logger _logger = Logger.getLogger(ModelBuilder.class.getSimpleName());

    public static Model build(ResultSet resultSet) {
        return switch (getFromNextRow(
                resultSet,
                () -> get(
                        JDBCService::getString,
                        Event.DTO.Fields.zoneId
                ),
                exception -> _logger.warning("Couldn't find user type when building user!\n" + exception.getMessage())
        )) {
            case null -> EventBuilder.build(resultSet);
            default -> UserBuilder.build(resultSet);
        };
    }
}
