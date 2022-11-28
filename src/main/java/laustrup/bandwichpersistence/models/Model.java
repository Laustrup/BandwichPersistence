package laustrup.bandwichpersistence.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The base of many objects, that share these same attributes.
 * When it is created through a constructor, that doesn't ask for a DateTime.
 * It will use the DateTime of now.
 */
public abstract class Model {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique, if there ain't other ids for this entity.
     */
    @Getter
    protected long _primaryId;

    /**
     * Another identification value in the database for a specific entity.
     * Must be unique with primary id.
     * Is used for incidents, where there are an connection between two entities
     * and they are both being used as primary keys
     */
    @Getter
    protected Long _secondaryId;

    /**
     * The name for an enitity or model.
     * Can be of different purposes,
     * such as username or simply for naming a unit.
     */
    @Getter @Setter
    protected String _title;

    /**
     * Specifies the time this entity was created.
     */
    @Getter
    protected LocalDateTime _timestamp;

    public Model() { _timestamp = LocalDateTime.now(); }

    public Model(String title) {
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    public Model(long id, String title, LocalDateTime timestamp) {
        _primaryId = id;
        _title = title;
        _timestamp = timestamp;
    }
    public Model(long id1, long id2, String title) {
        _primaryId = id1;
        _secondaryId = id2;
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    public Model(long id1, long id2, String title, LocalDateTime timestamp) {
        _primaryId = id1;
        _secondaryId = id2;
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * Checks if secondary id is null.
     * @return True if secondary id isn't null.
     */
    public boolean hasSecondaryId() { return _secondaryId != null; }
}
