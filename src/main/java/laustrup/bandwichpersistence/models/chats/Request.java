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

    /**
     * This message will be shown for the user, in order to inform of the request.
     */
    @Getter @Setter
    private String _message;

    public Request(User user, Event event, Plato approved, String message) {
        _user = user;
        _event = event;
        _approved = approved;
        _message = message;
    }
    public Request(User user, Event event, Plato approved) {
        _user = user;
        _event = event;
        _approved = approved;
    }

    /**
     * Will set the approved to true.
     */
    public void approve() { _approved.set_argument(true); }
}
