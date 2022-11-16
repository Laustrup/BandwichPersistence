package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.repositories.Repository;

/**
 * Are handling Repository actions for Events.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class EventRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static EventRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static EventRepository get_instance() {
        if (_instance == null) _instance = new EventRepository();
        return _instance;
    }

    private EventRepository() {}
}
