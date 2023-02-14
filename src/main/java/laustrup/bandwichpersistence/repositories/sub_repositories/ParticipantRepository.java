package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Are handling Repository actions for Participants.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class ParticipantRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static ParticipantRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ParticipantRepository get_instance() {
        if (_instance == null) _instance = new ParticipantRepository();
        return _instance;
    }

    private ParticipantRepository() {}

    /**
     * Will create a Participant and get the generated key value if success.
     * @param participant The Participant that will be created.
     * @param password The password assigned for the Participant.
     * @return A ResultSet of the created values with the generated keys. If there's an SQLException, it returns null.
     */
    public ResultSet create(Participant participant, String password) {
        try {
            return create(UserRepository.get_instance().insertUserSQL(participant,password)).getGeneratedKeys();
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Participant...",e);
        }
        return null;
    }
}
