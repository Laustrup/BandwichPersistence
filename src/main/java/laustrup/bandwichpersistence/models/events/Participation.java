package laustrup.bandwichpersistence.models.events;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Determines type of which a Participant is participating in an Event.
 */
@Data @ToString
public class Participation extends Model {

    /**
     * The Participant of the participation.
     */
    private Participant _participant;

    /**
     * The Event of the participation.
     */
    private Event _event;

    /**
     * The type of which participant is participating in the participation.
     */
    private ParticipationType _type;

    public Participation(Participant participant, Event event, ParticipationType type) {
        super(event.get_primaryId(), participant.get_primaryId(),
                "Participation of participant " +
                        participant.get_primaryId() + " AND Event " +
                        event.get_primaryId());
        _participant = participant;
        _event = event;
        _type = type;
    }

    public Participation(Participant participant, Event event, ParticipationType type, LocalDateTime timestamp) {
        super(event.get_primaryId(), participant.get_primaryId(),
                "Participation of participant " +
                        participant.get_primaryId() + " AND Event " +
                        event.get_primaryId(), timestamp);
        _participant = participant;
        _event = event;
        _type = type;
    }

    /**
     * Each Participation have four different choices of participating.
     */
    public enum ParticipationType { ACCEPTED, IN_DOUBT, CANCELED, INVITED }
}