package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Rating;
import laustrup.bandwichpersistence.core.models.Venue;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class RatingBuilder {

    private static final Logger _logger = Logger.getLogger(RatingBuilder.class.getSimpleName());

    public static Venue.Rating buildRatingOfVenue(ResultSet resultset) {
        AtomicReference<Integer> value = new AtomicReference<>();
        AtomicReference<UUID>
                appointedId = new AtomicReference<>(),
                reviewerId = new AtomicReference<>();
        AtomicReference<String> comment = new AtomicReference<>();
        AtomicReference<Organisation> organisation = new AtomicReference<>();
        AtomicReference<Instant> timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        value.set(getInteger(Rating.DTO.Fields.value));
                        appointedId.set(getUUID(Rating.DTO.Fields.appointedId));
                        reviewerId.set(getUUID(Rating.DTO.Fields.reviewerId));
                        comment.set(getString(Rating.DTO.Fields.comment));
                        organisation.set(OrganisationBuilder.build(resultset));
                        timestamp.set(getInstant(Rating.DTO.Fields.timestamp));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Rating.DTO.Fields.appointedId
                    ).equals(primary) || !get(
                            JDBCService::getUUID,
                            Rating.DTO.Fields.reviewerId
                    ).equals(primary),
                    appointedId.get(),
                    reviewerId.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Venue.Rating(
                value.get(),
                appointedId.get(),
                reviewerId.get(),
                comment.get(),
                organisation.get(),
                timestamp.get()
        );
    }
}
