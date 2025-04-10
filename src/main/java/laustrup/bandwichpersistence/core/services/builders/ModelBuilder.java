package laustrup.bandwichpersistence.core.services.builders;

import jdk.jshell.spi.ExecutionControl;
import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.ZoneId;
import java.util.function.Function;
import java.util.function.Supplier;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.get;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.peek;

public class ModelBuilder extends BuilderService<Model> {

    private final UserBuilder _userBuilder = new UserBuilder();
    private final EventBuilder _eventBuilder = new EventBuilder();

    protected ModelBuilder() {
        super(Model.class, ModelBuilder.class);
    }

    @Override
    public Model build(ResultSet resultSet) {
        return determineKind(
                getZoneId(resultSet),
                () -> _userBuilder.build(resultSet),
                () -> _eventBuilder.build(resultSet)
        );
    }

    @Override
    protected void completion(Model collective, Model part) {
        ZoneId zoneId = null;

        try {
            zoneId = ((Event) collective).get_zoneId();
        } catch (Exception ignored) {}

        determineKind(
                zoneId == null ? null : zoneId.getId(),
                () -> _userBuilder.completion((User) collective, (User) part),
                () -> _eventBuilder.completion((Event) collective, (Event) part)
        );
    }

    @SneakyThrows @Override
    protected Function<Function<String, Field>, Model> logic(ResultSet resultSet) {
        throw new ExecutionControl.NotImplementedException("Logic in Model builder should not be implemented");
    }

    private void determineKind(String zoneId, Runnable... actions) {
        switch (zoneId) {
            case null -> actions[0].run();
            default -> actions[1].run();
        };
    }

    private Model determineKind(String zoneId, Supplier<Model>... actions) {
        return switch (zoneId) {
            case null -> actions[0].get();
            default -> actions[1].get();
        };
    }

    private String getZoneId(ResultSet resultSet) {
        return peek(
                resultSet,
                () -> get(
                        JDBCService::getString,
                        Event.DTO.Fields.zoneId
                ),
                exception -> _logger.warning("Couldn't find user type when building user!\n" + exception.getMessage())
        );
    }
}
