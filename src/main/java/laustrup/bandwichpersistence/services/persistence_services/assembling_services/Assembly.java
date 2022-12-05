package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.repositories.sub_repositories.*;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.EventAssembly;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

/**
 * This class uses other assemblies to build objects from database,
 * finishes details and closes database connections.
 * Only use this assembly class outside of assembly package.
 */
public class Assembly extends Assembler {

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
     * Gets a Search object with the informations given from the UserRepository.
     * @param query The search query of the Users that is wished to be assembled.
     * @return The assembled Search.
     */
    public Search search(String query) {
        Liszt<User> users = UserAssembly.get_instance().assembles(query);
        Liszt<Event> events = EventAssembly.get_instance().assembles(query);

        return finish(new Search(users, events));
    }

    /**
     * Gets a User object with the informations given from the UserRepository.
     * Will be initiated as the object it is meant to be.
     * @param login An object containing username and password.
     * @return The assembled User.
     */
    public User getUser(Login login) {
        return assembling(UserAssembly.get_instance().assemble(login), true);
    }

    /**
     * Gets a User object with the informations given from the UserRepository.
     * Will be initiated as the object it is meant to be.
     * @param id The id of the User that is wished to be assembled.
     * @return The assembled User.
     */
    public User getUser(long id) {
        return assembling(UserAssembly.get_instance().assemble(id), true);
    }

    /**
     * Will get all the Users.
     * @return All Users.
     */
    public Liszt<User> getUsers() { return userAssembling(UserAssembly.get_instance().assembles()); }

    /**
     * Finishes the last assembling of Users, in order to get all values.
     * @param users The Users that will be further assembled.
     * @return The assembled Users.
     */
    private Liszt<User> userAssembling(Liszt<User> users) {
        for (int i = 1; i <= users.size(); i++)
            users.set(i, assembling(users.get(i), false));

        return userFinishing(users);
    }

    /**
     * Finishes the last assembling of a User, in order to get all values.
     * @param user The User that will be further assembled.
     * @param willFinish Will set assembling as done and close connections, if true.
     * @return The assembled User.
     */
    private User assembling(User user, boolean willFinish) {
        if (user.getClass() == Artist.class ||
                user.getClass() == Band.class) {
            ((Performer) user).set_idols(_describer.describeUsers(((Performer) user).get_idols()));
            ((Performer) user).set_fans(_describer.describeUsers(((Performer) user).get_fans()));
        }
        else if (user.getClass() == Participant.class)
            ((Participant) user).set_idols(_describer.describeUsers(((Participant) user).get_idols()));

        user.set_chatRooms(_describer.describeChatRooms(user.get_chatRooms()));
        user.set_events(_describer.describeEvents(user.get_events()));

        Liszt<Bulletin> bulletins = _describer.describeBulletinAuthors(user.get_bulletins());
        for (int i = 1; i <= user.get_bulletins().size(); i++)
            user.get_bulletins().set(i, bulletins.get(i));
        user.set_bulletinReceivers();

        if (user.getClass() == Artist.class ||
            user.getClass() == Venue.class) {
            Liszt<Request> requests = user.getClass() == Artist.class ?
                    _describer.describeRequests(((Artist) user).get_requests(), true) :
                    _describer.describeRequests(((Venue) user).get_requests(), true);
            for (int i = 1; i <= requests.size(); i++) {
                if (user.getClass() == Artist.class)
                    ((Artist) user).get_requests().set(i, requests.get(i));
                else
                    ((Venue) user).get_requests().set(i, requests.get(i));
            }
            if (user.getClass() == Artist.class) {
                ((Artist) user).set_requestUsers();
                for (int i = 1; i <= requests.size(); i++)
                    ((Artist) user).get_requests().get(i).doneAssembling();
            }
            else {
                ((Venue) user).set_requestUsers();
                for (int i = 1; i <= requests.size(); i++)
                    ((Venue) user).get_requests().get(i).doneAssembling();
            }
        }

        user.setSubscriptionUser();
        user.setImagesAuthor();
        if (user.getClass() == Artist.class || user.getClass() == Band.class)
            ((Performer) user).setAuthorOfAlbums();

        if (willFinish)
            return finish(user);
        else
            return user;
    }

    /**
     * Gets an Event object with the informations given from the UserRepository.
     * Will be initiated as the object it is meant to be.
     * @param id The id of the Event that is wished to be assembled.
     * @return The assembled Event.
     */
    public Event getEvent(long id) { return assembling(EventAssembly.get_instance().assemble(id), true); }

    /**
     * Will get all the Events.
     * @return All Events.
     */
    public Liszt<Event> getEvents() { return eventAssembling(EventAssembly.get_instance().assembles()); }

    /**
     * Finishes the last assembling of Events, in order to get all values.
     * @param events The Events that will be further assembled.
     * @return The assembled Events.
     */
    private Liszt<Event> eventAssembling(Liszt<Event> events) {
        for (int i = 1; i <= events.size(); i++)
            events.set(i, assembling(events.get(i), false));

        return eventFinishing(events);
    }

    /**
     * Finishes the last assembling of an Event, in order to get all values.
     * @param event The Event that will be further assembled.
     * @param willFinish Will set assembling as done and close connections, if true.
     * @return The assembled Event.
     */
    private Event assembling(Event event, boolean willFinish) {
        event.set_venue((Venue) getUser(event.get_venue().get_primaryId()));


        if (willFinish)
            return finish(event);
        else
            return event;
    }

    /**
     * Will set the Search as done assembling and close all open connections.
     * @param search The Search that will be done assembling.
     * @return The assembled Search.
     */
    private Search finish(Search search) {
        Plato connectionStatus = closeConnections();
        if (!connectionStatus.get_truth())
            Printer.get_instance().print(connectionStatus.get_message(), new Exception());

        return search;
    }

    /**
     * Will set the User as done assembling and close all open connections.
     * @param user The User that will be done assembling.
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
     * Will set the Users as done assembling and close all open connections.
     * @param users The Users that will be done assembling.
     * @return The assembled Users.
     */
    private Liszt<User> userFinishing(Liszt<User> users) {
        for (User user : users)
            user.doneAssembling();

        Plato connectionStatus = closeConnections();
        if (!connectionStatus.get_truth())
            Printer.get_instance().print(connectionStatus.get_message(), new Exception());

        return users;
    }

    /**
     * Will set the Event as done assembling and close all open connections.
     * @param event The Event that will be done assembling.
     * @return The assembled Event.
     */
    private Event finish(Event event) {
        event.doneAssembling();

        Plato connectionStatus = closeConnections();
        if (!connectionStatus.get_truth())
            Printer.get_instance().print(connectionStatus.get_message(), new Exception());

        return event;
    }

    /**
     * Will set the Events as done assembling and close all open connections.
     * @param events The Events that will be done assembling.
     * @return The assembled Events.
     */
    private Liszt<Event> eventFinishing(Liszt<Event> events) {
        for (Event event : events)
            event.doneAssembling();

        Plato connectionStatus = closeConnections();
        if (!connectionStatus.get_truth())
            Printer.get_instance().print(connectionStatus.get_message(), new Exception());

        return events;
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

        status = MiscRepository.get_instance().connectionIsClosed();
        if (!status.get_truth() && status.get_argument() != Plato.Argument.UNDEFINED) {
            status = MiscRepository.get_instance().closeConnection();
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
