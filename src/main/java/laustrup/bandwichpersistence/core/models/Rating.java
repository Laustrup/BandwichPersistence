package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.InputMismatchException;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;

/**
 * Can be added to a model to indicate the rating that the model is appreciated.
 * The first id is the appointed and the second is the judge.
 * Is created by a user.
 */
@Getter
@FieldNameConstants
@Table.Target
public class Rating {

  @Table.Column(isPrimary = true)
  private final User.Id _appointedId, _reviewerId;

  /**
   * The value of the rating that is appointed.
   * Must be between 1 and 5.
   */
  private int _value;

  /**
   * Is not meant to be necessary, but can be added by the judge.
   */
  private final String _comment;

  private final Instant _timestamp;

  /**
   * Will translate a transport object of this object into a construct of this object.
   *
   * @param rating The transport object to be transformed.
   */
  public Rating(DTO rating) throws InputMismatchException {
    this(
        rating.getValue(),
        new User.Id(rating.getAppointedId()),
        new User.Id(rating.getReviewerId()),
        rating.getComment(),
        rating.getTimestamp()
    );
  }

  /**
   * Constructor with all values.
   *
   * @param value       The value of the Rating given.
   * @param appointedId The one receiving the Rating.
   * @param reviewerId  The one giving the Rating.
   * @param comment     A comment that is attached to the Rating.
   * @param timestamp   Specifies the time the Rating was created.
   */
  @Table.Constructor
  public Rating(
      int value,
      User.Id appointedId,
      User.Id reviewerId,
      String comment,
      Instant timestamp
  ) {
    _value = set_value(value);
    _appointedId = appointedId;
    _reviewerId = reviewerId;
    _comment = comment;
    _timestamp = timestamp;
  }

  /**
   * For generating a new Rating.
   * Timestamp is of now.
   *
   * @param value       The value of the Rating given.
   * @param appointedId The one receiving the Rating.
   * @param judgeId     The one giving the Rating.
   * @param comment     A comment that is attached to the Rating.
   */
  public Rating(int value, User.Id appointedId, User.Id judgeId, String comment) {
    this(value, appointedId, judgeId, comment, Instant.now());
  }

  /**
   * Sets the value of this Rating.
   *
   * @param value The specific value, that is wished to be set as the value of the Rating.
   * @return The new value of the current Rating.
   * @throws InputMismatchException Will be thrown if the value is not between 1 and 5.
   */
  public int set_value(int value) throws InputMismatchException {
    if (0 < value && value <= 5)
      _value = value;
    else
      throw new InputMismatchException();

    return _value;
  }

  @Override
  public String toString() {
    return toStringify(this);
  }

  /**
   * The Data Transfer Object.
   * Is meant to be used as having common fields and be the body of Requests and Responses.
   * Doesn't have any logic.
   */
  @Getter
  @Setter
  @FieldNameConstants
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DTO {

    private UUID appointedId, reviewerId;

    /**
     * The value of the rating that is appointed.
     * Must be between 0 and 5.
     */
    private int value;

    /**
     * Is not meant to be necessary, but can be added by the judge.
     */
    private String comment;

    private Instant timestamp;

    public DTO(Rating rating) {
      appointedId = rating.get_appointedId().get_value();
      reviewerId = rating.get_reviewerId().get_value();
      value = rating.get_value();
      comment = rating.get_comment();
      timestamp = rating.get_timestamp();
    }
  }
}
