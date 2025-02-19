package laustrup.bandwichpersistence.core.models;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class JoinedModel extends Model {

    /**
     * Another identification value in the database for a specific entity.
     * Must be unique with primary id.
     * Is used for incidents, where there are a connection between two entities,
     * and they are both being used as primary keys
     */
    protected UUID _secondaryId;

    public JoinedModel(JoinedModelDTO model) {
        super(model);
        _secondaryId = model.getSecondaryId();
    }

    /**
     * Will generate a timestamp of the moment now in datetime.
     * @param primaryId A hex decimal value identifying this item uniquely.
     * @param secondaryId Another hex decimal value identifying another item uniquely.
     * @param title A title describing this entity internally.
     */
    public JoinedModel(UUID primaryId, UUID secondaryId, String title) {
        _primaryId = primaryId;
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
    public JoinedModel(
            UUID primaryId,
            UUID secondaryId,
            String title,
            History history,
            Instant timestamp
    ) {
        _primaryId = primaryId;
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


        public JoinedModelDTO(JoinedModel model) {
            super(model);
            secondaryId = model.get_secondaryId();
        }
    }
}
