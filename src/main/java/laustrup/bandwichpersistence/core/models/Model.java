package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.services.ModelService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

import static laustrup.bandwichpersistence.core.services.EternaryService.stating;
import static laustrup.bandwichpersistence.core.services.ObjectService.ifExists;

/**
 * The base of many objects, that share these same attributes.
 * When it is created through a constructor, that doesn't ask for a DateTime.
 * It will use the DateTime of now.
 */
@Getter
@FieldNameConstants
@ToString(of = {"_identity", "_timestamp"})
public abstract class Model<IDENTITY extends Identity<SIGNATURE>, SIGNATURE extends Signature<?>> {

  @Table.Column(value = "id")
  protected IDENTITY _identity;

  /**
   * Specifies the time this entity was created.
   */
  protected Instant _timestamp;

  /**
   * Simply said, this is used to describe a relevant situation.
   * Useful to identify an incident or change.
   * Is added to the Response entity class when answering.
   */
  @Table.ExcludedColumn
  protected Situation _situation = Situation.NONE;

  /**
   * Sets the Situation.
   * The situation mustn't become none after it has had a situation,
   * then it must be resolved, which this method insures to prevent misinformation.
   *
   * @param situation The new Situation.
   * @return The current Situation.
   */
  public Situation set_situation(Situation situation) {
    _situation = situation == Situation.NONE && _situation != Situation.NONE
        ? Situation.RESOLVED
        : situation;

    return _situation;
  }

  public Model(ModelDTO<IDENTITY, SIGNATURE, ?> model, IDENTITY identity) {
    _identity = identity;
    _situation = model.getSituation();
    _timestamp = model.getTimestamp();
  }

  /**
   * Will generate a timestamp of the moment now in datetime.
   */
  public Model() {
    _timestamp = Instant.now();
  }

  /**
   * @param timestamp Specifies the time this entity was created.
   */
  public Model(Instant timestamp) {
    _timestamp = timestamp;
  }

  public Model(IDENTITY id, Instant timestamp) {
    _identity = id;
    _timestamp = timestamp;
  }

  public Model(IDENTITY id) {
    _identity = id;
    _timestamp = Instant.now();
  }

  @Override
  public boolean equals(Object object) {
    return stating(object instanceof Model)
        .then(() -> ModelService.equals(this, object))
        .orElse(false);
  }

  /**
   * The base of many objects, that share these same attributes.
   * When it is created through a constructor, that doesn't ask for a DateTime.
   * It will use the DateTime of now.
   */
  @Getter
  @FieldNameConstants
  @JsonIgnoreProperties(ignoreUnknown = true)
  public abstract static class ModelDTO<IDENTITY extends Identity<SIGNATURE>, SIGNATURE extends Signature<SIGNATURE_TYPE>, SIGNATURE_TYPE> {

    /**
     * The identification value in the database for a specific entity.
     * Must be unique, if there ain't other ids for this entity.
     * UUIDs are unique hex decimal values of the specific entity.
     */
    protected SIGNATURE_TYPE id;


    /**
     * Specifies the time this entity was created.
     */
    protected Instant timestamp;

    /**
     * Simply said, this is used to describe a relevant situation.
     * Useful to identify an incident or change.
     * Is added to the Response entity class when answering.
     */
    @Setter
    protected Situation situation;

    public ModelDTO(
        SIGNATURE_TYPE id,
        Situation situation,
        Instant timestamp
    ) {
      this.id = id;
      this.situation = situation;
      this.timestamp = timestamp;
    }

    public ModelDTO(
        SIGNATURE_TYPE id,
        Instant timestamp
    ) {
      this.id = id;
      this.timestamp = timestamp;
    }

    public ModelDTO(Model<IDENTITY, SIGNATURE> model) {
      id = ifExists(model.get_identity(), Identity::get_value);
      timestamp = model.get_timestamp();
      situation = model.get_situation();
    }
  }
}
