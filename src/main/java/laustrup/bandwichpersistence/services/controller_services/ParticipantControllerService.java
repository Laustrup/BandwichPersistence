package laustrup.bandwichpersistence.services.controller_services;

public class ParticipantControllerService {

    /**
     * Singleton instance of the Service.
     */
    private static ParticipantControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ParticipantControllerService get_instance() {
        if (_instance == null) _instance = new ParticipantControllerService();
        return _instance;
    }

    private ParticipantControllerService() {}
}
