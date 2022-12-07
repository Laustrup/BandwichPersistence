package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.ArtistPersistenceService;
import org.springframework.http.HttpStatus;
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

    /**
     * Will create an Artist and afterwards put it in a ResponseEntity.
     * @param artist The Artist that is wished to be created.
     * @param password The password assigned for the Artist.
     * @return A ResponseEntity with the Artist and the HttpStatus.
     */
    public ResponseEntity<Response<Artist>> create(Artist artist, String password) {
        if (new Login(artist.get_username(), password).passwordIsValid())
            return entityContent(ArtistPersistenceService.get_instance().create(artist, password));
        else
            return new ResponseEntity<>(new Response<>(artist, Response.StatusType.INVALID_PASSWORD_FORMAT),
                    HttpStatus.NOT_ACCEPTABLE);
    }

}
