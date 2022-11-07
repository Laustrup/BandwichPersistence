package laustrup.bandwichpersistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Can be added to a model to indicate the rating that the model is appreciated.
 * Is created by a user.
 */
@ToString
public class Rating {

    @Getter
    private String _ratingId, _userId;
    @Getter @Setter
    private double _value;

    // Constructor for database
    public Rating(String ratingId, String userId, double value) {
        _ratingId = ratingId;
        _userId = userId;
        _value = value;
    }

    // Constructor for creating
    public Rating(double value, String userId) {
        _value = value;
        _userId = userId;
    }
}
