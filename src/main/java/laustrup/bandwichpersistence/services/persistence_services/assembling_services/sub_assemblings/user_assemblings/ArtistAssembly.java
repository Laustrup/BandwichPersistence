package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build Artists from database row values.
 * Is a singleton.
 */
public class ArtistAssembly extends UserAssembler {

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
     * Assembles Artists with values from the ResultSet.
     * @param set A ResultSet from the database, must include the values intended for the assembled object.
     * @return Artists made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Liszt<Artist> assembles(ResultSet set, boolean isTemplate) throws SQLException {
        Liszt<Artist> artists = new Liszt<>();

        if (set != null) {
            while (!set.isAfterLast()) {
                if (set.isBeforeFirst())
                    set.next();
                artists.add(assemble(set,isTemplate));
                set.next();
            }
        }

        return artists;
    }

    /**
     * Assembles an Artist with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return An Artist object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Artist assemble(ResultSet set, boolean isTemplate) throws SQLException {
        setupUserAttributes(set);
        Liszt<Gig> gigs = new Liszt<>();
        String runner = set.getString("gear.description");
        Liszt<User> fans = new Liszt<>();
        Liszt<User> idols = new Liszt<>();
        Liszt<Request> requests = new Liszt<>();
        Liszt<Long> bandIds = new Liszt<>();

        if (!isTemplate) {
            do {
                if (_id != set.getLong("users.id"))
                    break;

                _albums = _handler.handleAlbums(set, _albums);
                _ratings = _handler.handleRatings(set, _ratings);
                gigs = _handler.handleGigs(set, gigs);
                _events = _handler.handleEvents(set, _events);
                _chatRooms = _handler.handleChatRooms(set, _chatRooms);
                _bulletins = _handler.handleBulletins(set, _bulletins, false);

                if (set.getLong("followings.idol_id") == _id)
                    fans = _handler.handleFans(set, fans);
                else
                    idols = _handler.handleIdols(set, idols);

                requests = _handler.handleRequests(set, requests, new Artist(_id));

                if (!bandIds.contains(set.getLong("band_members.band_id")))
                    bandIds.add(set.getLong("band_members.band_id"));
            } while (set.next());
        }

        Artist artist = new Artist(_id, _username, _firstName, _lastName, _description, _contactInfo, _albums, _ratings, _events, gigs,
                _chatRooms, _subscription, _bulletins,
                !isTemplate&&!bandIds.isEmpty() ? BandAssembly.get_instance().assembles(UserRepository.get_instance().get(bandIds), true) : new Liszt<>(),
                runner, fans, idols, requests, _timestamp);

        resetUserAttributes();
        return artist;
    }
}
