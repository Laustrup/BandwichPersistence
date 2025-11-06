package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Band;

public class BandBuilder extends BuilderService<Band> {

  private static BandBuilder _instance;

  public static BandBuilder get_instance() {
    if (_instance == null)
      _instance = new BandBuilder();

    return _instance;
  }

  private BandBuilder() {

  }

  @Override
  protected void completion(Band reference, Band object) {
    combine(reference.get_albums(), object.get_albums());
    combine(reference.get_events(), object.get_events());
    combine(reference.get_posts(), object.get_posts());
    combine(reference.get_fans(), object.get_fans());
  }
}
