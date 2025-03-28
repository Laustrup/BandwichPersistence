package laustrup.bandwichpersistence.core.models.chats.messages;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.services.UserService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.models.User.UserDTO;

/**
 * An abstract class that contains common attributes for Messages.
 */
@Getter @FieldNameConstants
public abstract class MessageBase extends Model {

    /**
     * The User that wrote the Message.
     */
    @Getter
    protected User _author;

    /**
     * The content of the written Message.
     */
    @Setter
    protected String _content;

    /**
     * If null it has not been sent, otherwise it has at the time it has.
     */
    @Setter
    protected Instant _sent;

    /**
     * Can be null in case that it have never been edited, otherwise it will be the time that it was edited.
     */
    protected boolean _edited;

    protected Instant _read;

    /**
     * Converts a Data Transport Object into this object.
     * @param message The Data Transport Object that will be converted.
     */
    public MessageBase(DTO message) {
        this(
                message.getId(),
                UserService.from(message.getAuthor()),
                message.getContent(),
                message.getSent(),
                message.isEdited(),
                message.getRead(),
                message.getTimestamp()
        );
    }

    public MessageBase(
            UUID id,
            User author,
            String content,
            Instant sent,
            boolean isEdited,
            Instant read,
            Instant timestamp
    ) {
        super(id, "Message-"+id, timestamp);
        _author = author;
        _content = content;
        _sent = sent;
        _edited = isEdited;
        _read = read;
    }

    /**
     * Will tell if the Message is sent, by whether the time is sent was null.
     * @return True if sent is null.
     */
    public boolean isSent() {
        return _sent != null;
    }

    /**
     * Simply checks if the date it has been edited is null.
     * @return True if it has never been edited.
     */
    public boolean isEdited() {
        return _sent == null;
    }

    /**
     * Will edit the content and also set edited to true, if it is sent.
     * @param content The updated content, that is wished to be replaced with the old content.
     * @return The content of the Message.
     */
    public String edit(String content) {
        _content = content;
        if (_sent != null)
            _edited = true;

        return _content;
    }

    /**
     * An abstract class that contains common attributes for Messages.
     */
    @Getter @FieldNameConstants
    public abstract static class DTO extends ModelDTO {

        /**
         * The User that wrote the Message.
         */
        protected UserDTO author;

        /**
         * The content of the written Message.
         */
        protected String content;

        /**
         * If null it has not been sent, otherwise it has at the time it has.
         */
        protected Instant sent;

        /**
         * Can be null in case that it have never been edited, otherwise it will be the time that it was edited.
         */
        protected boolean isEdited;

        protected Instant read;

        public DTO(
                UUID id,
                String title,
                Instant timestamp,
                UserDTO author,
                String content,
                Instant sent,
                boolean isEdited,
                Instant read
        ) {
            super(id, title, timestamp);
            this.author = author;
            this.content = content;
            this.sent = sent;
            this.isEdited = isEdited;
            this.read = read;
        }

        /**
         * Converts into this DTO Object.
         * @param message The Object to be converted.
         */
        public DTO(MessageBase message) {
            super(message);
            author = UserService.from(message.get_author());
            content = message.get_content();
            sent = message.get_sent();
            isEdited = message.isEdited();
            read = message.get_read();
        }
    }
}
