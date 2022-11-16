package laustrup.bandwichpersistence.services.controller_services;


import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import org.springframework.http.HttpEntity;

public class VenueControllerService {

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

    /*
    public HttpEntity<Venue> getVenue() {

    }
     */
}
