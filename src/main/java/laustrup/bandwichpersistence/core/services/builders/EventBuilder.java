package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import laustrup.bandwichpersistence.core.utilities.parameters.NotBoolean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.builders.BuilderService.printError;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class EventBuilder {

    private static final Logger _logger = Logger.getLogger(EventBuilder.class.getSimpleName());

    public static Event build(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<String>
                title = new AtomicReference<>(),
                description = new AtomicReference<>();
        AtomicReference<Instant> openDoors = new AtomicReference<>();
        AtomicReference<NotBoolean> isCharity = new AtomicReference<>();
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

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        title.set(getString(Model.ModelDTO.Fields.title));
                        description.set(getString(Event.DTO.Fields.description));
                        openDoors.set(getInstant(Event.DTO.Fields.openDoors));
                        isCharity.set(new NotBoolean(
                                NotBoolean.Argument.valueOf(getString(NotBoolean.Fields._argument)),
                                getString(NotBoolean.Fields._message)
                        ));
                        isPublic.set(getInstant(Event.DTO.Fields.isPublic));
                        isCancelled.set(getInstant(Event.DTO.Fields.isCancelled));
                        isSoldOut.set(getInstant(Event.DTO.Fields.isSoldOut));
                        location.set(UserBuilder.buildAddress(resultset));
                        zoneId.set(ZoneId.of(getString(Event.DTO.Fields.zoneId)));
                        ticketOptions.add(TicketBuilder.buildOption(resultset));
                        tickets.add(TicketBuilder.build(resultset));
                        contactInfo.set(UserBuilder.buildContactInfo(resultset));
                        gigs.add(buildGig(resultset));
                        organisations.add(OrganisationBuilder.build(resultset));
                        chatRoom.set(ChatRoomBuilder.build(resultset));
                        venue.set(VenueBuilder.build(resultset));
                        requests.add(RequestBuilder.build(resultset));
                        posts.add(PostBuilder.build(resultset));
                        albums.add(AlbumBuilder.build(resultset));
                        history.get().get_stories().add(HistoryBuilder.buildStory(resultset, history.get()));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id.get()
            );
        } catch (SQLException exception) {
            printError(
                    AlbumBuilder.class,
                    id.get(),
                    exception,
                    _logger
            );
            return null;
        }

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
    }

    public static Event.Gig buildGig(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<Event> event = new AtomicReference<>();
        Seszt<Band> act = new Seszt<>();
        AtomicReference<Instant>
                start = new AtomicReference<>(),
                end = new AtomicReference<>(),
                timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        event.set(build(resultset));
                        act.add(BandBuilder.build(resultset));
                        start.set(getInstant(Event.DTO.Fields.start));
                        end.set(getInstant(Event.DTO.Fields.end));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id.get()
            );
        } catch (SQLException exception) {
            printError(
                    AlbumBuilder.class,
                    id.get(),
                    exception,
                    _logger
            );
            return null;
        }

        return new Event.Gig(
                id.get(),
                event.get(),
                act,
                start.get(),
                end.get(),
                timestamp.get()
        );
    }
}
