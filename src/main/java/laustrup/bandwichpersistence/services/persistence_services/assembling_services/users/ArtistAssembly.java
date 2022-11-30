package laustrup.bandwichpersistence.services.persistence_services.assembling_services.users;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
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
 * Will build Artists from database row values.
 * Is a singleton.
 */
public class ArtistAssembly {

    /**
     * Singleton instance of the Service.
     */
    private static ArtistAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistAssembly get_instance() {
        if (_instance == null) _instance = new ArtistAssembly();
        return _instance;
    }

    private ArtistAssembly() {}

    /**
     * Assembles an Artist with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return An Artist object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Artist assemble(ResultSet set) throws SQLException {
        long id = set.getLong("users.id");
        String username = set.getString("users.username"),
                firstName = set.getString("users.first_name"),
                last_name = set.getString("users.last_name"),
                description = set.getString("users.`description`");

        ContactInfo contactInfo = ModelAssembly.get_instance().assembleContactInfo(set);
        Album images = new Album(username+":images", Album.Kind.IMAGE);
        Liszt<Rating> ratings = new Liszt<>();
        Liszt<Gig> gigs = new Liszt<>();
        Liszt<Event> events = new Liszt<>();
        Liszt<ChatRoom> chatRooms = new Liszt<>();
        Subscription subscription = ModelAssembly.get_instance().assembleSubscription(set);
        Liszt<Bulletin> bulletins = new Liszt<>();
        LocalDateTime timestamp = set.getTimestamp("users.`timestamp`").toLocalDateTime();
        Liszt<Album> music = new Liszt<>();
        Liszt<Band> bands = new Liszt<>();
        String runner = set.getString("gear.`description`");
        Liszt<Participant> fans = new Liszt<>();
        Liszt<User> followings = new Liszt<>();
        Liszt<Request> requests = new Liszt<>();

        do {
            if (id != set.getLong("users.id"))
                break;

        } while (set.next());

        return new Artist(id, username, firstName, last_name, description, contactInfo, images, ratings, events, gigs,
                chatRooms, subscription, bulletins, timestamp, music, bands, runner, fans, followings, requests);
    }

    /**
     * Assembles Artists with values from the ResultSet.
     * @param set A ResultSet from the database, must include the values intended for the assembled object.
     * @return Artists made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Liszt<Artist> assembles(ResultSet set) throws SQLException {
        Liszt<Artist> artists = new Liszt<>();

        while (!set.isAfterLast())
            artists.add(assemble(set));

        return artists;
    }
}
