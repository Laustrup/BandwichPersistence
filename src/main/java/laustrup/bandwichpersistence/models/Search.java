package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;

/**
 * Is used for response of a search request,
 * contains different objects that are alike of the search query.
 */
public class Search {

    /**
     * All the Users that contains similarities with a search query.
     */
    @Getter
    private Liszt<User> _users;

    /**
     * All the Events that contains similarities with a search query.
     */
    @Getter
    private Liszt<Event> _events;

    public Search(Liszt<User> users, Liszt<Event> events) {
        _users = users;
        _events = events;
    }

    @Override
    public String toString() {
        String search = "Search(Users(";

        for (int i = 1; i <= _users.size(); i++) {
            search += _users.get(i).toString();
            if (i > _users.size())
                search += ", ";
        }
        search += "),Events(";

        for (int i = 1; i <= _events.size(); i++) {
            search += _events.get(i).toString();
            if (i > _events.size())
                search += ", ";
        }

        return search + "))";
    }
}
