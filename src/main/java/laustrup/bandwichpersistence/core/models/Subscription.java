package laustrup.bandwichpersistence.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

/**
 * Defines the kind of subscription a user is having.
 * Only Artists and Bands can have a paying subscription.
 */
@Getter @FieldNameConstants
public class Subscription extends Model {

    /**
     * An enum that determines what kind of status, the situation of the Subscription is in.
     */
    @Setter
    private Status _status;

    private Kind _kind;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param subscription The transport object to be transformed.
     */
    public Subscription(DTO subscription) {
        super(subscription);
        _status = Status.valueOf(subscription.getStatus().toString());
        _kind = subscription.getKind();
    }

    /**
     * A constructor with all fields.
     * @param id The id the defines this specific Subscription, is the same as the User of this Subscription.
     * @param status An enum that determines what kind of status, the situation of the Subscription is in.
     * @param timestamp The time this object was created.
     */
    public Subscription(
            UUID id,
            Status status,
            Kind kind,
            Instant timestamp
    ) {
        _id = id;
        _title = "Subscription: " + id;
        _status = status;
        _kind = kind;
        _timestamp = timestamp;
    }

    /**
     * For creating a new Subscription.
     * Timestamp will be now.
     * @param status An enum that determines what kind of status, the situation of the Subscription is in.
     */
    public Subscription(Status status, Kind kind) {
        _title = "New-Subscription";
        _status = status;
        _kind = kind;
        _timestamp = Instant.now();
    }

    @Override
    public String toString() {
        return defineToString(
                getClass().getSimpleName(),
                new String[] {
                        Model.Fields._id,
                        Fields._status,
                        Model.Fields._timestamp
                },
                new String[] {
                        String.valueOf(get_id()),
                        get_status() != null ? get_status().name() : null,
                        String.valueOf(get_timestamp())
                }
        );
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

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends ModelDTO {

        /**
         * An enum that determines what kind of status, the situation of the Subscription is in.
         */
        private Subscription.Status status;

        private Subscription.Kind kind;

        public DTO(Subscription subscription) {
            super(subscription);
            status = Subscription.Status.valueOf(subscription.get_status().toString());
            kind = subscription.get_kind();
        }
    }
}