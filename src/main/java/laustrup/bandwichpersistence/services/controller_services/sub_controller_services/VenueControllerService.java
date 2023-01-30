package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;


import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.venues.VenueDTO;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.VenuePersistenceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class VenueControllerService extends ControllerService<VenueDTO> {

    /**
     * Singleton instance of the Service.
     */
    private static VenueControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static VenueControllerService get_instance() {
        if (_instance == null) _instance = new VenueControllerService();
        return _instance;
    }

    private VenueControllerService() {}

    /**
     * Will create an Participant and afterwards put it in a ResponseEntity.
     * @param venue The Participant that is wished to be created.
     * @return A ResponseEntity with the Response of Venue and the HttpStatus.
     */
    public ResponseEntity<Response<VenueDTO>> create(Venue venue, String password) {
        if (new Login(venue.get_username(), password).passwordIsValid())
            return entityContent(new VenueDTO(VenuePersistenceService.get_instance().create(venue,password)));
        else
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.INVALID_PASSWORD_FORMAT),
                    HttpStatus.NOT_ACCEPTABLE);
    }
}
