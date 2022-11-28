package laustrup.bandwichpersistence.services.persistence_services.assembling_services.users;

import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build Participants from database row values.
 * Is a singleton.
 */
public class ParticipantAssembly {

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

    /**
     * Assembles a Participant with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return A Participant object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Participant assemble(ResultSet set) throws SQLException {

        do {

        } while (set.next());

        return new Participant();
    }
}
