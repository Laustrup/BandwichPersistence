package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;

/**
 * Defines the kind of subscription a user is having.
 * Only Artists and Bands can have a paying subscription.
 *
 * @param status An enum that determines what kind of status, the situation of the Subscription is in.
 */
@FieldNameConstants
@Table
public record Subscription(Id id, Status status, Kind kind, UserType userType) {

  /**
   * Will translate a transport object of this object into a construct of this object.
   *
   * @param subscription The transport object to be transformed.
   */
  public Subscription(DTO subscription) {
    this(
        new Id(subscription.getId()),
        subscription.getStatus(),
        subscription.getKind(),
        subscription.getUserType()
    );
  }

  /**
   * A constructor with all fields.
   *
   * @param id     The id the defines this specific Subscription, is the same as the User of this Subscription.
   * @param status An enum that determines what kind of status, the situation of the Subscription is in.
   */
  @Table.Constructor
  public Subscription {
  }

  /**
   * For creating a new Subscription.
   * Timestamp will be now.
   *
   * @param status An enum that determines what kind of status, the situation of the Subscription is in.
   */
  public Subscription(Status status, Kind kind, UserType userType) {
    this(
        null,
        status,
        kind,
        userType
    );
  }

  public static class Id extends CommonIdentity<Signature.UUID> {

    public Id(Signature.UUID signature) {
      super(signature);
    }

    public Id(UUID signature) {
      super(new Signature.UUID(signature));
    }

    @Override
    public Class<Subscription> getOwnerClassType() {
      return Subscription.class;
    }
  }

  @Override
  public String toString() {
    return toStringify(this);
  }

  /**
   * An enum that defines Status that a Subscription is currently in.
   */
  public enum Status {
    ACCEPTED,
    BLOCKED,
    DEACTIVATED,
    SUSPENDED,
    PENDING,
    CLOSED
  }

  public enum Kind {
    PAYING,
    FREE
  }

  public enum UserType {
    ARTIST,
    ORGANISATION_EMPLOYEE,
    PARTICIPANT
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

    private UUID id;

    /**
     * An enum that determines what kind of status, the situation of the Subscription is in.
     */
    private Status status;

    private Kind kind;

    private UserType userType;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DTO(
        @JsonProperty UUID id,
        @JsonProperty Status status,
        @JsonProperty Kind kind,
        @JsonProperty UserType userType
    ) {
      this.id = id;
      this.status = status;
      this.kind = kind;
      this.userType = userType;
    }

    public DTO(Subscription subscription) {
      this(
          subscription.id().get_value(),
          Status.valueOf(subscription.status().toString()),
          subscription.kind(),
          subscription.userType()
      );
    }
  }
}