package laustrup.bandwichpersistence.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.InputMismatchException;
import java.util.UUID;

/**
 * Can be added to a model to indicate the rating that the model is appreciated.
 * The first id is the appointed and the second is the judge.
 * Is created by a user.
 */
@Getter
@FieldNameConstants
public class Rating extends JoinedModel {

    /**
     * The value of the rating that is appointed.
     * Must be between 0 and 5.
     */
    private int _value;

    /**
     * Is not meant to be necessary, but can be added by the judge.
     */
    @Setter
    private String _comment;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param rating The transport object to be transformed.
     */
    public Rating(DTO rating) throws InputMismatchException {
        super(rating);
        _value = set_value(rating.getValue());
    }

    /**
     * Constructor with all values.
     * @param value The value of the Rating given.
     * @param appointedId The one receiving the Rating.
     * @param judgeId The one giving the Rating.
     * @param comment A comment that is attached to the Rating.
     * @param history The Events for this object.
     * @param timestamp Specifies the time the Rating was created.
     */
    public Rating(
            int value,
            UUID appointedId,
            UUID judgeId,
            String comment,
            History history,
            Instant timestamp
    ) {
        super(appointedId, judgeId, appointedId + "-" + judgeId, history, timestamp);
        _comment = comment;
        _value = set_value(value);
    }

    /**
     * For generating a new Rating.
     * Timestamp is of now.
     * @param value The value of the Rating given.
     * @param appointedId The one receiving the Rating.
     * @param judgeId The one giving the Rating.
     * @param comment A comment that is attached to the Rating.
     */
    public Rating(int value, UUID appointedId, UUID judgeId, String comment) {
        this(value, appointedId, judgeId, comment, null,  Instant.now());
    }

    /**
     * Sets the value of this Rating.
     * @param value The specific value, that is wished to be set as the value of the Rating.
     * @return The new value of the current Rating.
     * @throws InputMismatchException Will be thrown if the value is not between 0 and 5.
     */
    public int set_value(int value) throws InputMismatchException {
        if (0 < value && value <= 5 )
            _value = value;
        else
            throw new InputMismatchException();

        return _value;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Fields._value,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_value()),
                String.valueOf(get_timestamp())
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends JoinedModelDTO {

        /**
         * The value of the rating that is appointed.
         * Must be between 0 and 5.
         */
        private int value;

        /**
         * Is not meant to be necessary, but can be added by the judge.
         */
        private String comment;

        public DTO(Rating rating) {
            super(rating);
            value = rating.get_value();
            comment = rating.get_comment();
        }
    }
}
