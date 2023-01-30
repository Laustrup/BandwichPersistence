package laustrup.bandwichpersistence.models.chats;

import laustrup.bandwichpersistence.models.dtos.chats.RequestDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.Plato;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Determines if a User have approved to be a part of the Event.
 */
public class Request extends Model {

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

    public Request(RequestDTO request) {
        super(request.getPrimaryId(), request.getEvent().getPrimaryId(),
                "Request of " + request.getUser().getUsername() + " to " + request.getEvent().getTitle(),
                request.getTimestamp());
        _user = DTOService.get_instance().convertFromDTO(request.getUser());
        _event = new Event(request.getEvent());
        _approved = new Plato(request.getApproved());
        _message = request.getMessage();
    }
    public Request(User user, Event event, Plato approved, String message, LocalDateTime timestamp) {
        super(user.get_primaryId(), event.get_primaryId(), "Request of " + user.get_username() + " to " + event.get_title(),timestamp);
        _user = user;
        _event = event;
        _approved = approved;
        _message = message;
        _assembling = true;
    }
    public Request(User user, Event event, Plato approved) {
        super(user.get_primaryId(), event.get_primaryId(), "Request of " + user.get_username() + " to " + event.get_title(),
                LocalDateTime.now());
        _user = user;
        _event = event;
        _approved = approved;
    }

    /**
     * Will set the approved to true.
     */
    public void approve() { _approved.set_argument(true); }

    /**
     * Will set the User, but only if it is under assembling.
     * @param user The User of this Request.
     * @return The User of this Request.
     */
    public User set_user(User user) {
        if (_assembling)
            _user = user;
        return _user;
    }

    /**
     * Will set the Event, but only if it is under assembling.
     * @param event The Event of this Request.
     * @return The Event of this Request.
     */
    public Event set_event(Event event) {
        if (_assembling)
            _event = event;
        return _event;
    }

    @Override
    public String toString() {
        return "Request(" +
                    "primaryId:" + _primaryId +
                    ",secondaryId:" + _secondaryId +
                    ",approved:" + _approved.get_argument() +
                    ",timestamp:" + _timestamp +
                ")";
    }
}
