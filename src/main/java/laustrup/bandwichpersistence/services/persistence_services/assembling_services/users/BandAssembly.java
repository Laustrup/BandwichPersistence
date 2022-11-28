package laustrup.bandwichpersistence.services.persistence_services.assembling_services.users;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.ModelAssembly;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
        long id = set.getLong("users.id");
        String username = set.getString("users.username"), description = set.getString("users.`description`");
        ContactInfo contactInfo = ModelAssembly.get_instance().assembleContactInfo(set);
        Album images = new Album(username+":images", Album.Kind.IMAGE);
        Liszt<Rating> ratings = new Liszt<>();
        Liszt<Event> events = new Liszt<>();
        Liszt<ChatRoom> chatRooms = new Liszt<>();
        Subscription subscription = ModelAssembly.get_instance().assembleSubscription(set);
        Liszt<Bulletin> bulletins = new Liszt<>();
        LocalDateTime timestamp = set.getTimestamp("users.`timestamp`").toLocalDateTime();
        Liszt<Album> music = new Liszt<>();
        Liszt<Artist> members = new Liszt<>();
        String runner = set.getString("gear.`description`");
        Liszt<Participant> fans = new Liszt<>();
        Liszt<User> followings = new Liszt<>();

        do {

        } while (set.next());

        return new Band(id,username,description,contactInfo,images,ratings,events,chatRooms,subscription,bulletins,
                timestamp,music,members,runner,fans,followings);
    }
}
