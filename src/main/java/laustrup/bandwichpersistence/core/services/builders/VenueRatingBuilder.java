package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Venue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VenueRatingBuilder extends BandwichBuilderService<Venue.Rating> {

  private static VenueRatingBuilder _instance;

  public static VenueRatingBuilder get_instance() {
    if (_instance == null)
      _instance = new VenueRatingBuilder();

    return _instance;
  }

  @Override
  protected void completion(Venue.Rating reference, Venue.Rating object) {

  }
}
