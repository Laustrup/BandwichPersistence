package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.models.dtos.events.EventDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Is used for response of a search request,
 * contains different objects that are alike of the search query.
 */
@NoArgsConstructor @Data
public class Search {

    /**
     * All the Users that contains similarities with a search query.
     */
    private UserDTO[] users;

    /**
     * All the Events that contains similarities with a search query.
     */
    private EventDTO[] events;

    public Search(Liszt<User> users, Liszt<Event> events) {
        this.users = new UserDTO[users.size()];
        for (int i = 0; i < this.users.length; i++)
            this.users[i] = DTOService.get_instance().convertToDTO(users.get(i+1));
        this.events = new EventDTO[events.size()];
        for (int i = 0; i < this.events.length; i++)
            this.events[i] = new EventDTO(events.get(i+1));
    }
}
