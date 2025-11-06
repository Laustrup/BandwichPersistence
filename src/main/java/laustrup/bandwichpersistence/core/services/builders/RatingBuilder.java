package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Rating;

public class RatingBuilder extends BandwichBuilderService<Rating> {

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
}
