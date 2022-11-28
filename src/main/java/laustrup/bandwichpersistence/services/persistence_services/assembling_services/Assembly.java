package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.users.UserAssembly;

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
        User user = UserAssembly.get_instance().assemble(login);

        user.setSubscriptionUser();
        user.setImagesAuthor();

        UserRepository.get_instance().closeConnection();
        return user;
    }

    /**
     * Gets a User object with the informations given from the UserRepository.
     * Will be initiated as the object it is meant to be.
     * @param id The id of the User that is wished to be assembled.
     * @return The assembled User.
     */
    public User getUser(long id) {
        User user = UserAssembly.get_instance().assemble(id);

        user.setSubscriptionUser();
        user.setImagesAuthor();

        UserRepository.get_instance().closeConnection();
        return user;
    }
}
