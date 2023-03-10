package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.utilities.collections.Liszt;
import laustrup.bandwichpersistence.utilities.console.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

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
    public Liszt<Band> assembles(ResultSet set, boolean isTemplate) throws SQLException {
        Liszt<Band> bands = new Liszt<>();

        if (set != null)
            while (set.next())
                bands.addUnique(assemble(set, isTemplate));

        return bands;
    }

    /**
     * Assembles a Band with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return A Band object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Band assemble(ResultSet set, boolean isTemplate) throws SQLException {
        setupUserAttributes(set);
        Liszt<Gig> gigs = new Liszt<>();
        Liszt<Long> memberIds = new Liszt<>();
        String runner = set.getString("gear.description");
        Liszt<User> fans = new Liszt<>();
        Liszt<User> idols = new Liszt<>();

        if (!isTemplate) {
            do {
                if (_id != set.getLong("users.id"))
                    break;

                _albums = _handler.handleAlbums(set, _albums);
                _ratings = _handler.handleRatings(set, _ratings);
                gigs = _handler.handleGigs(set, gigs);
                _events = _handler.handleEvents(set, _events);
                _chatRooms = _handler.handleChatRooms(set, _chatRooms);
                _bulletins = _handler.handleBulletins(set, _bulletins);

                if (set.getLong("followings.idol_id") == _id)
                    fans = _handler.handleFans(set, fans);
                else
                    idols = _handler.handleIdols(set, idols);

                if (!memberIds.contains(set.getLong("band_members.artist_id")))
                    memberIds.add(set.getLong("band_members.artist_id"));
            } while (set.next());
        }

        try {
            Band band = new Band(_id, _username, _description, _contactInfo, _albums, _ratings, _events, gigs, _chatRooms,
                    _subscription, _bulletins,
                    !isTemplate&&!memberIds.isEmpty() ? ArtistAssembly.get_instance().assembles(UserRepository.get_instance().get(memberIds),true) : new Liszt<>(),
                    runner, fans, idols, _timestamp);

            resetUserAttributes();
            return band;
        } catch (InputMismatchException e) {
            Printer.get_instance().print("Couldn't assemble band...",e);
        }

        return null;
    }
}
