package laustrup.bandwichpersistence.models.dtos;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.services.DTOService;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Can be added to a model to indicate the rating that the model is appreciated.
 * Is created by a user.
 */
@NoArgsConstructor @Data
public class RatingDTO extends ModelDTO {

    /**
     * The id of the user, that has received this rating.
     * Is used for when inserting the rating to the user.
     */
    private UserDTO appointed;

    /**
     * The id of the user, that has given this rating.
     * Is used for when inserting the rating to the user.
     */
    private UserDTO judge;

    /**
     * The value of the rating that is appointed.
     * Must be between 0 and 5.
     */
    private int value;

    /**
     * Is not meant to be necessary, but can be added by the judge.
     */
    private String _comment;

    public RatingDTO(Rating rating) {
        super(rating.get_appointed().get_primaryId(), rating.get_judge().get_primaryId(),
                rating.get_appointed().get_username()+"-"+rating.get_judge().get_username(), rating.get_timestamp());
        value = rating.get_value();
        appointed = DTOService.get_instance().convertToDTO(rating.get_appointed());
        judge = DTOService.get_instance().convertToDTO(rating.get_judge());
    }
}
