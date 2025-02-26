package laustrup.bandwichpersistence.core.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class Participation extends JointModel {

    /**
     * The type of which participant is participating in the participation.
     */
    @Setter
    private Type _type;

    public Participation(DTO participation) {
        super(participation);
        _type = participation.getType();
    }

    public Participation(UUID primaryId, UUID secondaryId, String title, Type type) {
        super(primaryId, secondaryId, title);
        _type = type;
    }

    public Participation(UUID primaryId, UUID secondaryId, String title, Type type, History history, Instant timestamp) {
        super(primaryId, secondaryId, title, history, timestamp);
        _type = type;
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
    public static class DTO extends JoinedModelDTO {

        private Type type;

        public DTO(Participation participation) {
            super(participation);
            type = participation.get_type();
        }
    }
}
