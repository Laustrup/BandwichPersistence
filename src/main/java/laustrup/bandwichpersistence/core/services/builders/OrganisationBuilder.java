package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;


import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class OrganisationBuilder extends BuilderService<Organisation> {

    private static final Logger _logger = Logger.getLogger(OrganisationBuilder.class.getSimpleName());

    private static OrganisationBuilder _instance;

    public static OrganisationBuilder get_instance() {
        if (_instance == null)
            _instance = new OrganisationBuilder();

        return _instance;
    }

    private OrganisationBuilder() {
        super(Organisation.class, _logger);
    }

    @Override
    protected void completion(Organisation collective, Organisation part) {
        combine(collective.get_events(), part.get_events());
        combine(collective.get_venues(), part.get_venues());
        combine(collective.get_requests(), part.get_requests());
        combine(collective.get_chatRoomTemplates(), part.get_chatRoomTemplates());
        combine(collective.get_albums(), part.get_albums());
        combine(collective.get_employees(), part.get_employees());
    }

    @Override
    protected Function<Function<String, Field>, Organisation> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String> title = new AtomicReference<>();
            Seszt<Event> events = new Seszt<>();
            Seszt<Venue> venues = new Seszt<>();
            Seszt<Request> requests = new Seszt<>();
            AtomicReference<ContactInfo> contactInfo = new AtomicReference<>();
            Seszt<ChatRoom.Template> chatRoomTemplates = new Seszt<>();
            Seszt<Album> albums = new Seszt<>();
            Seszt<Organisation.Employee> employees = new Seszt<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(title, table.apply(getString(Model.ModelDTO.Fields.title)));
                        combine(events, EventBuilder.get_instance().build(resultSet));
                        combine(venues, VenueBuilder.get_instance().build(resultSet));
                        combine(requests, RequestBuilder.get_instance().build(resultSet));
                        ContactInfoBuilder.get_instance().complete(contactInfo, resultSet);
                        combine(chatRoomTemplates, ChatRoomTemplateBuilder.get_instance().build(resultSet));
                        combine(albums, AlbumBuilder.get_instance().build(resultSet));
                        combine(employees, OrganisationEmployeeBuilder.get_instance().build(resultSet));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Organisation(
                    id.get(),
                    title.get(),
                    events,
                    venues,
                    requests,
                    contactInfo.get(),
                    chatRoomTemplates,
                    albums,
                    employees,
                    timestamp.get()
            );
        };
    }
}
