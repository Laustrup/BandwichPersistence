package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.ArtistPersistenceService;
import org.springframework.http.ResponseEntity;

public class ArtistControllerService extends ControllerService<Artist> {

    /**
     * Singleton instance of the Service.
     */
    private static ArtistControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistControllerService get_instance() {
        if (_instance == null) _instance = new ArtistControllerService();
        return _instance;
    }

    private ArtistControllerService() {}


    public ResponseEntity<Artist> create(Artist artist, String password) {
        return entityContent(ArtistPersistenceService.get_instance().create(artist, password));
    }

}
