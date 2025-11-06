package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Venue;

public class VenueBuilder extends BuilderService<Venue> {

  private static VenueBuilder _instance;

  public static VenueBuilder get_instance() {
    if (_instance == null)
      _instance = new VenueBuilder();

    return _instance;
  }

  private VenueBuilder() {

  }

  @Override
  protected void completion(Venue reference, Venue object) {
    combine(reference.get_organisations(), object.get_organisations());
    combine(reference.get_albums(), object.get_albums());
    combine(reference.get_posts(), object.get_posts());
    combine(reference.get_ratings(), object.get_ratings());
    combine(reference.get_areas(), object.get_areas());
  }
}
