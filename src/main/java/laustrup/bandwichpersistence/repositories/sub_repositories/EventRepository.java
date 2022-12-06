package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    /**
     * Will collect a JDBC ResultSet of all Events from the database, by using a SQL statement.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get() {
        return get("");
    }

    /**
     * Will collect a JDBC ResultSet of an Event from the database, by using a SQL statement.
     * @param id The id of the Event, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(long id) {
        return get("WHERE `events`.id = " + id);
    }

    /**
     * Will collect a JDBC ResultSet of several Events from the database, by using a SQL statement.
     * @param ids The ids of the Events, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("`events`.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return get(where.toString());
    }

    /**
     * Will collect a JDBC ResultSet of all Events that has something in common with a search query from the database,
     * by using a SQL statement.
     * It will compare columns of titles, descriptions and locations.
     * Doesn't order them by relevance at the moment.
     * @param query The search query, that is a line that should have something in common with columns.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet search(String query) {
        query = query.replaceAll("%","");
        return get("WHERE `events`.title LIKE '%" + query + "%' OR " +
                "`events`.description LIKE '%" + query + "%' OR " +
                "`events`.location LIKE '%" + query + "%'");
    }

    /**
     * This is the method that can through a SQL statement find and collect the Event.
     * Both from an id/ids or a search query, that is given from other public methods.
     * @param where The where statement, that decides what information that is being looked for.
     * @return The collected JDBC ResultSet.
     */
    private ResultSet get(String where) {
        return read("SELECT * FROM `events` " +
                "INNER JOIN contact_informations ON `events`.venue_id = contact_informations.id " +
                "INNER JOIN gigs ON `events`.id = gigs.event_id " +
                "INNER JOIN acts ON gigs.id = acts.gig_id " +
                "INNER JOIN requests ON `events`.id = requests.event_id " +
                "INNER JOIN participations ON `events`.id = participations.event_id " +
                "INNER JOIN event_bulletins ON `events`.id = event_bulletins.receiver_id " +
                "INNER JOIN album_relations ON `events`.id = album_relations.event_id " +
                "INNER JOIN albums ON album_relations.album_id = albums.id " +
                "INNER JOIN album_endpoints ON album.id = album_endpoints " +
                "INNER JOIN users ON `events`.venue_id = users.id OR acts.user_id = users.id " +
                    "OR requests.user_id = users.id OR participations.participant_id = users.id " +
                        "OR event_bulletins.author_id = users.id OR album_relations.user_id = users.id " +
                where + ";");
    }

    /**
     * Will create aN Event and get the generated key value if success.
     * @param event The Event that will be created.
     * @return A ResultSet of the created values with the generated keys. If there's an SQLException, it returns null.
     */
    public ResultSet create(Event event) {
        try {
            return create("INSERT INTO `events`(" +
                        "title," +
                        "open_doors," +
                        "`description`," +
                        "is_voluntary," +
                        "is_public," +
                        "is_cancelled," +
                        "is_sold_out," +
                        "location," +
                        "price," +
                        "tickets_url," +
                        "venue_id," +
                        "`timestamp`" +
                    ") " +
                    "VALUES (" +
                        event.get_title() +"','" +
                        event.get_openDoors() + "','" +
                        event.get_description() + "','" +
                        event.get_voluntary().get_argument() + "','" +
                        event.get_public().get_argument() + "','" +
                        event.get_cancelled().get_argument() + "','" +
                        event.get_soldOut().get_argument() + "','" +
                        event.get_location() + "'," +
                        event.get_price() + ",'" +
                        event.get_ticketsURL() + "'," +
                        event.get_venue().get_primaryId() + "," +
                    "NOW());").getGeneratedKeys();
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Event...", e);
        }
        return null;
    }
}
