package laustrup.bandwichpersistence.services.controller_services;

public class EventControllerService {

    /**
     * Singleton instance of the Service.
     */
    private static EventControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static EventControllerService get_instance() {
        if (_instance == null) _instance = new EventControllerService();
        return _instance;
    }

    private EventControllerService() {}
}
