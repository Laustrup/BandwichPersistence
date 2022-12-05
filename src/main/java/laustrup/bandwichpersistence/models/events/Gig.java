package laustrup.bandwichpersistence.models.events;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Determines a specific gig of one band for a specific time.
 */
@Data
public class Gig extends Model {
    /**
     * The Event of this Gig.
     */
    private Event _event;

    /**
     * This act is of a Gig and can both be assigned as artists or bands.
     */
    private Performer[] _act;

    /**
     * The start of the Gig, where the act will begin.
     */
    private LocalDateTime _start;

    /**
     * The end of the Gig, where the act will end.
     */
    private LocalDateTime _end;

    public Gig(Performer[] act) {
        super("New gig");
        _act = act;
    }

    public Gig(long id, Event event, Performer[] act, LocalDateTime start, LocalDateTime end, LocalDateTime timestamp) {
        super(id, "Gig:"+id, timestamp);
        _event = event;
        _act = act;
        _start = start;
        _end = end;

        _assembling = true;
    }

    public Gig(Event event, Performer[] act, LocalDateTime start, LocalDateTime end) {
        super("New gig");
        _event = event;
        _act = act;
        _start = start;
        _end = end;
    }

    /**
     * Checks if a Performer is a part of the act.
     * @param performer The Performer object that is wished to be checked.
     * @return True if the primary ids matches of the Performer and a Performer of the act,
     *         otherwise false.
     */
    public boolean contains(Performer performer) {
        for (Performer actor : _act)
            if (actor.get_primaryId() == performer.get_primaryId())
                return true;

        return false;
    }

    /**
     * Will add a Performer to the act.
     * @param performer The Performer object that is wished to be added.
     * @return All the Performers of the act.
     */
    public Performer[] add(Performer performer) {
        Performer[] storage = new Performer[_act.length+1];

        for (int i = 0; i < _act.length; i++)
            storage[i] = _act[i];

        storage[_act.length] = performer;
        _act = storage;

        return _act;
    }

    @Override
    public String toString() {
        return "Gig(id:" + _primaryId +
                "start:" + _start.toString() +
                "end:" + _end.toString() +
                ")";
    }
}