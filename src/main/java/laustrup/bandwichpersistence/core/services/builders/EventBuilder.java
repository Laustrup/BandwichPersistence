package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import laustrup.bandwichpersistence.core.utilities.parameters.Truthiness;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class EventBuilder extends BuilderService<Event> {

    private static final Logger _logger = Logger.getLogger(EventBuilder.class.getName());

    private static EventBuilder _instance;

    public static EventBuilder get_instance() {
        if (_instance == null)
            _instance = new EventBuilder();

        return _instance;
    }

    private EventBuilder() {
        super(Event.class, _logger);
    }

    @Override
    protected void completion(Event collective, Event part) {
        combine(collective.get_ticketOptions(), part.get_ticketOptions());
        combine(collective.get_tickets(), part.get_tickets());
        combine(collective.get_gigs(), part.get_gigs());
        combine(collective.get_organisations(), part.get_organisations());
        combine(collective.get_requests(), part.get_requests());
        combine(collective.get_posts(), part.get_posts());
        combine(collective.get_albums(), part.get_albums());
    }

    @Override
    protected Function<Function<String, Field>, Event> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String>
                    title = new AtomicReference<>(),
                    description = new AtomicReference<>();
            AtomicReference<Instant> openDoors = new AtomicReference<>();
            AtomicReference<Truthiness> isCharity = new AtomicReference<>();
            AtomicReference<Instant>
                    isPublic = new AtomicReference<>(),
                    isCancelled = new AtomicReference<>(),
                    isSoldOut = new AtomicReference<>();
            AtomicReference<ContactInfo.Address> location = new AtomicReference<>();
            AtomicReference<ZoneId> zoneId = new AtomicReference<>();
            Seszt<Ticket.Option> ticketOptions = new Seszt<>();
            Seszt<Ticket> tickets = new Seszt<>();
            AtomicReference<ContactInfo> contactInfo = new AtomicReference<>();
            Seszt<Event.Gig> gigs = new Seszt<>();
            Seszt<Organisation> organisations = new Seszt<>();
            AtomicReference<ChatRoom> chatRoom = new AtomicReference<>();
            AtomicReference<Venue> venue = new AtomicReference<>();
            Seszt<Request> requests = new Seszt<>();
            Seszt<Post> posts = new Seszt<>();
            Seszt<Album> albums = new Seszt<>();
            AtomicReference<History> history = new AtomicReference<>(new History(History.JoinTableDetails.EVENT));
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(title, table.apply(Model.ModelDTO.Fields.title));
                        set(description, table.apply(Event.DTO.Fields.description));
                        set(openDoors, table.apply(Event.DTO.Fields.openDoors));
                        isCharity.set(new Truthiness(
                                Truthiness.Argument.valueOf(getString(Truthiness.Fields._argument)),
                                getString(Truthiness.Fields._message)
                        ));
                        set(isPublic, table.apply(Event.DTO.Fields.isPublic));
                        set(isCancelled, table.apply(Event.DTO.Fields.isCancelled));
                        set(isSoldOut, table.apply(Event.DTO.Fields.isSoldOut));
                        AddressBuilder.get_instance().complete(location, AddressBuilder.get_instance().build(resultSet));
                        zoneId.set(ZoneId.of(getString(Event.DTO.Fields.zoneId)));
                        combine(ticketOptions, TicketOptionBuilder.get_instance().build(resultSet));
                        combine(tickets, TicketBuilder.get_instance().build(resultSet));
                        ContactInfoBuilder.get_instance().complete(contactInfo, resultSet);
                        combine(gigs, GigBuilder.get_instance().build(resultSet));
                        combine(organisations, OrganisationBuilder.get_instance().build(resultSet));
                        ChatRoomBuilder.get_instance().complete(chatRoom, resultSet);
                        VenueBuilder.get_instance().complete(venue, resultSet);
                        combine(requests, RequestBuilder.get_instance().build(resultSet));
                        combine(posts, PostBuilder.get_instance().build(resultSet));
                        combine(albums, AlbumBuilder.get_instance().build(resultSet));
                        history.get().get_stories().add(HistoryBuilder.buildStory(resultSet, history.get()));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Event(
                    id.get(),
                    title.get(),
                    description.get(),
                    openDoors.get(),
                    isCharity.get(),
                    isPublic.get(),
                    isCancelled.get(),
                    isSoldOut.get(),
                    location.get(),
                    zoneId.get(),
                    ticketOptions,
                    tickets,
                    contactInfo.get(),
                    gigs,
                    organisations,
                    chatRoom.get(),
                    venue.get(),
                    requests,
                    new Seszt<>(),
                    posts,
                    albums,
                    history.get(),
                    timestamp.get()
            );
        };
    }
}
