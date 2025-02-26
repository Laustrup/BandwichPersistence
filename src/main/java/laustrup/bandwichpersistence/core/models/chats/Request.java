package laustrup.bandwichpersistence.core.models.chats;

import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.services.DTOService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

import static laustrup.bandwichpersistence.core.models.User.UserDTO;

/**
 * Determines if a User have approved to be a part of the Event.
 */
@Getter @FieldNameConstants
public class Request extends Model {

    /**
     * The User that needs to approve the Event.
     */
    private User _user;

    /**
     * The Event that has been requested for.
     */
    private Event _event;

    /**
     * The value that indicates if the request for the Event has been approved.
     * From the first date that isn't null, this has been approved.
     */
    @Setter
    private LocalDateTime _approved;

    /**
     * Will set the approved to now and therefore approve from now on.
     * In case that it is already approved, nothing will happen.
     */
    public void approve() {
        if (_approved == null)
            _approved = LocalDateTime.now();
    }

    /**
     * Will tell if the Request is approved, by whether the time is approved was null.
     * @return True if the approved is null.
     */
    public boolean isApproved() {
        return _approved != null;
    }

    /**
     * Will set the approved to null and therefore not approved.
     */
    public void deny() {
        _approved = null;
    }

    /**
     * This message will be shown for the user, in order to inform of the request.
     */
    @Setter
    private String _message;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param request The transport object to be transformed.
     */
    public Request(DTO request) {
        super(request);
        _user = (User) DTOService.convert(request.getUser());
        _event = new Event(request.getEvent());
        _approved = request.getApproved();
        _message = request.getMessage();
    }

    /**
     * A constructor with all values as parameters, is meant for assembling from database.
     * @param user The User that is requested for the Gig.
     * @param event The Event that is hosting the Gig.
     * @param approved Defines if the Request have been approved.
     * @param message Details added about the Request, is optional.
     * @param history The Events for this object.
     * @param timestamp The date and time the Request was made.
     */
    public Request(
            User user,
            Event event,
            LocalDateTime approved,
            String message,
            History history,
            Instant timestamp
    ) {
        super(
            user != null ? user.get_id() : null,
            event != null ? event.get_id() : null,
            user != null && event != null
                ? "Request of " + user.get_username() + " to " + event.get_name() : "Empty Request",
            history,
            timestamp
        );

        if (user == null || event == null)
            throw new IllegalArgumentException("User and event are both required for Request with timestamp: " + timestamp);

        _user = user;
        _event = event;
        _approved = approved;
        _message = message;
    }

    /**
     * Default constructor to generate Requests that are just created.
     * @param user The User that is requested for the Gig.
     * @param event The Event that is hosting the Gig.
     */
    public Request(User user, Event event) {
        super(
                user.get_id(),
                event.get_id(),
                "Request of " + user.get_username() + " to " + event.get_name()
        );
        _message = """
                This request wishes @user to perform at @event
                """
                .replace("@user", user.get_username())
                .replace("@event", event.get_name());
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._id,
                Model.Fields._secondaryId,
                Fields._approved,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_id),
                String.valueOf(_secondaryId),
                _approved != null ? _approved.toString() : null,
                String.valueOf(_timestamp)
            }
        );
    }

    /** Determines if a User have approved to be a part of the Event. */
    @Getter
    public static class DTO extends ModelDTO {

        /** The User that needs to approve the Event. */
        private UserDTO user;

        /** The Event that has been requested for. */
        private Event.DTO event;

        /** The value that indicates if the request for the Event has been approved. */
        private LocalDateTime approved;

        /** This message will be shown for the user, in order to inform of the request. */
        private String message;

        /**
         * Converts into this DTO Object.
         * @param request The Object to be converted.
         */
        public DTO(Request request) {
            super(request);
            user = DTOService.convert(request.get_user());
            event = new Event.DTO(request.get_event());
            approved = request.get_approved();
            message = request.get_message();
        }
    }
}
