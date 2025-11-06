package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;

public class AlbumMediaBuilder extends BandwichBuilderService<Album.Media> {

  private static AlbumMediaBuilder _instance;

  public static AlbumMediaBuilder get_instance() {
    if (_instance == null)
      _instance = new AlbumMediaBuilder();

    return _instance;
  }

  private AlbumMediaBuilder() {

  }

  @Override
  protected void completion(Album.Media reference, Album.Media object) {

  }
}
