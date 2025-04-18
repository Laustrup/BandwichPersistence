package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Venue;
import laustrup.bandwichpersistence.core.persistence.Field;

import java.sql.ResultSet;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class VenueRatingBuilder extends BuilderService<Venue.Rating> {

    private static final Logger _logger = Logger.getLogger(VenueRatingBuilder.class.getSimpleName());

    private static VenueRatingBuilder _instance;

    public static VenueRatingBuilder get_instance() {
        if (_instance == null)
            _instance = new VenueRatingBuilder();

        return _instance;
    }

    private VenueRatingBuilder() {
        super(Venue.Rating.class, _logger);
    }

    @Override
    protected void completion(Venue.Rating reference, Venue.Rating object) {

    }

    @Override
    protected Function<Function<String, Field>, Venue.Rating> logic(ResultSet resultSet) {
        return table -> (Venue.Rating) RatingBuilder.Service.get_instance().generateLogic(
                resultSet,
                RatingBuilder.Service.Implementation.VENUE,
                table
        );
    }
}
