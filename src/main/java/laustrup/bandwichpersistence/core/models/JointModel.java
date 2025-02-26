package laustrup.bandwichpersistence.core.models;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class JointModel extends Model {

    /**
     * Another identification value in the database for a specific entity.
     * Must be unique with primary id.
     * Is used for incidents, where there are a connection between two entities,
     * and they are both being used as primary keys
     */
    protected UUID _secondaryId;

    public JointModel(JoinedModelDTO model) {
        super(model);
        _secondaryId = model.getSecondaryId();
    }

    /**
     * Will generate a timestamp of the moment now in datetime.
     * @param primaryId A hex decimal value identifying this item uniquely.
     * @param secondaryId Another hex decimal value identifying another item uniquely.
     * @param title A title describing this entity internally.
     */
    public JointModel(UUID primaryId, UUID secondaryId, String title) {
        _id = primaryId;
        _secondaryId = secondaryId;
        _title = title;
        _timestamp = Instant.now();
    }

    /**
     * @param primaryId A hex decimal value identifying this item uniquely.
     * @param secondaryId Another hex decimal value identifying another item uniquely.
     * @param history A collection of events that has occurred.
     * @param title A title describing this entity internally.
     * @param timestamp Specifies the time this entity was created.
     */
    public JointModel(
            UUID primaryId,
            UUID secondaryId,
            String title,
            History history,
            Instant timestamp
    ) {
        _id = primaryId;
        _secondaryId = secondaryId;
        _title = title;
        _history = history;
        _timestamp = timestamp;
    }

    @Getter
    public static abstract class JoinedModelDTO extends ModelDTO {

        /**
         * Another identification value in the database for a specific entity.
         * Must be unique with primary id.
         * Is used for incidents, where there are a connection between two entities,
         * and they are both being used as primary keys
         */
        protected UUID secondaryId;


        public JoinedModelDTO(JointModel model) {
            super(model);
            secondaryId = model.get_secondaryId();
        }
    }
}
