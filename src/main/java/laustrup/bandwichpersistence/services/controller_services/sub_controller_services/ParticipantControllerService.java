package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;

public class ParticipantControllerService extends ControllerService<Participant> {

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
