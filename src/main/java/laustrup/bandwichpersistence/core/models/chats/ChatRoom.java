package laustrup.bandwichpersistence.core.models.chats;

import laustrup.bandwichpersistence.core.models.BusinessUser;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.services.UserService;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.models.User.UserDTO;
import static laustrup.bandwichpersistence.core.services.ObjectService.ifExists;

/**
 * This is used for multiple Users to communicate with each other through Mails.
 */
@Getter @FieldNameConstants
public class ChatRoom extends Model {

    /**
     * All the Mails that has been sent will be stored here.
     */
    private Seszt<Message> _messages;

    /**
     * The Users, except the responsible, that can write with each other.
     */
    private Seszt<User> _chatters;

    /**
     * Converts a Data Transport Object into this object.
     * @param chatRoom The Data Transport Object that will be converted.
     */
    public ChatRoom(ChatRoom.DTO chatRoom) {
        super(chatRoom);
        _messages = new Seszt<>();
        convert(chatRoom.getMails());
        convert(chatRoom.getChatters());
    }

    /**
     * Converts a Data Transport Object into Mails.
     * @param mails The Data Transport Object that will be converted.
     */
    private void convert(Message.DTO[] mails) {
        _messages = new Seszt<>();
        for (Message.DTO mail : mails)
            _messages.add(new Message(mail));
    }

    /**
     * Converts a Data Transport Object into Chatters.
     * @param chatters The Data Transport Object that will be converted.
     */
    private void convert(UserDTO[] chatters) {
        _chatters = new Seszt<>();
        for (UserDTO chatter : chatters)
            _chatters.add(UserService.from(chatter));
    }

    /**
     * Containing all attributes of this object.
     * @param id The primary id.
     * @param title The title of the ChatRoom, if it is null or empty, it will be the usernames of the chatters.
     * @param messages The Mails with relations to this ChatRoom.
     * @param chatters The chatters that are members of this ChatRoom.
     * @param timestamp The time this ChatRoom was created.
     */
    public ChatRoom(
            UUID id,
            String title,
            Seszt<Message> messages,
            Seszt<User> chatters,
            Instant timestamp
    ) {
        super(id, title, timestamp);
        _chatters = chatters;
        _title = determineChatRoomTitle(_title);
        _messages = messages;
    }

    /**
     * Will generate a new ChatRoom.
     * Timestamp will be of now.
     * @param title The title of the ChatRoom, if it is null or empty, it will be the usernames of the chatters.
     * @param messages The Mails with relations to this ChatRoom.
     * @param chatters The chatters that are members of this ChatRoom.
     */
    public ChatRoom(String title, Seszt<Message> messages, Seszt<User> chatters) {
        super(title);
        _messages = messages;
        _chatters = chatters;
    }

    /**
     * Will generate a title of the chatters of this ChatRoom
     * but only if the title isn't default set yet.
     * @return The generated Title.
     */
    private String determineChatRoomTitle() {
        String title = determineChatRoomTitle(null);
        return _title.equals(title) ? _title : title;
    }

    /**
     * Will make the title of this ChatRoom be of custom title or chatters' usernames.
     * @param title The custom title.
     * @return The determined title.
     */
    private String determineChatRoomTitle(String title) {
        if (title == null || title.isEmpty()) {
            StringBuilder usernames = new StringBuilder();

            for (int i = 1; i <= _chatters.size(); i++)
                usernames.append(_chatters.Get(i).get_username()).append(i < _chatters.size() ? ", " : "");

            return usernames.toString();
        }
        else
            return title;
    }

    /**
     * Adds a Mail to the ChatRoom, if the author of the Mail is a chatter of the ChatRoom.
     * If the responsible haven't answered yet, it will check if it now is answered.
     * @param message A Mail object, that is wished to be added.
     * @return All the Mails of this ChatRoom.
     */
    public Seszt<Message> add(Message message) { return add(new Message[]{message}); }

    /**
     * Adds Mails to the ChatRoom, if the author of the Mails is a chatter of the ChatRoom.
     * If the responsible haven't answered yet, it will check if it now is answered.
     * @param messages Mail objects, that is wished to be added.
     * @return All the Mails of this ChatRoom.
     */
    public Seszt<Message> add(Message[] messages) {
        ifExists(messages, () -> {
            for (Message message : messages)
                if (exists(message.get_author()))
                    _messages.add(message);
        });

        return _messages;
    }

