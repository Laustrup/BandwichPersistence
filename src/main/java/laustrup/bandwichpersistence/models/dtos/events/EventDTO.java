package laustrup.bandwichpersistence.models.dtos.events;

import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.albums.AlbumDTO;
import laustrup.bandwichpersistence.models.dtos.chats.RequestDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.BulletinDTO;
import laustrup.bandwichpersistence.models.dtos.users.contact_infos.ContactInfoDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.venues.VenueDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.parameters.Plato;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * An Event is placed a gig, where a venue is having bands playing at specific times.
 */
@NoArgsConstructor @Data
public class EventDTO extends ModelDTO {

    /**
     * Is when the participants can enter the event,
     * not necessarily the same time as when the gigs start.
     * It must be before or same time as the gigs start.
     */
    private LocalDateTime openDoors;

    /**
     * This DateTime is determining when the first gig will start.
     * Is being calculated automatically.
     */
    private LocalDateTime start;

    /**
     * This DateTime is determining when the last gig will end.
     * Is being calculated automatically.
     */
    private LocalDateTime end;

    /**
     * The amount of time it will take the gigs in total.
     * Is being calculated automatically.
     */
    private long length;

    /**
     * The description of the Event, that can be edited by Performers or Venue
     */
    private String description;

    /**
     * This Event is paid or voluntary.
     */
    private Plato.Argument isVoluntary;

    /**
     * If this is a public Event, other Users can view and interact with it.
     */
    private Plato.Argument isPublic;

    /**
     * Will be true, if this Event is cancelled.
     * Can only be cancelled by the Venue.
     */
    private Plato.Argument isCancelled;

    /**
     * This is marked if there is no more tickets to sell.
     */
    private Plato.Argument isSoldOut;

    /**
     * This is the address or place, whether the Event will be held.
     * Will be used to be search at in Google Maps.
     */
    private String location;

    /**
     * The cost of a ticket.
     * Can be multiple prices for tickets, but this is just meant as the cheapest.
     */
    private double price;

    /**
     * A link for where the tickets can be sold.
     * This might be altered later on,
     * since it is wished to be sold through Bandwich.
     * If the field isn't touched, it will use the Venue's location.
     */
    private String ticketsURL;

    /**
     * Different information of contacting.
     */
    private ContactInfoDTO contactInfo;

    /**
     * The gigs with times and acts of the Event.
     */
    private GigDTO[] gigs;

    /**
     * This venue is the ones responsible for the Event,
     * perhaps even the place it is held, but not necessarily.
     */
    private VenueDTO venue;

    /**
     * These requests are needed to make sure, everyone wants to be a part of the Event.
     */
    private RequestDTO[] requests;

    /**
     * The people that will participate in the Event,
     * not including venues or acts.
     */
    private ParticipationDTO[] participations;

    /**
     * Post from different people, that will mention contents.
     */
    private BulletinDTO[] bulletins;

    /**
     * An Album of images, that can be used to promote this Event.
     */
    private AlbumDTO[] albums;

    public EventDTO(long id) {
        super(id);
    }

    public EventDTO(Event event) {
        super(event.get_primaryId(), event.get_title(), event.get_timestamp());
        description = event.get_description();

        if (event.get_gigs() != null) {
            gigs = new GigDTO[event.get_gigs().size()];
            for (int i = 0; i < gigs.length; i++)
                gigs[i] = new GigDTO(event.get_gigs().Get(i+1));
        }

        openDoors = event.get_openDoors();
        start = event.get_start();
        end = event.get_end();
        length = event.get_length();

        isVoluntary = event.get_voluntary() != null ? event.get_voluntary().get_argument() : null;
        isPublic = event.get_public() != null ? event.get_public().get_argument() : null;
        isCancelled = event.get_cancelled() != null ? event.get_cancelled().get_argument() : null;
        isSoldOut = event.get_soldOut() != null ? event.get_soldOut().get_argument() : null;
        price = event.get_price();
        ticketsURL = event.get_ticketsURL();
        contactInfo = event.get_contactInfo() != null ? new ContactInfoDTO(event.get_contactInfo()) : null;
        venue = (VenueDTO) DTOService.get_instance().convertToDTO(event.get_venue());

        location = event.get_location();

        if (event.get_requests() != null) {
            requests = new RequestDTO[event.get_requests().size()];
            for (int i = 0; i < requests.length; i++)
                requests[i] = new RequestDTO(event.get_requests().Get(i+1));
        }
        if (event.get_participations() != null) {
            participations = new ParticipationDTO[event.get_participations().size()];
            for (int i = 0; i < participations.length; i++)
                participations[i] = new ParticipationDTO(event.get_participations().Get(i+1));
        }
        if (event.get_bulletins() != null) {
            bulletins = new BulletinDTO[event.get_bulletins().size()];
            for (int i = 0; i < bulletins.length; i++)
                bulletins[i] = new BulletinDTO(event.get_bulletins().Get(i+1));
        }
        if (event.get_albums() != null) {
            albums = new AlbumDTO[event.get_albums().size()];
            for (int i = 0; i < albums.length; i++)
                albums[i] = new AlbumDTO(event.get_albums().Get(i+1));
        }
    }
}
