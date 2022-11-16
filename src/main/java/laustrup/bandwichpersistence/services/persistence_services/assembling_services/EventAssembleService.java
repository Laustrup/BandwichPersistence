package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

public class EventAssembleService {

    /**
     * Singleton instance of the Service.
     */
    private static EventAssembleService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static EventAssembleService get_instance() {
        if (_instance == null) _instance = new EventAssembleService();
        return _instance;
    }

    private EventAssembleService() {}
}
