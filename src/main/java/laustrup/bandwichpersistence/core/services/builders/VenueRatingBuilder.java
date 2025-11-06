package laustrup.bandwichpersistence.core.services.builders;

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
  protected void completion(Venue.Rating reference, Venue.Rating object) {

  }
}
