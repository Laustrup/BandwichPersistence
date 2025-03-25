package laustrup.bandwichpersistence.core.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public abstract class ParticipationBase {

    /**
     * The type of which participant is participating in the participation.
     */
    @Setter
    protected Type _type;

    protected Instant _timestamp;

    public ParticipationBase(DTO participation) {
        this(
                participation.getType(),
                participation.getTimestamp()
        );
    }

    public ParticipationBase(Type type) {
        this(
                type,
                Instant.now()
        );
    }

    public ParticipationBase(Type type, Instant timestamp) {
        _type = type;
        _timestamp = timestamp;
    }

    /** Each Participation have four different choices of participating. */
    public enum Type {
        /**
         * Ticket is bought if needed and they have it in their calendar.
         */
        JOINING,
        /**
         * Considering joining event.
         */
        INTERESTED,
        /**
         * Does not join event.
         */
        DENIED,
        /**
         * Has been invited to event, has not decided yet.
         */
        INVITED
    }

    @Getter
    public abstract static class DTO {

        protected Type type;

        protected Instant timestamp;

        public DTO(Type type, Instant timestamp) {
            this.type = type;
            this.timestamp = timestamp;
        }

        public DTO(ParticipationBase participation) {
            this(
                    participation.get_type(),
                    participation.get_timestamp()
            );
        }
    }
}
