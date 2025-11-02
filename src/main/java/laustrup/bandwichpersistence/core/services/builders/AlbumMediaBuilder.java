package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;
import laustrup.bandwichpersistence.core.models.Model;

import java.util.UUID;

public class AlbumMediaBuilder extends BuilderService<Album.Media> {

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

  @Override
  protected Album.Media construct() {
    return new Album.Media(
        new Album.Media.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Model.Fields._title),
        get_field(Album.Media.Fields._endpoint),
        get_field(Album.Media.Fields._kind),
        get_field(Model.Fields._timestamp)
    );
  }
}
