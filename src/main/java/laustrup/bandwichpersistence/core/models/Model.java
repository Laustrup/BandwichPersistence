package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.utilities.console.Printer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

/**
 * The base of many objects, that share these same attributes.
 * When it is created through a constructor, that doesn't ask for a DateTime.
 * It will use the DateTime of now.
 */
@FieldNameConstants
@ToString(of = {"_primaryId", "_title", "_timestamp"})
public abstract class Model {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique, if there ain't other ids for this entity.
     * UUIDs are unique hex decimal values of the specific entity.
     */
    @Getter
    protected UUID _primaryId;

    /**
     * The name for an entity or model.
     * Can be of different purposes,
     * such as username or simply for naming a unit.
     */
    @Getter @Setter
    protected String _title;

    /**
     * Specifies the time this entity was created.
     */
    @Getter
    protected Instant _timestamp;

    /**
     * Simply said, this is used to describe a relevant situation.
     * Useful to identify an incident or change.
     * Is added to the Response entity class when answering.
     */
    @Getter
    protected Situation _situation = Situation.NONE;

    /**
     * Contains stories that consists of events or logs.
     */
    @Getter
    protected History _history;

    /**
     * Sets the Situation.
     * The situation mustn't become none after it has had a situation,
     * then it must be resolved, which this method insures to prevent misinformation.
     * @param situation The new Situation.
     * @return The current Situation.
     */
    public Situation set_situation(Situation situation) {
        _situation = situation == Situation.NONE && _situation != Situation.NONE
            ? Situation.RESOLVED
            : situation;

        return _situation;
    }

    /** For the defineToString of how it should be split. */
    @FieldNameConstants.Exclude
    private final String _toStringFieldSplitter = ",\n \t",
            _toStringKeyValueSplitter = ":\t";

    /**
     * Converts the data transport object into this model.
     * @param model The data transport model to be converted.
     */
    public Model(ModelDTO model) {
        _primaryId = model.getPrimaryId();
        _title = model.getClass().getSimpleName() + " \"" + model.getPrimaryId() + "\"";
        _situation = model.getSituation();
        _history = model.getHistory();
        _timestamp = model.getTimestamp();
    }

    /** Will generate a timestamp of the moment now in datetime. */
    public Model() {
        _timestamp = Instant.now();
    }

    /**
     * Will generate a timestamp of the moment now in datetime.
     * @param title A title describing this entity internally.
     */
    public Model(String title) {
        _title = title;
        _timestamp = Instant.now();
    }

    /**
     * @param title A title describing this entity internally.
     * @param history A collection of events that has occurred.
     * @param timestamp Specifies the time this entity was created.
     */
    public Model(String title, History history, Instant timestamp) {
        _title = title;
        _history = history;
        _timestamp = timestamp;
    }

    /**
     * @param id A hex decimal value identifying this item uniquely.
     * @param title A title describing this entity internally.
     * @param history A collection of events that has occurred.
     * @param timestamp Specifies the time this entity was created.
     */
    public Model(UUID id, String title, History history, Instant timestamp) {
        _primaryId = id;
        _title = title;
        _history = history;
        _timestamp = timestamp;
    }

    /**
     * @param id A hex decimal value identifying this item uniquely.
     * @param title A title describing this entity internally.
     */
    public Model(UUID id, String title) {
        _primaryId = id;
        _title = title;
        _timestamp = Instant.now();
    }

    /**
     * Will generate a toString from the attributes and values.
     * Makes it able to have the same structure for all objects.
     * If there is more value inputs than keys, the toString will not be unique, even though it must.
     * @param title The class name of the class Model, always use getClass().getSimpleName().
     * @param values First array is a String array of keys and the other are its values.
     * @return The generated toString.
     */
    protected String defineToString(String title, String[][] values) {
        return defineToString(title, values[0], values[1]);
    }

    /**
     * Will generate a toString from the attributes and values.
     * Makes it able to have the same structure for all objects.
     * If there is more value inputs than keys, the toString will not be unique, even though it must.
     * @param title The class name of the class Model, always use getClass().getSimpleName().
     * @param keys The attributes of the class Model, visualized as keys.
     * @param values The values of the attributes/keys, as Strings.
     * @return The generated toString.
     */
    protected String defineToString(String title, String[] keys, String[] values) {
        StringBuilder content = new StringBuilder();

        try {
            if (values.length <= keys.length)
                for (int i = 0; i < keys.length; i++) {
                    content.append(keys[i]).append(_toStringKeyValueSplitter).append(values[i] != null ? values[i] : "null");
                    if (i < keys.length-1)
                        content.append(_toStringFieldSplitter);
                }
            else
                content = new StringBuilder("Content couldn't be generated, since there are less attributes than values");
        } catch (Exception e) {
            String message = title + " had an error when trying to define its ToString.";
            Printer.print(message, e);
            content = new StringBuilder(get_primaryId() != null ? String.valueOf(get_primaryId()) : message);
        }

        return title + "(\n \t" + content + "\n)";
    }

    /**
     * The base of many objects, that share these same attributes.
     * When it is created through a constructor, that doesn't ask for a DateTime.
     * It will use the DateTime of now.
     */
    @Getter
    public abstract static class ModelDTO {

        /**
         * The identification value in the database for a specific entity.
         * Must be unique, if there ain't other ids for this entity.
         * UUIDs are unique hex decimal values of the specific entity.
         */
        protected UUID primaryId;

        /**
         * Another identification value in the database for a specific entity.
         * Must be unique with primary id.
         * Is used for incidents, where there are an connection between two entities,
         * and they are both being used as primary keys
         */
        protected UUID secondaryId;

        /**
         * The name for an entity or model.
         * Can be of different purposes,
         * such as username or simply for naming a unit.
         */
        @Setter
        protected String title;

        /**
         * A collection of events that has occurred.
         */
        protected History history;

        /** Specifies the time this entity was created. */
        protected Instant timestamp;

        /**
         * Simply said, this is used to describe a relevant situation.
         * Useful to identify an incident or change.
         * Is added to the Response entity class when answering.
         */
        @Setter
        protected Situation situation;

        public ModelDTO(Model model) {
            primaryId = model.get_primaryId();
            title = model.get_name();
            history = model.get_history();
            timestamp = model.get_timestamp();
            situation = model.get_situation();
        }
    }
}
