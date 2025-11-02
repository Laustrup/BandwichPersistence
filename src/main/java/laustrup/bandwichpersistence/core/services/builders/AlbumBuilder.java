package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;
import laustrup.bandwichpersistence.core.models.Model;

import java.util.UUID;

public class AlbumBuilder extends BuilderService<Album> {

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

  @Override
  protected Album construct() {
    return new Album(
        new Album.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Model.Fields._title),
        get_field(Album.Fields._media),
        get_field(Model.Fields._timestamp)
    );
  }
}
