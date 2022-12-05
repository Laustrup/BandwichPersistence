package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;

/**
 * Are handling Repository actions for objects that aren't of a specific User.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class MiscRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static MiscRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static MiscRepository get_instance() {
        if (_instance == null) _instance = new MiscRepository();
        return _instance;
    }

    private MiscRepository() {}

    public ResultSet chatRooms(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("chat_rooms.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return read("SELECT * FROM chat_rooms " +
                "INNER JOIN chatters ON chat_rooms.id = chatters.chat_room_id " +
                "INNER JOIN mails ON chat_rooms.id = mails.chat_room id " +
                where + ";");
    }

    public ResultSet bulletins(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("user_bulletins.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return read("SELECT * FROM user_ " +
                "INNER JOIN chatters ON chat_rooms.id = chatters.chat_room_id " +
                "INNER JOIN mails ON chat_rooms.id = mails.chat_room id " +
                where + ";");
    }

    /**
     * Will collect a JDBC ResultSet of a Card from the database, by using a SQL statement.
     * @param id The id of the Card, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet card(long id) {
        return read("SELECT * FROM cards WHERE id = " + id + ";");
    }
}
