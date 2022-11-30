package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.repositories.sub_repositories.*;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

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
            ((Performer) user).set_idols(UserAssembly.get_instance().assembles(((Performer) user).get_idols()));
            ((Performer) user).set_fans(UserAssembly.get_instance().assembles(((Performer) user).get_fans()));
        }
        else if (user.getClass() == Participant.class)
            ((Participant) user).set_idols(UserAssembly.get_instance().assembles(((Participant) user).get_idols()));

        user.setSubscriptionUser();
        user.setImagesAuthor();
        if (user.getClass() == Artist.class || user.getClass() == Band.class)
            ((Performer) user).setAuthorOfAlbums();

        return finish(user);
    }

    /**
     * Will set the User as done assembling and close all open connections.
     * @param user The user that will be done assembling.
     * @return The assembled User.
     */
    private User finish(User user) {
        user.doneAssembling();

        Plato connectionStatus = closeConnections();
        if (!connectionStatus.get_truth())
            Printer.get_instance().print(connectionStatus.get_message(), new Exception());

        return user;
    }

    /**
     * Checks if all connections are closed, if they are open, they will be closed.
     * @return A Plato object, if the truth is true, then there have been no issue,
     *         if there is an issue, it will have a false truth and a message for the Printer.
     */
    private Plato closeConnections() {
        Plato status;
        Plato situation = new Plato(true);

        status = ArtistRepository.get_instance().connectionIsClosed();
        if (!status.get_truth() && status.get_argument() != Plato.Argument.UNDEFINED) {
            status = ArtistRepository.get_instance().closeConnection();
            if (!status.get_truth()) {
                situation.set_message("Couldn't close ArtistRepository at Assembly...");
                situation.set_argument(false);
            }
        }

        status = BandRepository.get_instance().connectionIsClosed();
        if (!status.get_truth() && status.get_argument() != Plato.Argument.UNDEFINED) {
            status = BandRepository.get_instance().closeConnection();
            if (!status.get_truth()) {
                situation.set_message("Couldn't close ArtistRepository at Assembly...");
                situation.set_argument(false);
            }
        }

        status = EventRepository.get_instance().connectionIsClosed();
        if (!status.get_truth() && status.get_argument() != Plato.Argument.UNDEFINED) {
            status = EventRepository.get_instance().closeConnection();
            if (!status.get_truth()) {
                situation.set_message("Couldn't close ArtistRepository at Assembly...");
                situation.set_argument(false);
            }
        }

        status = ParticipantRepository.get_instance().connectionIsClosed();
        if (!status.get_truth() && status.get_argument() != Plato.Argument.UNDEFINED) {
            status = ParticipantRepository.get_instance().closeConnection();
            if (!status.get_truth()) {
                situation.set_message("Couldn't close ArtistRepository at Assembly...");
                situation.set_argument(false);
            }
        }

        status = UserRepository.get_instance().connectionIsClosed();
        if (!status.get_truth() && status.get_argument() != Plato.Argument.UNDEFINED) {
            status = UserRepository.get_instance().closeConnection();
            if (!status.get_truth()) {
                situation.set_message("Couldn't close ArtistRepository at Assembly...");
                situation.set_argument(false);
            }
        }

        status = VenueRepository.get_instance().connectionIsClosed();
        if (!status.get_truth() && status.get_argument() != Plato.Argument.UNDEFINED) {
            status = VenueRepository.get_instance().closeConnection();
            if (!status.get_truth()) {
                situation.set_message("Couldn't close ArtistRepository at Assembly...");
                situation.set_argument(false);
            }
        }

        return situation;
    }
}
