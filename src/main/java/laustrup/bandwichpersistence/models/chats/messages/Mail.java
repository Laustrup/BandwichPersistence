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

    public Mail(long id, ChatRoom chatRoom, User author, String content,
                boolean isSent, boolean isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _chatRoom = chatRoom;
    }

    public Mail(ChatRoom chatRoom, User author) {
        super(author);
        _chatRoom = chatRoom;
    }

}
