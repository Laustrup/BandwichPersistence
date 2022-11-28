package laustrup.bandwichpersistence.services.persistence_services.assembling_services.users;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build Bands from database row values.
 * Is a singleton.
 */
public class BandAssembly {

    /**
     * Singleton instance of the Service.
     */
    private static BandAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static BandAssembly get_instance() {
        if (_instance == null) _instance = new BandAssembly();
        return _instance;
    }

    private BandAssembly() {}

    /**
     * Assembles a Band with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return A Band object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Band assemble(ResultSet set) throws SQLException {
        long id;
        String username, description;
        ContactInfo contactInfo;
        Album images;
        Liszt<Rating> ratings = new Liszt<>();

        do {

        } while (set.next());

        return new Band();
    }
}
