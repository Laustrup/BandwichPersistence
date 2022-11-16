package laustrup.bandwichpersistence.services.controller_services;

public class PerformerControllerService {

    /**
     * Singleton instance of the Service.
     */
    private static PerformerControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static PerformerControllerService get_instance() {
        if (_instance == null) _instance = new PerformerControllerService();
        return _instance;
    }

    private PerformerControllerService() {}
}
