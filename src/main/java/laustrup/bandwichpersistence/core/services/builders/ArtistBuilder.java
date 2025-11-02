package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.BusinessUser;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.util.UUID;

public class ArtistBuilder extends BuilderService<Artist> {

  private static ArtistBuilder _instance;

  public static ArtistBuilder get_instance() {
    if (_instance == null)
      _instance = new ArtistBuilder();

    return _instance;
  }

  private ArtistBuilder() {

  }

  @Override
  protected void completion(Artist reference, Artist object) {
    combine(reference.get_albums(), object.get_albums());
    combine(reference.get_authorities(), object.get_authorities());
    combine(reference.get_chatRooms(), object.get_chatRooms());
    combine(reference.get_bandMemberships(), object.get_bandMemberships());
    combine(reference.get_gigs(), object.get_gigs());
    combine(reference.get_follows(), object.get_follows());
    combine(reference.get_requests(), object.get_requests());
    combine(reference.get_ratings(), object.get_ratings());
    combine(reference.get_history().get_stories(), object.get_history().get_stories());
  }

  @Override
  protected Artist construct() {
    return new Artist(
        new Artist.Id((UUID) get_field(Model.Fields._identity)),
        get_field(User.Fields._username),
        get_field(User.Fields._firstName),
        get_field(User.Fields._lastName),
        get_field(User.Fields._description),
        get_field(User.Fields._contactInfo),
        get_field(Artist.Fields._albums),
        get_field(User.Fields._subscription),
        get_field(Artist.Fields._authorities),
        get_field(BusinessUser.Fields._chatRooms),
        new Seszt<>(),
        get_field(Artist.Fields._bandMemberships),
        get_field(Artist.Fields._gigs),
        get_field(Artist.Fields._runner),
        get_field(Artist.Fields._follows),
        get_field(Artist.Fields._requests),
        get_field(Artist.Fields._ratings),
        get_field(User.Fields._history),
        get_field(Model.Fields._timestamp)
    );
  }
}
