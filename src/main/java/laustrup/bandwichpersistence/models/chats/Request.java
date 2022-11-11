package laustrup.bandwichpersistence.models.chats;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.users.User;

import laustrup.bandwichpersistence.utilities.Plato;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Determines if a User have approved to be a part of the Event.
 */
@ToString
public class Request {

    /**
     * The User that needs to approve the Event.
     */
    @Getter
    private User _user;

    /**
     * The Event that has been requested for.
     */
    @Getter
    private Event _event;

    /**
     * The value that indicates if the request for the Event has been approved.
     */
    @Getter @Setter
    private Plato _approved;

    public Request(User user, Event event, Plato approved) {
        _user = user;
        _event = event;
        _approved = approved;
    }
}
