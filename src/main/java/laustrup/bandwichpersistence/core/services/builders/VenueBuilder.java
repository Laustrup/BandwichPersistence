package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
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

public class VenueBuilder extends BuilderService<Venue> {

    private static final Logger _logger = Logger.getLogger(VenueBuilder.class.getName());

    private static VenueBuilder _instance;

    public static VenueBuilder get_instance() {
        if (_instance == null)
            _instance = new VenueBuilder();

        return _instance;
    }

    private VenueBuilder() {
        super(Venue.class, _logger);
    }

    @Override
    protected void completion(Venue reference, Venue object) {
        combine(reference.get_organisations(), object.get_organisations());
        combine(reference.get_albums(), object.get_albums());
        combine(reference.get_posts(), object.get_posts());
        combine(reference.get_ratings(), object.get_ratings());
        combine(reference.get_areas(), object.get_areas());
    }

    @Override
    protected Function<Function<String, Field>, Venue> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String>
                    title = new AtomicReference<>(),
                    description = new AtomicReference<>(),
                    stageSetup = new AtomicReference<>();

            Seszt<Organisation> organisations = new Seszt<>();
            Seszt<Album> albums = new Seszt<>();
            AtomicReference<ContactInfo.Address> address = new AtomicReference<>();
            Seszt<Post> posts = new Seszt<>();
            Seszt<Venue.Rating> ratings = new Seszt<>();
            Seszt<String> areas = new Seszt<>();
            AtomicReference<Integer> size = new AtomicReference<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(title, table.apply(Model.ModelDTO.Fields.title));
                        set(description, table.apply(Venue.DTO.Fields.description));
                        set(stageSetup, table.apply(Venue.DTO.Fields.stageSetup));
                        combine(organisations, OrganisationBuilder.get_instance().build(resultSet));
                        combine(albums, AlbumBuilder.get_instance().build(resultSet));
                        AddressBuilder.get_instance().complete(address, resultSet);
                        combine(posts, PostBuilder.get_instance().build(resultSet));
                        combine(ratings, VenueRatingBuilder.get_instance().build(resultSet));
                        combine(areas, getString(Venue.DTO.Fields.areas));
                        set(size, table.apply(Venue.DTO.Fields.size));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Venue(
                    id.get(),
                    title.get(),
                    description.get(),
                    organisations,
                    albums,
                    address.get(),
                    stageSetup.get(),
                    posts,
                    ratings,
                    areas,
                    size.get(),
                    timestamp.get()
            );
        };
    }
}
