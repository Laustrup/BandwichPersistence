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
     * Is used for incidents, where there are a connection between two entities
     * and they are both being used as primary keys
     */
    protected Long _secondaryId;

    /**
     * Gets the secondary id, if there isn't any, it will get the primary id.
     * @return The gathered id.
     */
    public Long get_secondaryId() {
        return _secondaryId == null || _secondaryId < 1 ? get_primaryId() : _secondaryId;
    }

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

    /**
     * A status that determines if the Model is being assembled or not.
     * Some methods require this boolean to be true.
     */
    @Getter
    protected boolean _assembling;

    /**
     * A kind of String message, that can be used to define an incident a message.
     */
    @Getter @Setter
    protected String _situation = "UNDEFINED";

    public Model() {
        _timestamp = LocalDateTime.now();
    }
    public Model(long id) {
        _primaryId = id;
        _timestamp = LocalDateTime.now();
    }

    public Model(String title) {
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    public Model(long id, String title, LocalDateTime timestamp) {
        _primaryId = id;
        _title = title;
        _timestamp = timestamp;
    }
    public Model(long primaryId, long secondaryId, String title) {
        _primaryId = primaryId;
        _secondaryId = secondaryId;
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    public Model(long primaryId, long secondaryId, String title, LocalDateTime timestamp) {
        _primaryId = primaryId;
        _secondaryId = secondaryId;
        _title = title;
        _timestamp = timestamp;
    }

    public Model(String title, LocalDateTime timestamp) {
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * Checks if secondary id is null.
     * @return True if secondary id isn't null.
     */
    public boolean hasSecondaryId() { return _secondaryId != null; }

    /**
     * Will determine that the status of is being assembling is over by making assembling false.
     * @return The assembling status.
     */
    public boolean doneAssembling() {
        _assembling = false;
        return _assembling;
    }
}
