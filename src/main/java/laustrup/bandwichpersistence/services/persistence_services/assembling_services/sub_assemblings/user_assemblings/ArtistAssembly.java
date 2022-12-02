package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
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
    public Liszt<Artist> assembles(ResultSet set) throws SQLException {
        Liszt<Artist> artists = new Liszt<>();

        while (!set.isAfterLast()) {
            if (set.isBeforeFirst())
                set.next();
            artists.add(assemble(set));
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
    public Artist assemble(ResultSet set) throws SQLException {
        setupUserAttributes(set);
        Liszt<Gig> gigs = new Liszt<>();
        Liszt<Album> music = new Liszt<>();
        String runner = set.getString("gear.`description`");
        Liszt<User> fans = new Liszt<>();
        Liszt<User> idols = new Liszt<>();
        Liszt<Request> requests = new Liszt<>();
        Liszt<Long> bandIds = new Liszt<>();

        do {
            if (_id != set.getLong("users.id"))
                break;

            if (Album.Kind.valueOf(set.getString("albums.kind")).equals(Album.Kind.IMAGE))
                _images = _handler.handleEndpoints(set, _images);
            else
                // User is by default set to Artist, but that might change, when Album is described.
                music = _handler.handleMusic(set, music, new Artist(set.getLong("album_relations.user_id")));

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

        Liszt<Band> bands = BandAssembly.get_instance().assembles(UserRepository.get_instance().get(bandIds));

        resetUserAttributes();
        return new Artist(_id, _username, _firstName, _lastName, _description, _contactInfo, _images, _ratings, _events, gigs,
                _chatRooms, _subscription, _bulletins, _timestamp, music, bands, runner, fans, idols, requests);
    }
}
