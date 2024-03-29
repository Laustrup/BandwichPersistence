package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.utilities.console.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Are handling Repository actions for Bands.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class BandRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static BandRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static BandRepository get_instance() {
        if (_instance == null) _instance = new BandRepository();
        return _instance;
    }

    private BandRepository() {}

    /**
     * Will create a Band and get the generated key value if success.
     * @param band The Band that will be created.
     * @param password The password assigned for the Band.
     * @return A ResultSet of the created values with the generated keys. If there's an SQLException, it returns null.
     */
    public ResultSet create(Band band, String password) {
        try {
            return create(UserRepository.get_instance().insertUserSQL(band,password)).getGeneratedKeys();
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Band...", e);
        }
        return null;
    }
}
