package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.models.Response;
import laustrup.models.chats.messages.Bulletin;
import laustrup.dtos.events.EventDTO;
import laustrup.models.events.Event;
import laustrup.models.events.Participation;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.EventPersistenceService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.parameters.Plato;

import org.springframework.http.ResponseEntity;

public class EventControllerService extends ControllerService<EventDTO> {

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
     * @return A ResponseEntity with the Response of Event and the HttpStatus.
     */
    public ResponseEntity<Response<EventDTO>> get(long id) {
        return entityContent(new EventDTO(Assembly.get_instance().getEvent(id)));
    }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting all Events.
     * Uses an assemblyService for reading the database and building the Event objects.
     * @return The created ResponseEntity of all Events.
     */
    public ResponseEntity<Response<EventDTO[]>> get() {
        Liszt<Event> events = Assembly.get_instance().getEvents();
        EventDTO[] dtos = new EventDTO[events.size()];
        for (int i = 0; i < dtos.length; i++)
            dtos[i] = new EventDTO(events.Get(i+1));
        return entityContent(dtos);
    }

    /**
     * Will create an Event and afterwards put it in a ResponseEntity.
     * @param event The Event that is wished to be created.
     * @return A ResponseEntity with the Event and the HttpStatus.
     */
    public ResponseEntity<Response<EventDTO>> create(Event event) {
        return entityContent(new EventDTO(EventPersistenceService.get_instance().create(event)));
    }

    /**
     * Will delete Event and create a ResponseEntity with a Response that includes its status of the delete.
     * @param event The Event that should be deleted.
     * @return The created ResponseEntity of a Response with the status of the delete.
     */
    public ResponseEntity<Response<Plato.Argument>> delete(Event event) {
        return platoContent(EventPersistenceService.get_instance().delete(event));
    }

    /**
     * Will update Event and create a ResponseEntity with a Response of the current state of Event.
     * @param event The Event that should be updated.
     * @return The created ResponseEntity of a Response with the current state of Event.
     */
    public ResponseEntity<Response<EventDTO>> update(Event event) {
        return entityContent(new EventDTO(EventPersistenceService.get_instance().update(event)));
    }

    /**
     * Will upsert Participations of an Event and create a ResponseEntity with a Response of the current state of Event.
     * @param participations The Participations that should be upserted.
     * @return The created ResponseEntity of a Response with the current state of Event.
     */
    public ResponseEntity<Response<EventDTO>> upsert(Liszt<Participation> participations) {
        return entityContent(new EventDTO(EventPersistenceService.get_instance().upsert(participations)));
    }

    /**
     * Will upsert a Bulletin of an Event and create a ResponseEntity with a Response of the current state of Event.
     * @param bulletin The Bulletin that should be upserted.
     * @return The created ResponseEntity of a Response with the current state of Event.
     */
    public ResponseEntity<Response<EventDTO>> upsert(Bulletin bulletin) {
        return entityContent(new EventDTO(EventPersistenceService.get_instance().upsert(bulletin)));
    }
}
