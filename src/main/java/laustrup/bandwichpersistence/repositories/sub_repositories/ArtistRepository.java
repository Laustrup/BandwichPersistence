package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;

/**
 * Are handling Repository actions for Artists.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class ArtistRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static ArtistRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistRepository get_instance() {
        if (_instance == null) _instance = new ArtistRepository();
        return _instance;
    }

    private ArtistRepository() {}
}
