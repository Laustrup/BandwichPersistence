package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.repositories.Repository;

import java.sql.ResultSet;

/**
 * Are handling Repository actions for User's common uses.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class UserRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static UserRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserRepository get_instance() {
        if (_instance == null) _instance = new UserRepository();
        return _instance;
    }

    private UserRepository() {}

    /**
     * Will collect a JDBC ResultSet of a User from the database, by using a SQL statement.
     * Checks if the username is an email, but if username is email, it needs to be checked if it is null,
     * since it is declared to be needing an email to log in with an email.
     * @param login An object containing username and password.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(Login login) {
        return login.usernameIsEmailKind() ?
                get(" WHERE contact_informations.email = " + login.get_username() +
                        " AND users.`password` = " + login.get_password())
                : get(" WHERE users.username = " + login.get_username() +
                        " AND users.`password` = " + login.get_password());
    }

    /**
     * Will collect a JDBC ResultSet of a User from the database, by using a SQL statement.
     * @param id The id of the User, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(long id) {
        return get(" WHERE users.id = " + id);
    }

    /**
     * This is the method that can through a SQL statement find and collect the User.
     * Both from an id or a login, that is given from other public methods.
     * @param where The where statement, that decides what information that is being looked for.
     * @return The collected JDBC ResultSet.
     */
    private ResultSet get(String where) {
        return read("SELECT * FROM users " +
                "INNER JOIN band_members ON band_members.artist_id = users.id OR band_members.band_id = users.id " +
                "INNER JOIN gear ON gear.user_id = users.id " +
                "INNER JOIN venues ON venues.user_id = users.id " +
                "INNER JOIN `events` ON `events`.venue_id = users.id " +
                "INNER JOIN gigs ON gigs.event_id = `events`.id " +
                "INNER JOIN acts ON acts.gig_id = gigs.id " +
                "INNER JOIN participations ON participations.event_id = `events`.id" +
                "INNER JOIN followings ON followings.follower_id = users.id OR followings.lead_id = users.id " +
                "INNER JOIN chatters ON chatters.user_id = users.id " +
                "INNER JOIN chat_rooms ON chatters.chat_room_id = chat_rooms.id " +
                "INNER JOIN bulletins ON users.id = bulletins.receiver_id " +
                "INNER JOIN requests ON users.id = requests.user_id " +
                "INNER JOIN ratings ON users.id = ratings.appointed_id " +
                "INNER JOIN user_albums ON users.id = user_albums.user_id " +
                "INNER JOIN albums ON user_albums.album_id = albums.id " +
                "INNER JOIN album_endpoints ON albums.id = album_endpoints.album_id " +
                "INNER JOIN subscriptions ON users.id = subscriptions.user_id " +
                "INNER JOIN contact_informations ON users.id = contact_informations.user_id " +
                where + ";");
    }
}