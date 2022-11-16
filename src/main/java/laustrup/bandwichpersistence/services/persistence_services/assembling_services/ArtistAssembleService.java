package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

public class ArtistAssembleService {

    /**
     * Singleton instance of the Service.
     */
    private static ArtistAssembleService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistAssembleService get_instance() {
        if (_instance == null) _instance = new ArtistAssembleService();
        return _instance;
    }

    private ArtistAssembleService() {}
}
