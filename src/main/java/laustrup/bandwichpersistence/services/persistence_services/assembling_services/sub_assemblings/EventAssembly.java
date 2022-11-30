package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings;

public class EventAssembly {

    /**
     * Singleton instance of the Service.
     */
    private static EventAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static EventAssembly get_instance() {
        if (_instance == null) _instance = new EventAssembly();
        return _instance;
    }

    private EventAssembly() {}
}