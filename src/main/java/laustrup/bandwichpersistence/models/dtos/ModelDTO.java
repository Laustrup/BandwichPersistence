package laustrup.bandwichpersistence.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * The base of many objects, that share these same attributes.
 * When it is created through a constructor, that doesn't ask for a DateTime.
 * It will use the DateTime of now.
 */
@Data
public abstract class ModelDTO {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique, if there ain't other ids for this entity.
     */
    protected long primaryId;

    /**
     * Another identification value in the database for a specific entity.
     * Must be unique with primary id.
     * Is used for incidents, where there are an connection between two entities
     * and they are both being used as primary keys
     */
    protected Long secondaryId;

    /**
     * The name for an enitity or model.
     * Can be of different purposes,
     * such as username or simply for naming a unit.
     */
    protected String title;

    /**
     * Specifies the time this entity was created.
     */
    protected LocalDateTime timestamp;

    public ModelDTO() {
        timestamp = LocalDateTime.now();
    }
    public ModelDTO(long id) {
        primaryId = id;
        timestamp = LocalDateTime.now();
    }

    public ModelDTO(String title) {
        this.title = title;
        timestamp = LocalDateTime.now();
    }

    public ModelDTO(long id, String title, LocalDateTime timestamp) {
        primaryId = id;
        this.title = title;
        this.timestamp = timestamp;
    }
    public ModelDTO(long primaryId, long secondaryId, String title) {
        this.primaryId = primaryId;
        this.secondaryId = secondaryId;
        this.title = title;
        timestamp = LocalDateTime.now();
    }

    public ModelDTO(long primaryId, long secondaryId, String title, LocalDateTime timestamp) {
        this.primaryId = primaryId;
        this.secondaryId = secondaryId;
        this.title = title;
        this.timestamp = timestamp;
    }

    public ModelDTO(String title, LocalDateTime timestamp) {
        this.title = title;
        this.timestamp = timestamp;
    }
}
