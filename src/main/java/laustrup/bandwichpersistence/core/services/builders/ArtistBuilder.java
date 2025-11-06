package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.Artist;

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
  }
}
