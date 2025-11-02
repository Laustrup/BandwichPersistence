package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Band;
import laustrup.bandwichpersistence.core.models.Model;

import java.util.UUID;

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

  @Override
  protected Band construct() {
    return new Band(
        new Band.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Band.Fields._name),
        get_field(Band.Fields._description),
        get_field(Band.Fields._albums),
        get_field(Band.Fields._events),
        get_field(Band.Fields._subscription),
        get_field(Band.Fields._posts),
        get_field(Band.Fields._runner),
        get_field(Band.Fields._fans),
        get_field(Model.Fields._timestamp)
    );
  }
}
