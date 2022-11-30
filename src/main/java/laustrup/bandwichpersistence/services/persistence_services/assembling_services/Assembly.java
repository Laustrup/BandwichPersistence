package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;

/**
 * This class uses other assemblies to build objects from database,
 * finishes details and closes database connections.
 * Only use this assembly class outside of assembly package.
 */
public class Assembly {

    /**
     * Singleton instance of the Service.
     */
    private static Assembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static Assembly get_instance() {
        if (_instance == null) _instance = new Assembly();
        return _instance;
    }

    private Assembly() {}

    /**
     * Gets a User object with the informations given from the UserRepository.
     * Will be initiated as the object it is meant to be.
     * @param login An object containing username and password.
     * @return The assembled User.
     */
    public User getUser(Login login) {
        return assemble(UserAssembly.get_instance().assemble(login));
    }

    /**
     * Gets a User object with the informations given from the UserRepository.
     * Will be initiated as the object it is meant to be.
     * @param id The id of the User that is wished to be assembled.
     * @return The assembled User.
     */
    public User getUser(long id) {
        return assemble(UserAssembly.get_instance().assemble(id));
    }

    /**
     * Finishes the last assembling of a User, in order to get all values.
     * @param user The User that will be further assembled.
     * @return The assembled User.
     */
    private User assemble(User user) {
        if (user.getClass() == Artist.class ||
                user.getClass() == Band.class) {
            ((Performer) user).set_followings(UserAssembly.get_instance().assembles(((Performer) user).get_followings()));
            ((Performer) user).set_fans(UserAssembly.get_instance().assembles(((Performer) user).get_fans()));
        }
        else if (user.getClass() == Participant.class)
            ((Participant) user).set_followings(UserAssembly.get_instance().assembles(((Participant) user).get_followings()));

        user.setSubscriptionUser();
        user.setImagesAuthor();
        if (user.getClass() == Artist.class || user.getClass() == Band.class)
            ((Performer) user).setAuthorOfAlbums();

        user.doneAssembling();
        UserRepository.get_instance().closeConnection();
        return user;
    }
}
