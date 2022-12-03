package laustrup.bandwichpersistence.services.controller_services;

import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerService {

    /**
     * Singleton instance of the Service.
     */
    private static UserControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserControllerService get_instance() {
        if (_instance == null) _instance = new UserControllerService();
        return _instance;
    }

    private UserControllerService() {}

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for logging in.
     * Uses an assemblyService for reading the database and building the User object.
     * Checks if the username is its email and that the email of the object isn't null.
     * @param login An object containing username and password.
     * @return The created ResponseEntity of a User.
     */
    public ResponseEntity<User> get(Login login) {
        User user = Assembly.get_instance().getUser(login);

        if ((login.usernameIsEmailKind() &&
                (user.get_contactInfo().get_email() == null || user.get_contactInfo().get_email().isEmpty())) ||
                !login.usernameIsEmailKind())
            return entityContent(user);

        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting a User by its id.
     * Uses an assemblyService for reading the database and building the User object.
     * @param id The id of the User, that is wished to be gathered.
     * @return The created ResponseEntity of a User.
     */
    public ResponseEntity<User> get(long id) {
        return entityContent(Assembly.get_instance().getUser(id));
    }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting a Search by a String query.
     * Uses an assemblyService for reading the database and building the Search object.
     * @param query The String query of the Search, that is wished to be gathered.
     * @return The created ResponseEntity of a Search.
     */
    public ResponseEntity<Search> search(String query) { return searchContent(Assembly.get_instance().search(query)); }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param user The User that is either null or not and should be returned.
     * @return The created ResponseEntity of a User.
     */
    private ResponseEntity<User> entityContent(User user) {
        if (user != null) return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param search The Search that is either null or not and should be returned.
     * @return The created ResponseEntity of a Search.
     */
    private ResponseEntity<Search> searchContent(Search search) {
        if (search != null) return new ResponseEntity<>(search, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
