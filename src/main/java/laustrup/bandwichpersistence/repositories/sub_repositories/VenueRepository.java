package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.repositories.Repository;

/**
 * Are handling Repository actions for Venues.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class VenueRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static VenueRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static VenueRepository get_instance() {
        if (_instance == null) _instance = new VenueRepository();
        return _instance;
    }

    private VenueRepository() {}
}
