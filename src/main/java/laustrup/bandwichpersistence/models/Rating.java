package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.models.users.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.InputMismatchException;

/**
 * Can be added to a model to indicate the rating that the model is appreciated.
 * Is created by a user.
 */
@ToString
public class Rating {

    /**
     * The identification value in the database for a specific rating.
     * Must be unique.
     */
    @Getter
    private long _ratingId;

    /**
     * The id of the user, that has received this rating.
     * Is used for when inserting the rating to the user.
     */
    @Getter
    private User _appointed;

    /**
     * The id of the user, that has given this rating.
     * Is used for when inserting the rating to the user.
     */
    @Getter
    private User _judge;

    /**
     * The value of the rating that is appointed.
     * Must be between 0 and 5.
     */
    @Getter
    private int _value;

    /**
     * Is not meant to be necessary, but can be added by the judge.
     */
    @Getter @Setter
    private String _comment;

    public Rating(long ratingId, User appointed, User judge, int value) {
        _ratingId = ratingId;
        _appointed = appointed;
        _judge = judge;
        _value = value;
    }

    public Rating(int value, User appointed, User judge) throws InputMismatchException {
        if (0 < value && value <= 5 ) {
            _value = value;
            _appointed = appointed;
            _judge = judge;
        }
        else
            throw new InputMismatchException();
    }

    /**
     * Sets the value of this Rating.
     * @param value The specific value, that is wished to be set as the value of the Rating.
     * @return The new value of the current Rating.
     * @throws InputMismatchException Will be thrown if the value is not between 0 and 5.
     */
    public int set_value(int value) throws InputMismatchException {
        if (0 < value && value <= 5 ) {
            _value = value;
        }
        else
            throw new InputMismatchException();

        return _value;
    }
}
