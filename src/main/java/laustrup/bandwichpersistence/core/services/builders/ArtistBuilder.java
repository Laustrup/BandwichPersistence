package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.Follow;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.builders.BuilderService.printError;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ArtistBuilder {

    public static Logger _logger = Logger.getLogger(ArtistBuilder.class.getSimpleName());

    public static Artist build(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<String>
                username = new AtomicReference<>(),
                firstName = new AtomicReference<>(),
                lastName = new AtomicReference<>(),
                description = new AtomicReference<>();
        AtomicReference<ContactInfo> contactInfo = new AtomicReference<>();
        Seszt<Album> albums = new Seszt<>();
        AtomicReference<Subscription> subscription = new AtomicReference<>();
        Seszt<User.Authority> authorities = new Seszt<>();
        Seszt<ChatRoom> chatRooms = new Seszt<>();
        Seszt<Artist.Membership> memberships = new Seszt<>();
        Seszt<Event.Gig> gigs = new Seszt<>();
        AtomicReference<String> runner = new AtomicReference<>();
        Seszt<Follow> follows = new Seszt<>();
        Seszt<Request> requests = new Seszt<>();
        Seszt<Rating> ratings = new Seszt<>();
        AtomicReference<History> history = new AtomicReference<>(new History(History.JoinTableDetails.ARTIST));
        AtomicReference<Instant> timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        username.set(getString(User.UserDTO.Fields.username));
                        firstName.set(getString(User.UserDTO.Fields.firstName));
                        lastName.set(getString(User.UserDTO.Fields.lastName));
                        description.set(getString(User.UserDTO.Fields.description));
                        contactInfo.set(UserBuilder.buildContactInfo(resultSet));
                        albums.add(AlbumBuilder.build(resultSet));
                        subscription.set(UserBuilder.buildSubscription(resultSet));
                        authorities.add(User.Authority.valueOf(getString(User.UserDTO.Fields.authorities)));
                        chatRooms.add(ChatRoomBuilder.build(resultSet));
                        memberships.add(new Artist.Membership(
                                BandBuilder.build(resultSet),
                                Artist.Membership.Association.valueOf(getString(Artist.Membership.DTO.Fields.association))
                        ));
                        gigs.add(EventBuilder.buildGig(resultSet));
                        runner.set(getString(Artist.DTO.Fields.runner));
                        follows.add(new Follow(
                                getBoolean(Follow.DTO.Fields.notify),
                                getUUID(Follow.DTO.Fields.followerId),
                                getUUID(Follow.DTO.Fields.followedId)
                        ));
                        requests.add(RequestBuilder.build(resultSet));
                        ratings.add(RatingBuilder.buildRatingOfVenue(resultSet));
                        history.get().get_stories().add(HistoryBuilder.buildStory(resultSet, history.get()));
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
        }

        return new Artist(
                id.get(),
                username.get(),
                firstName.get(),
                lastName.get(),
                description.get(),
                contactInfo.get(),
                albums,
                subscription.get(),
                authorities,
                chatRooms,
                new Seszt<>(),
                memberships,
                gigs,
                runner.get(),
                follows,
                requests,
                ratings,
                history.get(),
                timestamp.get()
        );
    }
}
