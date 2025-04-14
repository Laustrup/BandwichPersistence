package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class BandBuilder extends BuilderService<Band> {

    private static final Logger _logger = Logger.getLogger(BandBuilder.class.getName());

    private static BandBuilder _instance;

    private final AlbumBuilder _albumBuilder = AlbumBuilder.get_instance();

    private final EventBuilder _eventBuilder = EventBuilder.get_instance();

    private final UserBuilder _userBuilder = UserBuilder.get_instance();

    private final SubscriptionBuilder _subscriptionBuilder = SubscriptionBuilder.get_instance();

    private final PostBuilder _postBuilder = PostBuilder.get_instance();

    public static BandBuilder get_instance() {
        if (_instance == null)
            _instance = new BandBuilder();

        return _instance;
    }

    private BandBuilder() {
        super(_instance, _logger);
    }

    @Override
    protected void completion(Band reference, Band object) {
        combine(reference.get_albums(), object.get_albums());
        combine(reference.get_events(), object.get_events());
        combine(reference.get_posts(), object.get_posts());
        combine(reference.get_fans(), object.get_fans());
    }

    @Override
    protected Function<Function<String, Field>, Band> logic(ResultSet resultSet) {
        return table -> {
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

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(name, table.apply(Band.DTO.Fields.name));
                        set(description, table.apply(Band.DTO.Fields.description));
                        set(runner, table.apply(Band.DTO.Fields.runner));
                        combine(albums, _albumBuilder.build(resultSet));
                        combine(events, _eventBuilder.build(resultSet));
                        combine(posts, _postBuilder.build(resultSet));
                        combine(fans, _userBuilder.build(resultSet));
                        _subscriptionBuilder.complete(subscription, resultSet);
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

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
        };
    }
}
