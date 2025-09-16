package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Rating;

public class RatingBuilder extends BuilderService<Rating> {

    private static RatingBuilder _instance;

    public static RatingBuilder get_instance() {
        if (_instance == null)
            _instance = new RatingBuilder();

        return _instance;
    }

    private RatingBuilder() {

    }

    @Override
    protected void completion(Rating reference, Rating object) {

    }

    @Override
    protected Rating construct() {
        return new Rating(
                get_field(Rating.Fields._value),
                get_field(Rating.Fields._appointedId),
                get_field(Rating.Fields._reviewerId),
                get_field(Rating.Fields._comment),
                get_field(Rating.Fields._timestamp)
        );
    }
}
