package laustrup.bandwichpersistence.models.events;

import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import lombok.Data;
import lombok.ToString;

/**
 * Determines type of which a Participant is participating in an Event.
 */
@Data
@ToString
public class Participation {
    /**
     * The Participant of the participation.
     */
    private Participant _participant;
    /**
     * The type of which participant is participating in the participation.
     */
    private ParticipationType _type;

    public Participation(Participant participant, ParticipationType type) {
        _participant = participant;
        _type = type;
    }

    /**
     * Each Participation have four different choices of participating.
     */
    public enum ParticipationType { ACCEPTED, IN_DOUBT, CANCELED, INVITED }
}