package laustrup.bandwichpersistence.models.dtos.events;

import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.PerformerDTO;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.services.DTOService;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Determines a specific gig of one band for a specific time.
 */
@NoArgsConstructor @Data
public class GigDTO extends ModelDTO {
    /**
     * The Event of this Gig.
     */
    private EventDTO event;

    /**
     * This act is of a Gig and can both be assigned as artists or bands.
     */
    private PerformerDTO[] act;

    /**
     * The start of the Gig, where the act will begin.
     */
    private LocalDateTime start;

    /**
     * The end of the Gig, where the act will end.
     */
    private LocalDateTime end;

    public GigDTO(Gig gig) {
        super(gig.get_primaryId(), "Gig:"+gig.get_primaryId(), gig.get_timestamp());
        event = new EventDTO(gig.get_event());
        act = new PerformerDTO[gig.get_act().length];
        for (int i = 0; i < act.length; i++)
            act[i] = (PerformerDTO) DTOService.get_instance().convertToDTO(gig.get_act()[i]);
        start = gig.get_start();
        end = gig.get_end();
    }
}