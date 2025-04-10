package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class GigBuilder extends BuilderService<Event.Gig> {

    private final EventBuilder _eventBuilder = new EventBuilder();

    private final BandBuilder _bandBuilder = new BandBuilder();

    protected GigBuilder() {
        super(Event.Gig.class, GigBuilder.class);
    }

    @Override
    protected void completion(Event.Gig collective, Event.Gig part) {
        combine(collective.get_act(), part.get_act());
    }

    @Override
    protected Function<Function<String, JDBCService.Field>, Event.Gig> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<Event> event = new AtomicReference<>();
            Seszt<Band> act = new Seszt<>();
            AtomicReference<Instant>
                    start = new AtomicReference<>(),
                    end = new AtomicReference<>(),
                    timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        _eventBuilder.complete(event, resultSet);
                        combine(act, _bandBuilder.build(resultSet));
                        start.set(getInstant(Event.DTO.Fields.start));
                        end.set(getInstant(Event.DTO.Fields.end));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Event.Gig(
                    id.get(),
                    event.get(),
                    act,
                    start.get(),
                    end.get(),
                    timestamp.get()
            );
        };
    }
}
