package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    /**
     * Will create an Artist and get the generated key value if success.
     * @param artist The Artist that will be created.
     * @param password The password assigned for the Artist.
     * @return A ResultSet of the created values with the generated keys. If there's an SQLException, it returns null.
     */
    public ResultSet create(Artist artist, String password) {
        try {
            return create("INSERT INTO users(username,`password`,first_name,last_name,`description`,`timestamp`,kind) " +
                    "VALUES ('" +
                    artist.get_username() + "','" +
                    password + "','" +
                    artist.get_firstName() +"','" +
                    artist.get_lastName() +"','" +
                    artist.get_description() +"'," +
                    "NOW(),'ARTIST'); ").getGeneratedKeys();
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Artist...", e);
        }
        return null;
    }
}
