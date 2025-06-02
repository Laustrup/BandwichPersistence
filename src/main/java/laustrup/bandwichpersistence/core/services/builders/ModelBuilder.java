package laustrup.bandwichpersistence.core.services.builders;

import jdk.jshell.spi.ExecutionControl;
import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.ZoneId;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations.Mode.PEEK;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.get;

public class ModelBuilder extends BuilderService<Model> {

    private static final Logger _logger = Logger.getLogger(ModelBuilder.class.getName());

    private static ModelBuilder _instance;

    public static ModelBuilder get_instance() {
        if (_instance == null)
            _instance = new ModelBuilder();

        return _instance;
    }

    private ModelBuilder() {
        super(Model.class, _logger);
    }

    @Override
    public Model build(ResultSet resultSet) {
        return determineKind(
                getZoneId(resultSet),
                () -> UserBuilder.get_instance().build(resultSet),
                () -> EventBuilder.get_instance().build(resultSet)
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
                () -> UserBuilder.get_instance().completion((User) collective, (User) part),
                () -> EventBuilder.get_instance().completion((Event) collective, (Event) part)
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
        return get(
                new Configurations(
                        JDBCService.getString(new Field(
                                Event.class.getSimpleName() + "s",
                                Event.DTO.Fields.zoneId
                        )),
                        resultSet,
                        PEEK
                ),
                String.class
        );
    }
}
