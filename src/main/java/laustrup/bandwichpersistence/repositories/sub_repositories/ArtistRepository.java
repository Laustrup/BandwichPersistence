package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Printer;

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
     * @return The generated id.
     */
    public long create(Artist artist, String password) {
        try {
            ResultSet set = create("INSERT INTO users(" +
                        "username," +
                        "email," +
                        "`password`," +
                        "first_name,last_name," +
                        "`description`," +
                        "`timestamp`," +
                        "kind) " +
                    "VALUES ('" +
                        artist.get_username() + "','" +
                        artist.get_contactInfo().get_email() + "','" +
                        password + "','" +
                        artist.get_firstName() +"','" +
                        artist.get_lastName() +"','" +
                        artist.get_description() +"'," +
                    "NOW(),'ARTIST');").getGeneratedKeys();

            if (set.isBeforeFirst())
                set.next();
            long id = set.getLong(1);

            edit("INSERT INTO gear(" +
                        "user_id," +
                        "description) " +
                    "VALUES (" +
                        id + ",'" +
                        artist.get_runner() +
                    "');",false);

            UserRepository.get_instance().createSubscriptionAndContactInfo(new Artist(id, artist.get_username(), artist.get_firstName(), artist.get_lastName(),
                    artist.get_description(), artist.get_contactInfo(), artist.get_albums(), artist.get_ratings(), artist.get_events(),
                    artist.get_gigs(), artist.get_chatRooms(), artist.get_subscription(), artist.get_bulletins(), artist.get_bands(),
                    artist.get_runner(), artist.get_fans(), artist.get_idols(), artist.get_requests(), artist.get_timestamp()
            ));

            return id;
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Artist...", e);
        }
        return 0;
    }
}
