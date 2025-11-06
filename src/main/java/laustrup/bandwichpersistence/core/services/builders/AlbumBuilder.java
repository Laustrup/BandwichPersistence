package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;

public class AlbumBuilder extends BandwichBuilderService<Album> {

  private static AlbumBuilder _instance;

  public static AlbumBuilder get_instance() {
    if (_instance == null)
      _instance = new AlbumBuilder();

    return _instance;
  }

  private AlbumBuilder() {

  }

  @Override
  protected void completion(Album reference, Album object) {
    combine(reference.get_media(), object.get_media());
  }

}
