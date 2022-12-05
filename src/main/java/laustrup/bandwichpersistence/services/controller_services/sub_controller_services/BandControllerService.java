package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;

public class BandControllerService extends ControllerService<Band> {

    /**
     * Singleton instance of the Service.
     */
    private static BandControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static BandControllerService get_instance() {
        if (_instance == null) _instance = new BandControllerService();
        return _instance;
    }

    private BandControllerService() {}
}
