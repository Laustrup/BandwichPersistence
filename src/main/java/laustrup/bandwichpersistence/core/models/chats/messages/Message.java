package laustrup.bandwichpersistence.core.models.chats.messages;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.User;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

/**
 * A Message that are sent in a ChatRoom.
 */
@Getter @FieldNameConstants
public class Message extends MessageBase {

    /**
     * The ChatRoom that this message has been sent in.
     */
    private ChatRoom _chatRoom;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param mail The transport object to be transformed.
     */
    public Message(DTO mail) {
        super(mail);
        _chatRoom = new ChatRoom(mail.getChatRoom());
    }

    public Message(
            UUID id,
            ChatRoom chatRoom,
            User author,
            String content,
            Instant isSent,
            boolean isEdited,
            Instant isRead,
            Instant timestamp
    ) {
        super(id, author, content, isSent, isEdited, isRead, timestamp);
        _chatRoom = chatRoom;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._id,
                MessageBase.Fields._author,
                Fields._chatRoom,
                MessageBase.Fields._content,
                MessageBase.Fields._sent,
                Model.Fields._timestamp
            }, new String[] {
                String.valueOf(_id),
                _author != null ? _author.toString() : null,
                _chatRoom != null ? _chatRoom.toString() : null,
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
    @Getter @FieldNameConstants
    public static class DTO extends MessageBase.DTO {

        /** The ChatRoom that this message exists in. */
        private ChatRoom.DTO chatRoom;

        /**
         * Converts into this DTO Object.
         * @param message The Object to be converted.
         */
        public DTO(Message message) {
            super(message);
            chatRoom = new ChatRoom.DTO(message.get_chatRoom());
        }
    }
}
