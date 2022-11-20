package laustrup.bandwichpersistence.models.events;

import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Determines a specific gig of one band for a specific time.
 */
@Data
@ToString
public class Gig {
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

    public Gig(Performer[] act, LocalDateTime start, LocalDateTime end) {
        _act = act;
        _start = start;
        _end = end;
    }
}