package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.ProgramInitializer;
import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.Follow;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import org.springframework.http.HttpStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
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
        Seszt<User.Participation> participations = new Seszt<>();
        Seszt<Band.Membership> memberships = new Seszt<>();
        Seszt<Event.Gig> gigs = new Seszt<>();
        AtomicReference<String> runner = new AtomicReference<>();
        Seszt<Follow> follows = new Seszt<>();
        Seszt<Request> requests = new Seszt<>();
        Seszt<Rating> ratings = new Seszt<>();
        AtomicReference<History> history = new AtomicReference<>();
        AtomicReference<Instant> timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        username.set(getString(Artist.DTO.Fields.username));
                        firstName.set(getString(Artist.DTO.Fields.firstName));
                        lastName.set(getString(Artist.DTO.Fields.lastName));
                        description.set(getString(Artist.DTO.Fields.description));
                        contactInfo.set(UserBuilder.buildContactInfo(resultSet));
                        albums.add(AlbumBuilder.build(resultSet));
                        subscription.set();
                        authorities.add()
                        memberships.add(new Artist.Membership(
                                BandBuilder.build(resultSet),
                                get(
                                        column -> Artist.Membership.Association.valueOf(getString(resultSet, column)),
                                        Artist.Membership.DTO.Fields.association
                                )
                        ));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(userId.get()),
                    userId.get()
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
        );
    }
}
