package laustrup.bandwichpersistence.core.models.chats.messages;

import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import lombok.Getter;

import java.time.Instant;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;

/**
 * A Message that are sent in a ChatRoom.
 */
@Getter @Table
public class Message extends MessageBase<Message.Id> {

  /**
   * Will translate a transport object of this object into a construct of this object.
   * @param mail The transport object to be transformed.
   */
  public Message(DTO mail) {
    super(mail, new Id(new Signature.UUID(mail.getId())));
  }

  public Message(
      Id id,
      User<? extends User.Id> author,
      String content,
      Instant isSent,
      boolean isEdited,
      Instant isRead,
      Instant timestamp
  ) {
    super(id, author, content, isSent, isEdited, isRead, timestamp);
  }

  @Override
  public String toString() {
    return toStringify(this);
  }

  public static class Id extends CommonIdentity<Signature.UUID> {

    public Id(Signature.UUID signature) {
      super(signature);
    }

    public Id(java.util.UUID signature) {
      super(new Signature.UUID(signature));
    }

    @Override
    public Class<Message> getOwnerClassType() {
          return Message.class;
        }
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends MessageBase.DTO<Message.Id> {

        /**
         * Converts into this DTO Object.
         * @param message The Object to be converted.
         */
        public DTO(Message message) {
            super(message);
        }
    }
}
