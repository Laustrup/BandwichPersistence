package laustrup.bandwichpersistence.services.persistence_services.entity_services;

import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.repositories.sub_repositories.ArtistRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistPersistenceService {

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
        if (artist.get_primaryId() == 0) {
            ResultSet set = ArtistRepository.get_instance().create(artist, password);

            try {
                if (set.isBeforeFirst())
                    set.next();
                return (Artist) Assembly.get_instance().getUser(set.getLong("users.id"));
            } catch (SQLException e) {
                Printer.get_instance().print("ResultSet error in Artist create service...", e);
            }
        }
        return null;
    }
}
