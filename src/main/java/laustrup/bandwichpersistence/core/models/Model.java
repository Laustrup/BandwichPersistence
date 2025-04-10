package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.services.ModelService;
import laustrup.bandwichpersistence.core.utilities.Coollection;
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
@Getter
@FieldNameConstants
@ToString(of = {"_id", "_title", "_timestamp"})
public abstract class Model {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique, if there ain't other ids for this entity.
     * UUIDs are unique hex decimal values of the specific entity.
     */
    protected UUID _id;

    /**
     * The name for an entity or model.
     * Can be of different purposes,
     * such as username or simply for naming a unit.
     */
    @Setter
    protected String _title;

    /**
     * Specifies the time this entity was created.
     */
    protected Instant _timestamp;

    /**
     * Simply said, this is used to describe a relevant situation.
     * Useful to identify an incident or change.
     * Is added to the Response entity class when answering.
     */
    protected Situation _situation = Situation.NONE;

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

    /**
     * Converts the data transport object into this model.
     * @param model The data transport model to be converted.
     */
    public Model(ModelDTO model) {
        _id = model.getId();
        _title = model.getClass().getSimpleName() + " \"" + model.getId() + "\"";
        _situation = model.getSituation();
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
     * @param timestamp Specifies the time this entity was created.
     */
    public Model(String title, Instant timestamp) {
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * @param id A hex decimal value identifying this item uniquely.
     * @param title A title describing this entity internally.
     * @param timestamp Specifies the time this entity was created.
     */
    public Model(UUID id, String title, Instant timestamp) {
        _id = id;
        _title = title;
        _timestamp = timestamp;
    }

    /**
     * @param id A hex decimal value identifying this item uniquely.
     * @param title A title describing this entity internally.
     */
    public Model(UUID id, String title) {
        _id = id;
        _title = title;
        _timestamp = Instant.now();
    }

    protected String defineToString(String title, Coollection<ToStringArgument> arguments) {
        return defineToString(title, ToStringArgument.convert(arguments));
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
        return ModelService.defineToString(title, get_id(), keys, values);
    }

    /**
     * The base of many objects, that share these same attributes.
     * When it is created through a constructor, that doesn't ask for a DateTime.
     * It will use the DateTime of now.
     */
    @Getter @FieldNameConstants
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class ModelDTO {

        /**
         * The identification value in the database for a specific entity.
         * Must be unique, if there ain't other ids for this entity.
         * UUIDs are unique hex decimal values of the specific entity.
         */
        protected UUID id;

        /**
         * The name for an entity or model.
         * Can be of different purposes,
         * such as username or simply for naming a unit.
         */
        @Setter
        protected String title;


        /** Specifies the time this entity was created. */
        protected Instant timestamp;

        /**
         * Simply said, this is used to describe a relevant situation.
         * Useful to identify an incident or change.
         * Is added to the Response entity class when answering.
         */
        @Setter
        protected Situation situation;

        public ModelDTO(
                UUID id,
                String title,
                Situation situation,
                Instant timestamp
        ) {
            this.id = id;
            this.title = title;
            this.situation = situation;
            this.timestamp = timestamp;
        }

        public ModelDTO(
                UUID id,
                String title,
                Instant timestamp
        ) {
            this.id = id;
            this.title = title;
            this.timestamp = timestamp;
        }

        public ModelDTO(Model model) {
            id = model.get_id();
            title = model.get_title();
            timestamp = model.get_timestamp();
            situation = model.get_situation();
        }
    }
}
