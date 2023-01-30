package laustrup.bandwichpersistence.models.events;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.dtos.events.ParticipationDTO;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.DTOService;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Determines type of which a Participant is participating in an Event.
 */
public class Participation extends Model {

    /**
     * The Participant of the participation.
     */
    @Getter
    private Participant _participant;

    /**
     * The Event of the participation.
     */
    @Getter
    private Event _event;

    /**
     * The type of which participant is participating in the participation.
     */
    @Getter @Setter
    private ParticipationType _type;

    public Participation(ParticipationDTO participation) {
        super(participation.getEvent().getPrimaryId(), participation.getParticipant().getPrimaryId(),
                "Participation of participant " +
                        participation.getParticipant().getPrimaryId() + " AND Event " +
                        participation.getEvent().getPrimaryId()
        );
        _participant = (Participant) DTOService.get_instance().convertFromDTO(participation.getParticipant());
        _event = new Event(participation.getEvent());
        _type = ParticipationType.valueOf(participation.getType().toString());
    }
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

    @Override
    public String toString() {
        return "Participation(" +
                    "primaryId:" + _primaryId +
                    ",secondaryId:" + _secondaryId +
                    ",title:" + _title +
                    ",type:" + _type +
                ")";
    }

    /**
     * Each Participation have four different choices of participating.
     */
    public enum ParticipationType { ACCEPTED, IN_DOUBT, CANCELED, INVITED }
}