    /**
     * It will add a chatter, if it isn't already added.
     * If the chatter is a Band, it will try to add all the members of the Band,
     * unless some already is a chatter.
     * @param chatter A user that is wished to be added as a chatter of the ChatRoom.
     * @return All the chatters of the ChatRoom.
     */
    public Seszt<User> add(User chatter) {
        return add(new User[]{chatter});
    }

    /**
     * It will add some chatters, if they aren't already added.
     * If the chatters are of Band, it will try to add all the members of the Band,
     * unless some already is a chatter.
     * @param chatters A users that is wished to be added as a chatter of the ChatRoom.
     * @return All the chatters of the ChatRoom.
     */
    public Seszt<User> add(User[] chatters) {
        ifExists(chatters,() -> {
            for (User chatter : chatters) {
                _chatters.add(chatter);
                _title = determineChatRoomTitle();
            }
        });

        return _chatters;
    }

    /**
     * Checks if a chatter exists in the ChatRoom.
     * @param chatter A User, that should be checked, if it already exists in the ChatRoom.
     * @return True if the chatter exists in the ChatRoom.
     */
    public boolean exists(User chatter) {
        for (User user : _chatters)
            if (
                user.getClass() == chatter.getClass()
                && user.get_id() == chatter.get_id()
            )
                return true;

        return false;
    }

    /**
     * Will remove a Mail from the ChatRoom.
     * @param message The Mail object that is wished to be removed.
     * @return All the Mails of this ChatRoom.
     */
    public Seszt<Message> remove(Message message) {
        for (int i = 1; i <= _messages.size(); i++) {
            if (_messages.Get(i).get_id() == message.get_id()) {
                _messages.remove(_messages.Get(i));
                break;
            }
        }

        return _messages;
    }

    /**
     * Will remove a chatter from the ChatRoom.
     * @param chatter A user object that is wished to be removed.
     * @return All the chatters of this ChatRoom.
     */
    public Seszt<User> remove(User chatter) {
        for (int i = 1; i <= _chatters.size(); i++) {
            if (_chatters.Get(i).get_id() == chatter.get_id()) {
                _chatters.remove(_chatters.Get(i));
                break;
            }
        }

        return _chatters;
    }

    /**
     * Edits a Mail of the ChatRoom.
     * @param message The Mail that is an updated version of a previous Mail, which will be updated.
     * @return True if it will be edited correctly.
     */
    public boolean edit(Message message) {
        for (int i = 1; i <= _messages.size(); i++) {
            if (_messages.Get(i).get_id() == message.get_id()) {
                _messages.set(i, message);
                return message == _messages.get(i);
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._id,
                Model.Fields._title,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_id),
                _title,
                String.valueOf(_timestamp)
            }
        );
    }

    @Getter
    public static class Template extends Model {

        private Seszt<BusinessUser> _chatters;

        public Template(DTO settings) {
            this(
                    settings.getId(),
                    settings.getTitle(),
                    Seszt.copy(settings.getChatters(), chatter -> UserService.fromBusinessUser(chatter)),
                    settings.getTimestamp()
            );
        }

        public Template(
                UUID id,
                String title,
                Seszt<BusinessUser> chatters,
                Instant timestamp
        ) {
            super(id, title, timestamp);
            _chatters = chatters;
        }

        @Getter
        public static class DTO extends ModelDTO {

            private Set<BusinessUser.BusinessUserDTO> chatters;

            public DTO(Template settings) {
                super(settings);
                chatters = settings.get_chatters().stream()
                        .map(UserService::fromBusinessUser)
                        .collect(Collectors.toSet());
            }
        }
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends ModelDTO {

        /** All the Mails that has been sent will be stored here. */
        private Message.DTO[] mails;

        /** The Users, except the responsible, that can write with each other. */
        private UserDTO[] chatters;

        /**
         * Converts into this DTO Object.
         * @param chatRoom The Object to be converted.
         */
        public DTO(ChatRoom chatRoom) {
            super(chatRoom);
            mails = new Message.DTO[chatRoom.get_messages().size()];
            for (int i = 0; i < mails.length; i++)
                mails[i] = new Message.DTO(chatRoom.get_messages().get(i));
            chatters = new User.UserDTO[chatRoom.get_chatters().size()];
            for (int i = 0; i < chatters.length; i++)
                chatters[i] = UserService.from(chatRoom.get_chatters().get(i));
        }
    }
}
