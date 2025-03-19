package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.builders.BuilderService.printError;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class BandBuilder {

    private static final Logger _logger = Logger.getLogger(BandBuilder.class.getSimpleName());

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
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        name.set(getString(Band.DTO.Fields.name));
                        description.set(getString(Band.DTO.Fields.description));
                        albums.add(AlbumBuilder.build(resultset));
                        events.add(EventBuilder.build(resultset));
                        posts.add(PostBuilder.build(resultset));
                        fans.add(UserBuilder.build(resultset));
                        subscription.set(UserBuilder.buildSubscription(resultset));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                        runner.set(getString(Band.DTO.Fields.runner));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
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

        return new Band(
                id.get(),
                name.get(),
                description.get(),
                albums,
                events,
                subscription.get(),
                posts,
                runner.get(),
                fans,
                timestamp.get()
        );
    }
}
