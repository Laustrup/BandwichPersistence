package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class BandBuilder {

    private static final Logger _logger = Logger.getLogger(AlbumBuilder.class.getSimpleName());

    public static Band build(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<Subscription> subscription = new AtomicReference<>();
        AtomicReference<Instant> timestamp = new AtomicReference<>();
        AtomicReference<String>
                name = new AtomicReference<>(),
                description = new AtomicReference<>(),
                runner = new AtomicReference<>();

        Seszt<Album> albums = new Seszt<>();
        Seszt<Event> events = new Seszt<>();
        Seszt<Post> posts = new Seszt<>();
        Seszt<User> fans = new Seszt<>();

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(get(
                                column -> getUUID(resultset, column),
                                Model.ModelDTO.Fields.id
                        ));
                        name.set(get(
                                column -> getString(resultset, column),
                                Band.DTO.Fields.name
                        ));
                        description.set(get(
                                column -> getString(resultset, column),
                                Band.DTO.Fields.description
                        ));
                        albums.add(AlbumBuilder.build(resultset)
                        );
                        events.add(EventBuilder.build(resultset)
                        );
                        posts.add(PostBuilder.build(resultset)
                        );
                        fans.add(UserBuilder.build(resultset)
                        );
                        subscription.set(SubscriptionBuilder.build(resultset)
                        );
                        timestamp.set(get(
                                column -> getTimestamp(resultset, column, Timestamp::toInstant),
                                Model.ModelDTO.Fields.timestamp
                        ));
                        runner.set(get(
                                column -> getString(resultset, column),
                                Band.DTO.Fields.runner
                        ));
                        
                    }
            )
        } catch (SQLException e) {
            _logger.log(
                    Level.WARNING,
                    String.format(
                            "Couldn't build Band with id %s, message is:\n\n%s",
                            id.get(),
                            e.getMessage()
                    ),
                    e
            );
        }

        return new Band(
                id.get(),
                name.get(),
                description.get(),
                albums,
                events,
                posts,
                fans,
                subscription.get(),
                timestamp.get(),
                runner.get()
        );
    }
}
