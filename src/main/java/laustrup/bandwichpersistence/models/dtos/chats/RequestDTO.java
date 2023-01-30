package laustrup.bandwichpersistence.models.dtos.chats;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.events.EventDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.Plato;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Determines if a User have approved to be a part of the Event.
 */
@NoArgsConstructor @Data
public class RequestDTO extends ModelDTO {

    /**
     * The User that needs to approve the Event.
     */
    private UserDTO user;

    /**
     * The Event that has been requested for.
     */
    private EventDTO event;

    /**
     * The value that indicates if the request for the Event has been approved.
     */
    private Plato.Argument approved;

    /**
     * This message will be shown for the user, in order to inform of the request.
     */
    private String message;

    public RequestDTO(Request request) {
        super(request.get_secondaryId(), request.get_secondaryId(),
                "Request of " + request.get_primaryId() + " to " + request.get_secondaryId(),
                request.get_timestamp());
        user = DTOService.get_instance().convertToDTO(request.get_user());
        event = new EventDTO(request.get_event());
        approved = request.get_approved().get_argument();
        message = request.get_message();
    }
}
