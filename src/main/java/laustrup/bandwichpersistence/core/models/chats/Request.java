package laustrup.bandwichpersistence.core.models.chats;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.services.ModelService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.UserService.fromBusinessUser;

/**
 * Determines if a User have approved to be a part of the Event.
 */
@Getter @FieldNameConstants
public class Request {

    /**
     * The User that needs to approve the Event.
     */
    private BusinessUser _user;

    /**
     * The Event that has been requested for.
     */
    private Event _event;

    /**
     * The value that indicates if the request for the Event has been approved.
     * From the first date that isn't null, this has been approved.
     */
    @Setter
    private Instant _approved;

    /**
     * Will set the approved to now and therefore approve from now on.
     * In case that it is already approved, nothing will happen.
     */
    public void approve() {
        if (_approved == null)
            _approved = Instant.now();
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

    private Instant _timestamp;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param request The transport object to be transformed.
     */
    public Request(DTO request) {
        this(
                fromBusinessUser(request.getUser()),
                new Event(request.getEvent()),
                request.getApproved(),
                request.getTimestamp()
        );
    }

    public Request(
            BusinessUser user,
            Event event,
            Instant approved,
            Instant timestamp
    ) {
        if (user == null || event == null)
            throw new IllegalArgumentException("User and event are both required for Request with timestamp: " + timestamp);

        _user = user;
        _event = event;
        _approved = approved;
        _timestamp = timestamp;
    }

    @Override
    public String toString() {
        return ModelService.defineToString(
            getClass().getSimpleName(),
            get_userId(),
            get_eventId(),
            new String[]{
                "userId",
                "eventId",
                Fields._approved,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(get_userId()),
                String.valueOf(get_eventId()),
                _approved != null ? _approved.toString() : null,
                String.valueOf(_timestamp)
            }
        );
    }

    public UUID get_userId() {
        return _user.get_id();
    }

    public UUID get_eventId() {
        return _event.get_id();
    }

    /** Determines if a User have approved to be a part of the Event. */
    @Getter
    public static class DTO {

        /** The User that needs to approve the Event. */
        private BusinessUser.BusinessUserDTO user;

        /** The Event that has been requested for. */
        private Event.DTO event;

        /** The value that indicates if the request for the Event has been approved. */
        private Instant approved;

        private Instant timestamp;

        /**
         * Converts into this DTO Object.
         * @param request The Object to be converted.
         */
        public DTO(Request request) {
            user = fromBusinessUser(request.get_user());
            event = new Event.DTO(request.get_event());
            approved = request.get_approved();
            timestamp = request.get_timestamp();
        }
    }
}
