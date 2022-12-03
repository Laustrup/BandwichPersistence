package laustrup.bandwichpersistence.models.chats.messages;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;

import laustrup.bandwichpersistence.utilities.Plato;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class Bulletin extends Message {

    @Getter
    public Model _receiver;

    public Bulletin(long id, User author, Model receiver, String content, boolean isSent, Plato isEdited, boolean isPublic, LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _receiver = receiver;
    }

    public Bulletin(long id, String content, boolean isSent, Plato isEdited, boolean isPublic, LocalDateTime timestamp) {
        super(id, null, content, isSent, isEdited, isPublic, timestamp);
    }

    public Bulletin(User author, String content) {
        super(author);
    }
}
