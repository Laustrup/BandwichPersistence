package laustrup.bandwichpersistence.core.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public abstract class Participation {

    /**
     * The type of which participant is participating in the participation.
     */
    @Setter
    protected Type _type;

    protected Instant _timestamp;

    public Participation(DTO participation) {
        this(
                participation.getType(),
                participation.getTimestamp()
        );
    }

    public Participation(Type type) {
        this(
                type,
                Instant.now()
        );
    }

    public Participation(Type type, Instant timestamp) {
        _type = type;
        _timestamp = timestamp;
    }

    /** Each Participation have four different choices of participating. */
    public enum Type {
        /**
         * Ticket is bought if needed and they have it in their calendar.
         */
        ACCEPTED,
        INTERESTED,
        CANCELED,
        INVITED
    }

    @Getter
    public static class DTO {

        protected Type type;

        protected Instant timestamp;

        public DTO(Participation participation) {
            type = participation.get_type();
            timestamp = participation.get_timestamp();
        }
    }
}
