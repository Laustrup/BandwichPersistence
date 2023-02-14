package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Are handling Repository actions for Venues.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class VenueRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static VenueRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static VenueRepository get_instance() {
        if (_instance == null) _instance = new VenueRepository();
        return _instance;
    }

    private VenueRepository() {}

    /**
     * Will create a Venue with its gear description, size and location and gets the generated key value if success.
     * Uses Assembly getUserUnassembled(), therefore it must finish and close connection.
     * @param venue The Venue that will be created.
     * @param password The password assigned for the Venue.
     * @return The Venue id created. If there's an SQLException, it returns null.
     */
    public Long create(Venue venue, String password) {
        try {
            ResultSet set = create(UserRepository.get_instance().insertUserSQL(venue,password)).getGeneratedKeys();

            if (set.isBeforeFirst())
                set.next();

            long id = set.getLong("id");

            create("INSERT INTO venues(" +
                        "user_id," +
                        "`size`," +
                        "location" +
                    ") " +
                    "VALUES(" +
                        id + "," +
                        venue.get_size() + "," +
                        varCharColumn(venue.get_location()) +
                    "); " +
                    "INSERTS INTO gear(" +
                        "user_id," +
                        "`description`" +
                    ") VALUES (" +
                        id + "," +
                        varCharColumn(venue.get_gearDescription()) +
                    ");");

            return id;
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Venue...",e);
        }

        return null;
    }
}
