package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

public class BandAssemblingService {

    /**
     * Singleton instance of the Service.
     */
    private static BandAssemblingService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static BandAssemblingService get_instance() {
        if (_instance == null) _instance = new BandAssemblingService();
        return _instance;
    }

    private BandAssemblingService() {}
}
