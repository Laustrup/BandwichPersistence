package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build Users from database row values.
 * Is a singleton.
 */
public class UserAssembly {

    /**
     * Singleton instance of the Service.
     */
    private static UserAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserAssembly get_instance() {
        if (_instance == null) _instance = new UserAssembly();
        return _instance;
    }

    private UserAssembly() {}

    /**
     * Builds a User object with the informations given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param login An object containing username and password.
     * @return The assembled User.
     */
    public User assemble(Login login) { return assemble(UserRepository.get_instance().get(login),true); }

    /**
     * Builds a User object with the informations given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param id The id of the User that is wished to be assembled.
     * @return The assembled User.
     */
    public User assemble(long id) { return assemble(UserRepository.get_instance().get(id),true); }

    /**
     * Builds all the User objects, where the informations are given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @return All the assembled Users.
     */
    public Liszt<User> assembles() {
        return assembles(UserRepository.get_instance().get());
    }

    /**
     * Builds User objects that are alike the search query, where the informations are given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param searchQuery The search that should have something in common with some Users.
     * @return The assembled Users similar to the search query.
     */
    public Liszt<User> assembles(String searchQuery) {
        return assembles(UserRepository.get_instance().search(searchQuery));
    }

    /**
     * The private method that assembles multiple Users, that are defined in other public methods.
     * @param set The ResultSet that will define the values for the Users.
     * @return The assembled Users.
     */
    private Liszt<User> assembles(ResultSet set) {
        Liszt<User> users = new Liszt<>();

        try {
            while (!set.isAfterLast()) {
                if (set.isBeforeFirst())
                    set.next();
                users.add(assemble(set, false));
            }
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't assemble Users...", e);
            return null;
        }

        return users;
    }

    /**
     * Rebuilds Users that are only with ids.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param users The User objects that should be described.
     * @return The described Users.
     */
    public Liszt<User> describe(Liszt<User> users) {
        Liszt<Long> ids = new Liszt<>();
        for (User user : users)
            ids.add(user.get_primaryId());

        users = new Liszt<>();
        ResultSet set = UserRepository.get_instance().get(ids);

        for (long id : ids) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                users.add(assemble(set, false));
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't describe Users...", e);
            }
        }

        return users;
    }

    /**
     * Assembles the User from a ResultSet.
     * Will not close the Connection to the database, when it is done assembling.
     * @param set A JDBC ResultSet that is gathered from the rows of the SQL statement to the database.
     * @param preInitiate If true, it will set the ResultsSet at first row, should only be done,
     *                   if there is only expected a single entity.
     * @return The assembled User.
     */
    private User assemble(ResultSet set, boolean preInitiate) {
        User user = null;

        try {
            if (preInitiate)
                set.next();
            switch (set.getString("users.kind")) {
                case "BAND" -> user = BandAssembly.get_instance().assemble(set);
                case "ARTIST" -> user = ArtistAssembly.get_instance().assemble(set);
                case "VENUE" -> user = VenueAssembly.get_instance().assemble(set);
                case "PARTICIPANT" -> user = ParticipantAssembly.get_instance().assemble(set);
            }
        } catch (SQLException e) {
            Printer.get_instance().print("Trouble assembling user...", e);
        }

        return user;
    }
}
