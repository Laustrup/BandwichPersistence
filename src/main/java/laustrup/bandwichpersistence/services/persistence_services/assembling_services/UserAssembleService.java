package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;

import java.sql.ResultSet;

public class UserAssembleService {

    private User _user;

    /**
     * Singleton instance of the Service.
     */
    private static UserAssembleService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserAssembleService get_instance() {
        if (_instance == null) _instance = new UserAssembleService();
        return _instance;
    }

    private UserAssembleService() {}

    public User assemble(Login login) {
        return assemble(UserRepository.get_instance().get(login));
    }

    public User assemble(long id) {
        return assemble(UserRepository.get_instance().get(id));
    }

    private User assemble(ResultSet resultSet) {


        UserRepository.get_instance().closeConnection();
        return _user;
    }
}
