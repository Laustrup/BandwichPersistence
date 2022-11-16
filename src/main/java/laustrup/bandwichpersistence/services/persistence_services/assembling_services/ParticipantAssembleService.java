package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

public class ParticipantAssembleService {

    /**
     * Singleton instance of the Service.
     */
    private static ParticipantAssembleService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ParticipantAssembleService get_instance() {
        if (_instance == null) _instance = new ParticipantAssembleService();
        return _instance;
    }

    private ParticipantAssembleService() {}
}
