package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.bands.BandDTO;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.BandPersistenceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BandControllerService extends ControllerService<BandDTO> {

    /**
     * Singleton instance of the Service.
     */
    private static BandControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static BandControllerService get_instance() {
        if (_instance == null) _instance = new BandControllerService();
        return _instance;
    }

    private BandControllerService() {}

    /**
     * Will create a Band and afterwards put it in a ResponseEntity.
     * @param band The Band that is wished to be created.
     * @param password The password assigned for the Band.
     * @return A ResponseEntity with the Response of Band and the HttpStatus.
     */
    public ResponseEntity<Response<BandDTO>> create(Band band, String password) {
        if (new Login(band.get_username(), password).passwordIsValid())
            return entityContent(new BandDTO(BandPersistenceService.get_instance().create(band, password)));
        else
            return new ResponseEntity<>(new Response<>(new BandDTO(band), Response.StatusType.INVALID_PASSWORD_FORMAT),
                    HttpStatus.NOT_ACCEPTABLE);
    }
}
