package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.utilities.Liszt;

import org.springframework.http.ResponseEntity;

public class EventControllerService extends ControllerService<Event> {

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

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting an Event by its id.
     * Uses an assemblyService for reading the database and building the Event object.
     * @param id The id of the Event, that is wished to be gathered.
     * @return The created ResponseEntity of an Event.
     */
    public ResponseEntity<Event> get(long id) { return entityContent(Assembly.get_instance().getEvent(id)); }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting all Events.
     * Uses an assemblyService for reading the database and building the Event objects.
     * @return The created ResponseEntity of all Events.
     */
    public ResponseEntity<Liszt<Event>> get() { return entityContent(Assembly.get_instance().getEvents()); }

}
