package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class VenueBuilder {

    private static final Logger _logger = Logger.getLogger(VenueBuilder.class.getSimpleName());

    public static Venue build(ResultSet resultSet) {

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

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        title.set(getString(Model.ModelDTO.Fields.title));
                        description.set(getString(Venue.DTO.Fields.description));
                        stageSetup.set(getString(Venue.DTO.Fields.stageSetup));
                        organisations.add(OrganisationBuilder.build(resultSet));
                        albums.add(AlbumBuilder.build(resultSet));
                        address.set(UserBuilder.buildContactInfo(resultSet).get_address());
                        posts.add(PostBuilder.build(resultSet));
                        ratings.add(RatingBuilder.buildRatingOfVenue(resultSet));
                        areas.add(getString(Venue.DTO.Fields.areas));
                        size.set(getInteger(Venue.DTO.Fields.size));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id.get()
            );
        } catch (Exception exception) {
            _logger.log(
                    Level.WARNING,
                    String.format(
                            "Couldn't build Venue with id %s, message is:\n\n%s",
                            id.get(),
                            exception.getMessage()
                    ),
                    exception
            );
        }

        return new Venue(
                id.get(),
                title.get(),
                description.get(),
                organisations,
                albums,
                address.get(),
                stageSetup.get()
                posts,
                ratings,
                areas,
                size.get(),
                timestamp.get()
        );
    }


}
