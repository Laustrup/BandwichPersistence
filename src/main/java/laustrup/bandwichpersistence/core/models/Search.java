package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.services.UserService;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;

import lombok.Getter;
import lombok.Setter;

import static laustrup.bandwichpersistence.core.models.User.UserDTO;

/**
 * Is used for response of a search request,
 * contains different objects that are alike of the search query.
 */
@Getter @Setter
public class Search {

    /**
     * All the Users that contains similarities with a search query.
     */
    private UserDTO[] users;

    /**
     * All the Events that contains similarities with a search query.
     */
    private Event.DTO[] events;

    public Search(Liszt<User> users, Liszt<Event> events) {
        this.users = new UserDTO[users.size()];
        for (int i = 0; i < this.users.length; i++)
            this.users[i] = UserService.from(users.get(i));
        this.events = new Event.DTO[events.size()];
        for (int i = 0; i < this.events.length; i++)
            this.events[i] = new Event.DTO(events.get(i));
    }
}
