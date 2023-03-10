package laustrup.bandwichpersistence.models.dtos.chats.messages;

import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.parameters.Plato;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * An abstract class that contains common attributes for Messages.
 */
@NoArgsConstructor @Data
public abstract class MessageDTO extends ModelDTO {

    /**
     * The User that wrote the Message.
     */
    protected UserDTO author;

    /**
     * The content of the written Message.
     */
    protected String content;

    /**
     * True if the Message is sent.
     */
    protected boolean isSent;

    /**
     * A Plato object, that will be true if the Message has been edited.
     * Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     */
    protected Plato.Argument isEdited;

    /**
     * Can be switch between both true and false, if true the message is public for every User.
     */
    protected boolean isPublic;

    public MessageDTO(long id, User author, String content, boolean isSent, Plato isEdited, boolean isPublic, LocalDateTime timestamp) {
        super(id, "Message-"+id,timestamp);
        this.author = DTOService.get_instance().convertToDTO(author);
        this.content = content;
        this.isSent = isSent;
        this.isEdited = isEdited.get_argument();
        this.isPublic = isPublic;
    }
}
