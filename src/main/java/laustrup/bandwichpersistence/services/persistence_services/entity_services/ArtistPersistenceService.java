package laustrup.bandwichpersistence.services.persistence_services.entity_services;

public class ArtistPersistenceService {

    /**
     * Singleton instance of the Service.
     */
    private static ArtistPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistPersistenceService get_instance() {
        if (_instance == null) _instance = new ArtistPersistenceService();
        return _instance;
    }

    private ArtistPersistenceService() {}
}
