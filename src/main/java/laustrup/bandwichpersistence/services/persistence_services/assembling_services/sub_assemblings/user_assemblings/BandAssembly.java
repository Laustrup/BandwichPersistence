package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build Bands from database row values.
 * Is a singleton.
 */
public class BandAssembly extends UserAssembler {

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
     * Assembles Bands with values from the ResultSet.
     * @param set A ResultSet from the database, must include the values intended for the assembled object.
     * @return Bands made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Liszt<Band> assembles(ResultSet set) throws SQLException {
        Liszt<Band> bands = new Liszt<>();

        while (!set.isAfterLast()) {
            if (set.isBeforeFirst())
                set.next();
            bands.add(assemble(set));
        }

        return bands;
    }

    /**
     * Assembles a Band with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return A Band object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Band assemble(ResultSet set) throws SQLException {
        setupUserAttributes(set);
        Liszt<Gig> gigs = new Liszt<>();
        Liszt<Album> music = new Liszt<>();
        Liszt<Long> memberIds = new Liszt<>();
        String runner = set.getString("gear.`description`");
        Liszt<User> fans = new Liszt<>();
        Liszt<User> idols = new Liszt<>();

        do {
            if (_id != set.getLong("users.id"))
                break;

            if (Album.Kind.valueOf(set.getString("albums.kind")).equals(Album.Kind.IMAGE))
                _images = _handler.handleEndpoints(set, _images);
            else
                // User is by default set to Band, but that might change, when Album is described.
                music = _handler.handleMusic(set, music, new Band(set.getLong("album_relations.user_id")));

            _ratings = _handler.handleRatings(set, _ratings);
            gigs = _handler.handleGigs(set, gigs);
            _events = _handler.handleEvents(set, _events);
            _chatRooms = _handler.handleChatRooms(set, _chatRooms);
            _bulletins = _handler.handleBulletins(set, _bulletins, false);

            if (set.getLong("followings.idol_id") == _id)
                fans = _handler.handleFans(set, fans);
            else
                idols = _handler.handleIdols(set, idols);

            if (!memberIds.contains(set.getLong("band_members.artist_id")))
                memberIds.add(set.getLong("band_members.artist_id"));

        } while (set.next());

        Liszt<Artist> members = ArtistAssembly.get_instance().assembles(UserRepository.get_instance().get(memberIds));

        resetUserAttributes();
        return new Band(_id, _username, _description, _contactInfo, _images, _ratings, _events, gigs, _chatRooms,
                _subscription, _bulletins, _timestamp, music, members, runner, fans, idols);
    }

}
