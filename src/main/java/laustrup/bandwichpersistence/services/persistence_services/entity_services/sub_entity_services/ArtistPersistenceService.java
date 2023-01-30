package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.repositories.DbGate;
import laustrup.bandwichpersistence.repositories.sub_repositories.ArtistRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.EntityService;

/**
 * Contains logic for CRUD of Artists.
 */
public class ArtistPersistenceService extends EntityService<Artist> {

    /**
     * Singleton instance of the Service.
     */
    private static ArtistPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistPersistenceService get_instance() {
        if (_instance == null) _instance = new ArtistPersistenceService();
        return _instance;
    }

    private ArtistPersistenceService() {}

    /**
     * Will create an Artist by using ArtistRepository.
     * Only does this, if id doesn't already exist.
     * Will also include the generated key.
     * Uses Assembly to get the values from the database,
     * to insure it exists and also to close connections.
     * @param artist The Artist that will be created.
     * @param password The password for the Artist.
     * @return If success, the created Artist with its generated key, otherwise null.
     */
    public Artist create(Artist artist, String password) {
        artist = (Artist) Assembly.get_instance().getUserUnassembled(ArtistRepository.get_instance().create(artist, password));
        DbGate.get_instance().close();
        return artist;
    }
}
