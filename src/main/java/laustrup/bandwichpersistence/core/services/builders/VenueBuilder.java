package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Venue;

import java.util.UUID;

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

  @Override
  protected Venue construct() {
    return new Venue(
        new Venue.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Venue.Fields._title),
        get_field(Venue.Fields._description),
        get_field(Venue.Fields._organisations),
        get_field(Venue.Fields._albums),
        get_field(Venue.Fields._location),
        get_field(Venue.Fields._stageSetup),
        get_field(Venue.Fields._posts),
        get_field(Venue.Fields._ratings),
        get_field(Venue.Fields._areas),
        get_field(Venue.Fields._size),
        get_field(Model.Fields._timestamp)
    );
  }
}
