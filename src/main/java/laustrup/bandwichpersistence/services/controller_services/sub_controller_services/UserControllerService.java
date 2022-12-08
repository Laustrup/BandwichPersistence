package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.UserPersistenceService;
import laustrup.bandwichpersistence.utilities.Liszt;

import laustrup.bandwichpersistence.utilities.Plato;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerService extends ControllerService<User> {

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
     * @return The created ResponseEntity of a User as Response.
     */
    public ResponseEntity<Response<User>> get(Login login) {
        User user = Assembly.get_instance().getUser(login);
        if (user == null)
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.WRONG_PASSWORD),
                    HttpStatus.NOT_ACCEPTABLE);

        if ((login.usernameIsEmailKind() &&
                (user.get_contactInfo().get_email() != null || !user.get_contactInfo().get_email().isEmpty())) ||
                !login.usernameIsEmailKind())
            return entityContent(user);

        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting a User by its id.
     * Uses an assemblyService for reading the database and building the User object.
     * @param id The id of the User, that is wished to be gathered.
     * @return The created ResponseEntity of a User as Response.
     */
    public ResponseEntity<Response<User>> get(long id) {
        return entityContent(Assembly.get_instance().getUser(id));
    }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting all Users.
     * Uses an assemblyService for reading the database and building the User objects.
     * @return The created ResponseEntity of all Users as Response.
     */
    public ResponseEntity<Response<Liszt<User>>> get() { return entityContent(Assembly.get_instance().getUsers()); }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting a Search by a String query.
     * Uses an assemblyService for reading the database and building the Search object.
     * @param query The String query of the Search, that is wished to be gathered.
     * @return The created ResponseEntity of a Response of Search.
     */
    public ResponseEntity<Response<Search>> search(String query) { return searchContent(Assembly.get_instance().search(query)); }

    /**
     * Will delete User and create a ResponseEntity with a Response that includes its status of the delete.
     * @param user The User that should be deleted.
     * @return The created ResponseEntity of a Response with the status of the delete.
     */
    public ResponseEntity<Response<Plato>> delete(User user) {
        return platoContent(UserPersistenceService.get_instance().delete(user));
    }

    /**
     * Will upsert Bulletin of a User and create a ResponseEntity with a Response of the current state of Receiver.
     * @param bulletin The Bulletin that should be upserted.
     * @return The created ResponseEntity of a Response with the current state of Receiver.
     */
    public ResponseEntity<Response<User>> upsert(Bulletin bulletin) {
        return entityContent(UserPersistenceService.get_instance().upsert(bulletin));
    }
}