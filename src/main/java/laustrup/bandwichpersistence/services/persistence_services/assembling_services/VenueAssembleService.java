package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

public class VenueAssembleService {

    /**
     * Singleton instance of the Service.
     */
    private static VenueAssembleService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static VenueAssembleService get_instance() {
        if (_instance == null) _instance = new VenueAssembleService();
        return _instance;
    }

    private VenueAssembleService() {}
}
