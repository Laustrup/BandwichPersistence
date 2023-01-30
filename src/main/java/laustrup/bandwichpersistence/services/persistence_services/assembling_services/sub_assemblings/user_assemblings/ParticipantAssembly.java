package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build Participants from database row values.
 * Is a singleton.
 */
public class ParticipantAssembly extends UserAssembler {

    /**
     * Singleton instance of the Service.
     */
    private static ParticipantAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ParticipantAssembly get_instance() {
        if (_instance == null) _instance = new ParticipantAssembly();
        return _instance;
    }

    private ParticipantAssembly() {}

    /*
     * Assembles Participants with values from the ResultSet.
     * @param set A ResultSet from the database, must include the values intended for the assembled object.
     * @return Participants made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
    public Liszt<Participant> assembles(ResultSet set) throws SQLException {
        Liszt<Participant> participants = new Liszt<>();

        while (!set.isAfterLast()) {
            if (set.isBeforeFirst())
                set.next();
            participants.add(assemble(set));
        }

        return participants;
    }
     */

    /**
     * Assembles a Participant with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return A Participant object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Participant assemble(ResultSet set, boolean isTemplate) throws SQLException {
        setupUserAttributes(set);
        Liszt<User> idols = new Liszt<>();

        if (!isTemplate) {
            do {
                if (_id != set.getLong("users.id"))
                    break;

                _albums = _handler.handleAlbums(set, _albums);
                idols = _handler.handleIdols(set, idols);
            } while (set.next());
        }

        Participant participant = new Participant(_id, _username, _firstName, _lastName, _description, _contactInfo, _albums, _ratings,
                _events, _chatRooms, _subscription, _bulletins, idols, _timestamp);

        resetUserAttributes();
        return participant;
    }
}
