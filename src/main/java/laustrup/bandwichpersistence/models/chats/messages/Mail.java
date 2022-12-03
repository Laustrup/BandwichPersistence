package laustrup.bandwichpersistence.models.chats.messages;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;

import laustrup.bandwichpersistence.utilities.Plato;
import lombok.Getter;

import java.time.LocalDateTime;

public class Mail extends Message {

    @Getter
    private ChatRoom _chatRoom;

    public Mail(long id, ChatRoom chatRoom, User author, String content,
                boolean isSent, Plato isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _chatRoom = chatRoom;
    }

    public Mail(long id, User author, String content,
                boolean isSent, Plato isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _assembling = true;
    }

    public Mail(ChatRoom chatRoom, User author) {
        super(author);
        _chatRoom = chatRoom;
    }

    public ChatRoom set_chatRoom(ChatRoom chatRoom) {
        if (_assembling) {
            _chatRoom = chatRoom;
            _assembling = false;
        }
        return _chatRoom;
    }

    @Override
    public String toString() {
        if (_assembling)
            return "Mail(id:" + _primaryId +
                    "authorId:" + _author.get_primaryId() +
                    "content:" + _content +
                    "isSent:" + _sent +
                    "isEdited:" + _edited.get_argument() +
                    "isPublic:" + _public +
                    "timestamp:" + _timestamp;
        else
            return "Mail(id:" + _primaryId +
                    "ChatRoom:" + _chatRoom. toString() +
                    "author:" + _author.toString() +
                    "content:" + _content +
                    "isSent:" + _sent +
                    "isEdited:" + _edited.get_argument() +
                    "isPublic:" + _public +
                    "timestamp:" + _timestamp;
    }
}
