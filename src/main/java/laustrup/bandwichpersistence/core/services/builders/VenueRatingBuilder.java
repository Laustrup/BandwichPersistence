package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Venue;

import java.sql.ResultSet;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class VenueRatingBuilder extends BuilderService<Venue.Rating> {

    private final RatingBuilder.Service _ratingBuilderService = new RatingBuilder.Service();

    protected VenueRatingBuilder() {
        super(Venue.Rating.class, VenueRatingBuilder.class);
    }

    @Override
    protected void completion(Venue.Rating reference, Venue.Rating object) {

    }

    @Override
    protected Function<Function<String, Field>, Venue.Rating> logic(ResultSet resultSet) {
        return table -> (Venue.Rating) _ratingBuilderService.generateLogic(
                resultSet,
                RatingBuilder.Service.Implementation.VENUE,
                table
        );
    }
}
