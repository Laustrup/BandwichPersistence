package laustrup.bandwichpersistence.models.dtos.users.sub_users.bands;

import laustrup.bandwichpersistence.models.dtos.users.sub_users.PerformerDTO;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Extends performer and contains Artists as members
 */
@NoArgsConstructor @Data
public class BandDTO extends PerformerDTO {

    /**
     * Contains all the Artists, that are members of this band.
     */
    private ArtistDTO[] members;

    /**
     * A description of the gear, that the band possesses and what they require for an Event.
     */
    private String runner;

    public BandDTO(Band band) {
        super(band.get_primaryId(), band.get_username(), band.get_description(), band.get_contactInfo(), Authority.BAND,
                band.get_albums(), band.get_ratings(), band.get_events(), band.get_gigs(), band.get_chatRooms(),
                band.get_subscription(), band.get_bulletins(), band.get_fans(), band.get_idols(), band.get_timestamp());
        members = new ArtistDTO[band.get_members().size()];
        for (int i = 0; i < members.length; i++)
            members[i] = new ArtistDTO(band.get_members().Get(i+1));

        runner = band.get_runner();
    }

    public BandDTO(User user) {
        super(user.get_primaryId(), user.get_username(), user.get_description(), user.get_contactInfo(), Authority.BAND,
                user.get_albums(), user.get_ratings(), user.get_events(), ((Band)user).get_gigs(), user.get_chatRooms(),
                user.get_subscription(), user.get_bulletins(), ((Band) user).get_fans(), ((Band) user).get_idols(),
                user.get_timestamp());
        if (user.get_authority() == User.Authority.BAND) {
            members = new ArtistDTO[((Band)user).get_members().size()];
            for (int i = 0; i < members.length; i++)
                members[i] = new ArtistDTO(((Band)user).get_members().Get(i+1));

            runner = ((Band)user).get_runner();
        }
    }
}
