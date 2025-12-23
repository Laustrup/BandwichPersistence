package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Rating;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingBuilder extends BandwichBuilderService<Rating> {

  private static RatingBuilder _instance;

  public static RatingBuilder get_instance() {
    if (_instance == null)
      _instance = new RatingBuilder();

    return _instance;
  }

  @Override
  protected void completion(Rating reference, Rating object) {

  }
}
