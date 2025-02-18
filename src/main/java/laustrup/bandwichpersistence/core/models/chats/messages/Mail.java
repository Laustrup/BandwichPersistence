package laustrup.bandwichpersistence.core.models.chats.messages;

import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.User;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A Message that are sent in a ChatRoom.
 */
@Getter @FieldNameConstants
public class Mail extends Message {

    /**
     * The ChatRoom that this message has been sent in.
     */
    private ChatRoom _chatRoom;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param mail The transport object to be transformed.
     */
    public Mail(DTO mail) {
        super(mail);
        _chatRoom = new ChatRoom(mail.getChatRoom());
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param chatRoom The ChatRoom that this message has been sent in.
     * @param author The User that wrote the Message.
     * @param content The content of the written Message.
     * @param isSent True if the Message is sent.
     * @param isEdited A Plato object, that will be true if the Message has been edited.
     *                 Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     * @param isPublic Can be switched between both true and false, if true the message is public for every User.
     * @param history The Events for this object.
     * @param timestamp Specifies the time this entity was created.
     */
    public Mail(
            UUID id,
            ChatRoom chatRoom,
            User author,
            String content,
            LocalDateTime isSent,
            LocalDateTime isEdited,
            boolean isPublic,
            History history,
            LocalDateTime timestamp
    ) {
        super(id, author, content, isSent, isEdited, isPublic, history, timestamp);
        _chatRoom = chatRoom;
    }

    /**
     * Generating a new Mail as a draft.
     * Timestamp will be of now.
     * @param chatRoom The ChatRoom forum that are having the Mail posted.
     * @param author The User that wrote the Message.
     * @param content The content of the written Message.
     */
    public Mail(
            ChatRoom chatRoom,
            User author,
            String content
    ) {
        super(author, content, null, null, false);
        _chatRoom = chatRoom;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._primaryId,
                Message.Fields._author,
                Fields._chatRoom,
                Message.Fields._content,
                Message.Fields._sent,
                Message.Fields._edited,
                Message.Fields._public,
                Model.Fields._timestamp
            }, new String[] {
                String.valueOf(_primaryId),
                _author != null ? _author.toString() : null,
                _chatRoom != null ? _chatRoom.toString() : null,
                _content,
                String.valueOf(_sent),
                _edited != null ? _edited.toString() : null,
                String.valueOf(_public),
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
    public static class DTO extends Message.DTO {

        /** The ChatRoom that this message exists in. */
        private ChatRoom.DTO chatRoom;

        /**
         * Converts into this DTO Object.
         * @param mail The Object to be converted.
         */
        public DTO(Mail mail) {
            super(mail);
            chatRoom = new ChatRoom.DTO(mail.get_chatRoom());
        }
    }
}
