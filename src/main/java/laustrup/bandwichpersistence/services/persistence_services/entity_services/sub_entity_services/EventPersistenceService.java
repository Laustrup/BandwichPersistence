package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.repositories.sub_repositories.EventRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.EntityService;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains logic for CRUD of Events.
 */
public class EventPersistenceService extends EntityService<Event> {

    /**
     * Singleton instance of the Service.
     */
    private static EventPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static EventPersistenceService get_instance() {
        if (_instance == null) _instance = new EventPersistenceService();
        return _instance;
    }

    private EventPersistenceService() {}

    /**
     * Will create an Event by using EventRepository.
     * Only does this, if id doesn't already exist.
     * Will also include the generated key.
     * Uses Assembly to get the values from the database,
     * to insure it exists and also to close connections.
     * At the moment only upserts ContactInfos, not Gigs, Requests or Participations.
     * These needs to be inserted afterwards.
     * @param event The Event that will be created.
     * @return If success, the created Event with its generated key, otherwise null.
     */
    public Event create(Event event) {
        if (event.get_primaryId() == 0) {
            ResultSet set = EventRepository.get_instance().create(event);
            ContactInfo contactInfo = event.get_contactInfo();

            try {
                if (set.isBeforeFirst())
                    set.next();
                event = Assembly.get_instance().getEventUnassembled(set.getLong("`events`.id"));
            } catch (SQLException e) {
                Printer.get_instance().print("ResultSet error in Event create service...", e);
                return null;
            }

            //Puts in contactInfo
            /*
            event = new Event(event.get_primaryId(),event.get_title(), event.get_description(), event.get_openDoors(),
                    event.get_voluntary(),event.get_public(),event.get_cancelled(),event.get_soldOut(), event.get_location(),
                    event.get_price(),event.get_ticketsURL(),contactInfo,event.get_gigs(),event.get_venue(),event.get_requests(),
                    event.get_participations(),event.get_bulletins(),event.get_images(),event.get_timestamp());
             */

            ModelRepository.get_instance().upsert(contactInfo);
            return Assembly.get_instance().finish(event);
        }
        return null;
    }

    /**
     * Will delete an Event and connections from repository will be closed.
     * @param event The Event that will be deleted.
     * @return A Plato with true truth if success, otherwise false with a message.
     */
    public Plato delete(Event event) {
        if (event.get_primaryId()>0)
            return new Plato(EventRepository.get_instance().delete(event));
        Plato status = new Plato(false);
        status.set_message("Couldn't delete " + event.get_title() + "...");
        return status;
    }
}
