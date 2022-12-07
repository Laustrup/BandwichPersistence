package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.repositories.sub_repositories.VenueRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.EntityService;

/**
 * Contains logic for CRUD of Venues.
 */
public class VenuePersistenceService extends EntityService<Venue> {

    /**
     * Singleton instance of the Service.
     */
    private static VenuePersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static VenuePersistenceService get_instance() {
        if (_instance == null) _instance = new VenuePersistenceService();
        return _instance;
    }

    private VenuePersistenceService() {}

    /**
     * Will create a Venue by using VenueRepository.
     * Only does this, if id doesn't already exist.
     * Will also include the generated key.
     * Uses Assembly to get the values from the database,
     * to insure it exists and also to close connections.
     * @param venue The Participant that will be created.
     * @param password The password for the Participant.
     * @return If success, the created Band with its generated key, otherwise null.
     */
    public Venue create(Venue venue, String password) {
        if (venue.get_primaryId() == 0) {
            Long id = VenueRepository.get_instance().create(venue, password);
            Subscription subscription = venue.get_subscription();
            ContactInfo contactInfo = venue.get_contactInfo();

            if (id!=null)
                venue = (Venue) Assembly.get_instance().getUserUnassembled(id);

            //Puts in subscription and contactInfo
            venue = new Venue(venue.get_primaryId(),venue.get_username(),venue.get_description(),contactInfo,
                    venue.get_images(),venue.get_ratings(),venue.get_events(),venue.get_chatRooms(),venue.get_timestamp(),
                    venue.get_location(), venue.get_gearDescription(), subscription.get_status(),subscription.get_offer(),
                    venue.get_bulletins(),venue.get_size(),venue.get_requests());

            if (upsert(venue))
                return venue;
        }
        return null;
    }
}
