package laustrup.bandwichpersistence.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The base of many objects, that share these same attributes.
 * When it is created through a constructor, that doesn't ask for a DateTime.
 * It will use the DateTime of now.
 */
@NoArgsConstructor
public abstract class Model {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique.
     */
    @Getter
    protected long _id;

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

    public Model(String title) {
        _title = title;
        _timestamp = LocalDateTime.now();
    }

    public Model(long id, String title, LocalDateTime timestamp) {
        _id = id;
        _title = title;
        _timestamp = timestamp;
    }

}
