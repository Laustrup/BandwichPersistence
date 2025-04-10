package laustrup.bandwichpersistence.core.models.chats.messages;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * A Message that are sent in a ChatRoom.
 */
@Getter
public class Message extends MessageBase {


    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param mail The transport object to be transformed.
     */
    public Message(DTO mail) {
        super(mail);
    }

    public Message(
            UUID id,
            User author,
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
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._id,
                MessageBase.Fields._author,
                MessageBase.Fields._content,
                MessageBase.Fields._sent,
                Model.Fields._timestamp
            }, new String[] {
                String.valueOf(_id),
                _author != null ? _author.toString() : null,
                _content,
                String.valueOf(_sent),
                String.valueOf(_timestamp)
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends MessageBase.DTO {

        /**
         * Converts into this DTO Object.
         * @param message The Object to be converted.
         */
        public DTO(Message message) {
            super(message);
        }
    }
}
