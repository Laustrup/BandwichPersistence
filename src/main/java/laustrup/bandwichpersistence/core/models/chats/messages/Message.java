package laustrup.bandwichpersistence.core.models.chats.messages;

import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.JoinedModel;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.services.DTOService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.models.User.UserDTO;

/**
 * An abstract class that contains common attributes for Messages.
 */
@Getter @FieldNameConstants
public abstract class Message extends JoinedModel {

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
    protected LocalDateTime _sent;

    /**
     * Can be null in case that it have never been edited, otherwise it will be the time that it was edited.
     */
    protected LocalDateTime _edited;

    /**
     * Can be switched between both true and false, if true the message is public for every User.
     */
    @Setter
    protected boolean _public;

    /**
     * Converts a Data Transport Object into this object.
     * @param message The Data Transport Object that will be converted.
     */
    public Message(DTO message) {
        super(message);
        _author = (User) DTOService.convert(message.getAuthor());
        _content = message.getContent();
        _sent = message.getSent();
        _edited = message.getIsEdited();
        _public = message.isPublic();
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param author The User that wrote the Message.
     * @param content The content of the written Message.
     * @param isSent True if the Message is sent.
     * @param isEdited A Plato object, that will be true if the Message has been edited.
     *                 Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     * @param isPublic Can be switched between both true and false, if true the message is public for every User.
     * @param history The Events for this object.
     * @param timestamp Specifies the time this entity was created.
     */
    public Message(
            UUID id,
            User author,
            String content,
            LocalDateTime isSent,
            LocalDateTime isEdited,
            boolean isPublic,
            History history,
            Instant timestamp
    ) {
        super(id, "Message-"+id, history, timestamp);
        _author = author;
        _content = content;
        _sent = isSent;
        _edited = isEdited;
        _public = isPublic;
    }

    /**
     * Generating a new Message as a draft.
     * Timestamp will be of now.
     * @param author The User that wrote the Message.
     * @param content The content of the written Message.
     * @param isSent True if the Message is sent.
     * @param isEdited A Plato object, that will be true if the Message has been edited.
     *                 Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     * @param isPublic Can be switched between both true and false, if true the message is public for every User.
     */
    public Message(User author, String content, LocalDateTime isSent, LocalDateTime isEdited, boolean isPublic) {
        super(null, "New Message");
        _author = author;
        _content = content;
        _sent = isSent;
        _edited = isEdited;
        _public = isPublic;
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
            _edited = LocalDateTime.now();

        return _content;
    }

    /**
     * An abstract class that contains common attributes for Messages.
     */
    @Getter
    protected abstract static class DTO extends JoinedModelDTO {

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
        protected LocalDateTime sent;

        /**
         * Can be null in case that it have never been edited, otherwise it will be the time that it was edited.
         */
        protected LocalDateTime isEdited;

        /**
         * Can be switched between both true and false, if true the message is public for every User.
         */
        protected boolean isPublic;

        /**
         * Converts into this DTO Object.
         * @param message The Object to be converted.
         */
        public DTO(Message message) {
            super(message);
            this.author = DTOService.convert(message.get_author());
            this.content = message.get_content();
            this.sent = message.get_sent();
            this.isEdited = message.get_edited();
            this.isPublic = message.is_public();
        }
    }
}
