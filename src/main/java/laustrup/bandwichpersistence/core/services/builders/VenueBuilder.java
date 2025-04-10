package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class VenueBuilder extends BuilderService<Venue> {

    private final OrganisationBuilder _organisationBuilder = new OrganisationBuilder();

    private final AlbumBuilder _albumBuilder = new AlbumBuilder();

    private final AddressBuilder _addressBuilder = new AddressBuilder();

    private final PostBuilder _postBuilder = new PostBuilder();

    private final VenueRatingBuilder _venueRatingBuilder = new VenueRatingBuilder();

    protected VenueBuilder() {
        super(Venue.class, VenueBuilder.class);
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
                        combine(organisations, _organisationBuilder.build(resultSet));
                        combine(albums, _albumBuilder.build(resultSet));
                        _addressBuilder.complete(address, resultSet);
                        combine(posts, _postBuilder.build(resultSet));
                        combine(ratings, _venueRatingBuilder.build(resultSet));
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
