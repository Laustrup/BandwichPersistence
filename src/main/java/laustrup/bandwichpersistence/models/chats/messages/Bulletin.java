package laustrup.bandwichpersistence.models.chats.messages;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;

import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class Bulletin extends Message {

    @Getter
    public Model _receiver;

    public Bulletin(long id, User author, Model receiver, String content,
                    boolean isSent, Plato isEdited, boolean isPublic,
                    LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _receiver = receiver;
    }

    public Bulletin(long id, String content, boolean isSent, Plato isEdited, boolean isPublic, LocalDateTime timestamp) {
        super(id, null, content, isSent, isEdited, isPublic, timestamp);
        _assembling = true;
    }

    public Bulletin(User author, String content) {
        super(author);
    }

    public Model set_reciever(Model reciever) {
        if (_assembling)
            _receiver = reciever;
        return _receiver;
    }

    public User set_author(User author) {
        if (_assembling)
            _author = author;
        return _author;
    }
}
