package laustrup.bandwichpersistence.services.controller_services;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.utilities.Liszt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param event The Event that is either null or not and should be returned.
     * @return The created ResponseEntity of an Event.
     */
    private ResponseEntity<Event> entityContent(Event event) {
        if (event != null) return new ResponseEntity<>(event, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param events The Events that is either null or not and should be returned.
     * @return The created ResponseEntity of Events.
     */
    private ResponseEntity<Liszt<Event>> entityContent(Liszt<Event> events) {
        if (events != null) return new ResponseEntity<>(events, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
