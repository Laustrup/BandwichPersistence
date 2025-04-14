package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Venue;

import java.sql.ResultSet;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class VenueRatingBuilder extends BuilderService<Venue.Rating> {

    private static final Logger _logger = Logger.getLogger(VenueRatingBuilder.class.getSimpleName());

    private static VenueRatingBuilder _instance;

    private static RatingBuilder.Service _ratingBuilderService = RatingBuilder.Service.get_instance();

    public static VenueRatingBuilder get_instance() {
        if (_instance == null)
            _instance = new VenueRatingBuilder();

        return _instance;
    }

    private VenueRatingBuilder() {
        super(_instance, _logger);
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
