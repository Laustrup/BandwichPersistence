package laustrup.bandwichpersistence.models.dtos.users.sub_users.venues;

import laustrup.bandwichpersistence.models.dtos.chats.RequestDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.dtos.users.contact_infos.ContactInfoDTO;
import laustrup.bandwichpersistence.models.dtos.users.subscriptions.SubscriptionDTO;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@NoArgsConstructor @Data
public class VenueDTO extends UserDTO {

    /**
     * The location that the Venue is located at, which could be an address or simple a place.
     */
    private String location;

    /**
     * The description of the gear that the Venue posses.
     */
    private String gearDescription;

    /**
     * The size of the stage and room, that Events can be held at.
     */
    private int size;

    /**
     * The Requests requested for this Venue.
     */
    private RequestDTO[] requests;

    public VenueDTO(Venue venue) {
        super(venue.get_primaryId(), venue.get_username(), venue.get_description(),
                new ContactInfoDTO(venue.get_contactInfo()), venue.get_albums(), venue.get_ratings(),
                venue.get_events(), venue.get_chatRooms(), new SubscriptionDTO(venue.get_subscription()),
                venue.get_bulletins(), Authority.VENUE, venue.get_timestamp());

        location = venue.get_location();

        gearDescription = venue.get_gearDescription();
        size = venue.get_size();
        requests = new RequestDTO[venue.get_requests().size()];
        for (int i = 0; i < requests.length; i++)
            requests[i] = new RequestDTO(venue.get_requests().get(i+1));
    }

    public VenueDTO(User user) {
        super(user.get_primaryId(), user.get_username(), user.get_description(),
                new ContactInfoDTO(user.get_contactInfo()), user.get_albums(), user.get_ratings(),
                user.get_events(), user.get_chatRooms(), new SubscriptionDTO(user.get_subscription()),
                user.get_bulletins(), Authority.VENUE, user.get_timestamp());

        if (user.get_authority() == User.Authority.VENUE) {
            location = ((Venue) user).get_location();

            gearDescription = ((Venue) user).get_gearDescription();
            size = ((Venue) user).get_size();
            requests = new RequestDTO[((Venue) user).get_requests().size()];
            for (int i = 0; i < requests.length; i++)
                requests[i] = new RequestDTO(((Venue) user).get_requests().get(i+1));
        }
    }
}
