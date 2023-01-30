package laustrup.bandwichpersistence.models.dtos.events;

import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.bandwichpersistence.models.events.Participation;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Determines type of which a Participant is participating in an Event.
 */
@NoArgsConstructor @Data
public class ParticipationDTO extends ModelDTO {

    /**
     * The Participant of the participation.
     */
    private ParticipantDTO participant;

    /**
     * The Event of the participation.
     */
    private EventDTO event;

    /**
     * The type of which participant is participating in the participation.
     */
    private ParticipationType type;

    public ParticipationDTO(Participation participation) {
        super(participation.get_event().get_primaryId(), participation.get_event().get_primaryId(),
                "Participation of participant " +
                        participation.get_participant().get_primaryId() + " AND Event " +
                        participation.get_event().get_primaryId(), participation.get_timestamp());
        participant = new ParticipantDTO(participation.get_participant());
        event = new EventDTO(participation.get_event());
        type = ParticipationType.valueOf(participation.get_type().toString());
    }

    /**
     * Each Participation have four different choices of participating.
     */
    public enum ParticipationType { ACCEPTED, IN_DOUBT, CANCELED, INVITED }
}