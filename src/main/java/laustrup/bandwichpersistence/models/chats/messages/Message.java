package laustrup.bandwichpersistence.models.chats.messages;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public abstract class Message extends Model {

    @Getter
    protected User _author;
    @Getter @Setter
    protected String _content;
    @Getter @Setter
    protected boolean _isSend, _isEdited, _isPublic;

    public Message(long id, User author, String content, boolean isEdited, boolean isPublic,
                   LocalDateTime timestamp) {
        super(id,"Message-"+id,timestamp);
        _author = author;
        _content = content;
        _isEdited = isEdited;
        _isPublic = isPublic;
    }

    public Message(User author, String content) {
        super("Message-" + content + "-by:" + author.get_title());
        _author = author;
        _content = content;
        _isEdited = false;
    }
}
