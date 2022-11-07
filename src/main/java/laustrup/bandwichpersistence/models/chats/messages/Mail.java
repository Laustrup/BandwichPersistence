package laustrup.bandwichpersistence.models.chats.messages;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class Mail extends Message {

    @Getter
    private ChatRoom _chatRoom;

    // Constructor for from database
    public Mail(long id, ChatRoom chatRoom, User author, String content, boolean isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isEdited, isPublic, timestamp);
        _chatRoom = chatRoom;
    }

    // Constructor add to database
    public Mail(ChatRoom chatRoom, User author, String content) {
        super(author, content);
        _chatRoom = chatRoom;
        _isEdited = false;
    }
}
