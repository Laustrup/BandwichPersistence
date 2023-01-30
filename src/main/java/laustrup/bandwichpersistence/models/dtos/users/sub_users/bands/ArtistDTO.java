package laustrup.bandwichpersistence.models.dtos.users.sub_users.bands;

import laustrup.bandwichpersistence.models.dtos.chats.RequestDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.PerformerDTO;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An Artist can either be a solo Performer or member of a Band, which changes the Subscription, if it ain't freemium.
 * Extends from Performer.
 */
@NoArgsConstructor @Data
public class ArtistDTO extends PerformerDTO {

    /**
     * The Bands that the Artist is a member of.
     */
    private BandDTO[] bands;

    /**
     * A description of the gear, that the Artist possesses and what they require for an Event.
     */
    private String runner;

    /**
     * The Requests requested for this Artist.
     */
    private RequestDTO[] requests;

    public ArtistDTO(Artist artist) {
        super(artist.get_primaryId(), artist.get_username(), artist.get_firstName(), artist.get_lastName(),
                artist.get_description(), artist.get_contactInfo(), Authority.ARTIST, artist.get_albums(),
                artist.get_ratings(),artist.get_events(), artist.get_gigs(), artist.get_chatRooms(),
                artist.get_subscription(), artist.get_bulletins(), artist.get_fans(), artist.get_idols(),
                artist.get_timestamp());
        bands = new BandDTO[artist.get_bands().size()];
        for (int i = 0; i < bands.length; i++)
            bands[i] = new BandDTO(artist.get_bands().get(i+1));
        runner = artist.get_runner();
        requests = new RequestDTO[artist.get_requests().size()];
        for (int i = 0; i < requests.length; i++)
            requests[i] = new RequestDTO(artist.get_requests().get(i+1));
    }

    public ArtistDTO(User user) {
        super(user.get_primaryId(), user.get_username(), user.get_firstName(), user.get_lastName(),
                user.get_description(), user.get_contactInfo(), Authority.ARTIST, user.get_albums(),
                user.get_ratings(),user.get_events(), ((Artist) user).get_gigs(), user.get_chatRooms(),
                user.get_subscription(), user.get_bulletins(), ((Artist) user).get_fans(), ((Artist) user).get_idols(),
                user.get_timestamp());

        if (user.get_authority() == User.Authority.ARTIST) {
            bands = new BandDTO[((Artist) user).get_bands().size()];
            for (int i = 0; i < bands.length; i++)
                bands[i] = new BandDTO(((Artist) user).get_bands().get(i+1));
            runner = ((Artist) user).get_runner();
            requests = new RequestDTO[((Artist) user).get_requests().size()];
            for (int i = 0; i < requests.length; i++)
                requests[i] = new RequestDTO(((Artist) user).get_requests().get(i+1));
        }
    }
}
