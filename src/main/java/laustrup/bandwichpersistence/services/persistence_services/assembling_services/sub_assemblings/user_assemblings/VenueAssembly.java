package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build Venues from database row values.
 * Is a singleton.
 */
public class VenueAssembly extends UserAssembler {

    /**
     * Singleton instance of the Service.
     */
    private static VenueAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static VenueAssembly get_instance() {
        if (_instance == null) _instance = new VenueAssembly();
        return _instance;
    }

    private VenueAssembly() {}

    /*
     * Assembles Venues with values from the ResultSet.
     * @param set A ResultSet from the database, must include the values intended for the assembled object.
     * @return Venues made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
    public Liszt<Venue> assembles(ResultSet set) throws SQLException {
        Liszt<Venue> venues = new Liszt<>();

        while (!set.isAfterLast()) {
            if (set.isBeforeFirst())
                set.next();
            venues.add(assemble(set));
        }

        return venues;
    }
     */

    /**
     * Assembles a Venue with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return A Venue object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Venue assemble(ResultSet set, boolean isTemplate) throws SQLException {
        setupUserAttributes(set);
        String location = set.getString("venues.location");
        String gear = set.getString("gear.description");
        int size = set.getInt("venues.size");
        Liszt<Request> requests = new Liszt<>();

        if (!isTemplate) {
            do {
                _albums = _handler.handleAlbums(set,_albums);
                requests = _handler.handleRequests(set, requests, new Venue(_id));
            } while (set.next());
        }

        Venue venue = new Venue(_id, _username, _description, _contactInfo, _albums, _ratings, _events, _chatRooms,
                location, gear, _subscription.get_status(), _subscription.get_offer(), _bulletins, size, requests,
                _timestamp);

        resetUserAttributes();
        return venue;
    }
}