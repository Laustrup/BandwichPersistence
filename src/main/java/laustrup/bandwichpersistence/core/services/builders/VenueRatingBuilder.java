package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Rating;
import laustrup.bandwichpersistence.core.models.Venue;

public class VenueRatingBuilder extends BuilderService<Venue.Rating> {

  private static VenueRatingBuilder _instance;

  public static VenueRatingBuilder get_instance() {
    if (_instance == null)
      _instance = new VenueRatingBuilder();

    return _instance;
  }

  private VenueRatingBuilder() {

  }

  @Override
  protected Venue.Rating construct() {
    return new Venue.Rating(
        get_field(Rating.Fields._value),
        get_field(Rating.Fields._appointedId),
        get_field(Rating.Fields._reviewerId),
        get_field(Rating.Fields._comment),
        get_field(Venue.Rating.Fields._organisation),
        get_field(Rating.Fields._timestamp)
    );
  }

  @Override
  protected void completion(Venue.Rating reference, Venue.Rating object) {

  }
}
