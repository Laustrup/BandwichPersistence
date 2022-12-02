package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;

import java.sql.ResultSet;

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

    public ResultSet get() {
        return get("");
    }

    public ResultSet get(long id) {
        return get("WHERE `events`.id = " + id);
    }

    public ResultSet get(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("`events`.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return get(where.toString());
    }

    public ResultSet search(String query) {
        query = query.replaceAll("%","");
        return get("WHERE `events`.title LIKE '%" + query + "%' OR " +
                "`events`.description LIKE '%" + query + "%' OR " +
                "`events`.location LIKE '%" + query + "%'");
    }

    private ResultSet get(String where) {
        return read("SELECT * FROM `events` " +
                "INNER JOIN contact_informations ON `events`.venue_id = contact_informations.id " +
                "INNER JOIN gigs ON `events`.id = gigs.event_id " +
                "INNER JOIN acts ON gigs.id = acts.gig_id " +
                "INNER JOIN requests ON `events`.id = requests.event_id " +
                "INNER JOIN participations ON `events`.id = participations.event_id " +
                "INNER JOIN event_bulletins ON `events`.id = event_bulletins.receiver_id " +
                "INNER JOIN users ON `events`.venue_id = users.id OR acts.user_id = users.id " +
                    "OR requests.user_id = users.id OR participations.participant_id = users.id " +
                        "OR event_bulletins.author_id = users.id " +
                "INNER JOIN event_albums ON `events`.id = event_albums.event_id " +
                "INNER JOIN co_" +
                where + ";");
    }
}
