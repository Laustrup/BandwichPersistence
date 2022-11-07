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
    private long _ratingId, _userId;
    @Getter @Setter
    private double _value;

    public Rating(long ratingId, long userId, double value) {
        _ratingId = ratingId;
        _userId = userId;
        _value = value;
    }

    public Rating(double value, long userId) {
        _value = value;
        _userId = userId;
    }
}
