package laustrup.bandwichpersistence.core.models.chats.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.services.ModelService;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.from;
import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;

/**
 * A kind of post that can be posted at any Model Object.
 */
@Getter @FieldNameConstants @Table
public class Post extends MessageBase<Post.Id> {

  public Model<? extends Identity<?>, ?> _receiver;

  /**
   * Will translate a transport object of this object into a construct of this object.
   * @param post The transport object to be transformed.
   */
  public Post(DTO post) {
    super(post, new Id(new Signature.UUID(post.getId())));
    _receiver = from(post);
  }

  public Post(
      Id id,
      User<? extends User.Id> author,
      Model<? extends Identity<?>, ?> receiver,
      String content,
      Instant isSent,
      boolean isEdited,
      Instant read,
      Instant timestamp
  ) {
    super(id, author, content, isSent, isEdited, read, timestamp);
    _receiver = receiver;
  }

  public static class Id extends CommonIdentity<Signature.UUID> {

    public Id(Signature.UUID signature) {
      super(signature);
    }

    public Id(java.util.UUID id) {
      super(new Signature.UUID(id));
    }

    @Override
    public Class<?> getOwnerClassType() {
      return Post.class;
    }
  }

  @Override
  public String toString() {
    return toStringify(this);
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @FieldNameConstants @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO extends MessageBase.DTO<Post.Id> {

        public ModelDTO<Identity<Signature<?>>, Signature<?>, ?> receiver;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty UUID id,
                @JsonProperty Instant timestamp,
                @JsonProperty User.UserDTO<? extends User.Id> author,
                @JsonProperty String content,
                @JsonProperty Instant sent,
                @JsonProperty boolean isEdited,
                @JsonProperty Instant read,
                @JsonProperty ModelDTO<Identity<Signature<?>>, Signature<?>, ?> receiver
        ) {
            super(id, timestamp, author, content, sent, isEdited, read);
            this.receiver = receiver;
        }

        /**
         * Converts into this DTO Object.
         * @param post The Object to be converted.
         */
        public DTO(Post post) {
            super(post);
            receiver = (ModelDTO<Identity<Signature<?>>, Signature<?>, ?>) ModelService.from(post.get_receiver());
        }
    }
}
