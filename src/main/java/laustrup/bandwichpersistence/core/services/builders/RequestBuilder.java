package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Rating;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.get;

public class RequestBuilder extends BuilderService<Request> {

    private static final Logger _logger = Logger.getLogger(RequestBuilder.class.getName());

    private static RequestBuilder _instance;

    public static RequestBuilder get_instance() {
        if (_instance == null)
            _instance = new RequestBuilder();

        return _instance;
    }

    private RequestBuilder() {
        super(Request.class, _logger);
    }

    @Override
    protected void completion(Request reference, Request object) {

    }

    @Override
    protected Function<Function<String, Field>, Request> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID>
                    receiverId = new AtomicReference<>(),
                    senderId = new AtomicReference<>();
            AtomicReference<Event> event = new AtomicReference<>();
            AtomicReference<Instant>
                    approved = new AtomicReference<>(),
                    timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(receiverId, table.apply(Model.ModelDTO.Fields.id));
                        set(senderId, table.apply(Model.ModelDTO.Fields.id));
                        EventBuilder.get_instance().complete(event, resultSet);
                        approved.set(getInstant(Request.DTO.Fields.approved));
                        timestamp.set(getInstant(Request.DTO.Fields.timestamp));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Rating.DTO.Fields.appointedId
                    ).equals(primary) || !get(
                            JDBCService::getUUID,
                            Rating.DTO.Fields.reviewerId
                    ).equals(primary),
                    receiverId,
                    senderId
            );

            return new Request(
                    receiverId.get(),
                    senderId.get(),
                    event.get(),
                    approved.get(),
                    timestamp.get()
            );
        };
    }
}
