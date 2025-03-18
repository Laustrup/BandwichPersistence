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

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ArtistBuilder {

    public static Logger _logger = Logger.getLogger(ArtistBuilder.class.getSimpleName());

    public static Artist build(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();
        String username,
        String firstName,
        String lastName,
        String description,
        ContactInfo contactInfo,
        Seszt<Album> albums,
        Subscription subscription,
        Seszt<User.Authority> authorities,
        Seszt<ChatRoom> chatRooms,
        Seszt<User.Participation> participations,
        Seszt<Band.Membership> memberships = new Seszt<>();
        Seszt<Event.Gig> gigs,
        String runner,
        Seszt<Follow> follows,
        Seszt<Request> requests,
        Seszt<Rating> ratings,
        History history,
        Instant timestamp

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(get(
                                column -> getUUID(resultSet, column),
                                Model.ModelDTO.Fields.id
                        ));
                        memberships.add(new Artist.Membership(
                                BandBuilder.build(resultSet),
                                get(
                                        column -> Artist.Membership.Association.valueOf(getString(resultSet, column)),
                                        Artist.Membership.DTO.Fields.association
                                )
                        ));
                        subscription.set(UserBuilder.buildSubscription(resultSet).get_object());

                    },
                    primary -> !get(
                            column -> getUUID(resultSet, column),
                            Model.ModelDTO.Fields.id
                    ).equals(userId.get()),
                    userId.get()
            );
        } catch (SQLException e) {
            _logger.log(
                    Level.WARNING,
                    String.format(
                            "Couldn't build Artist with id %s, message is:\n\n%s",
                            userId.get(),
                            e.getMessage()
                    ),
                    e
            );
        }

        return new Response<>(
                new Artist(
                        id.get(),
                ),
                httpStatus
        );
    }
}
