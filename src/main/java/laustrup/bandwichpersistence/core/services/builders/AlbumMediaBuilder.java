package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlbumMediaBuilder extends BandwichBuilderService<Album.Media> {

  private static AlbumMediaBuilder _instance;

  public static AlbumMediaBuilder get_instance() {
    if (_instance == null)
      _instance = new AlbumMediaBuilder();

    return _instance;
  }

  @Override
  protected void completion(Album.Media reference, Album.Media object) {

  }
}
