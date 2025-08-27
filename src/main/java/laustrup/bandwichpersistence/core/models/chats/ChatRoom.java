package laustrup.bandwichpersistence.core.models.chats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Situation;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.BusinessUser;
import laustrup.bandwichpersistence.core.models.users.BusinessUser.BusinessUserDTO;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.models.DatabaseEntityConfigurations;
import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;
import laustrup.bandwichpersistence.core.services.UserService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.models.users.User.UserDTO;
import static laustrup.bandwichpersistence.core.services.ObjectService.ifExists;

/**
 * This is used for multiple Users to communicate with each other through Mails.
 */
@Getter @FieldNameConstants @DatabaseEntity(title = "chat_rooms")
public class ChatRoom extends Model<ChatRoom.Id, Signature.UUID> implements DatabaseEntityConfigurations {

    /**
     * All the Mails that has been sent will be stored here.
     */
    private Seszt<Message> _messages;

    /**
     * The Users, except the responsible, that can write with each other.
     */
    private Seszt<User<? extends User.Id>> _chatters;

    public ChatRoom(ChatRoom.DTO chatRoom) {
        super(chatRoom, new Id(chatRoom.getId()));
        _messages = Seszt.copy(chatRoom.getMails(), Message::new);
        _chatters = Seszt.copy(chatRoom.getChatters(), UserService::from);
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
            Id id,
            String title,
            Seszt<Message> messages,
            Seszt<User<?>> chatters,
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
    public ChatRoom(String title, Seszt<Message> messages, Seszt<User<?>> chatters) {
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
    public Seszt<User<?>> add(User<?> chatter) {
        return add(new User[]{chatter});
    }

    /**
     * It will add some chatters, if they aren't already added.
     * If the chatters are of Band, it will try to add all the members of the Band,
     * unless some already is a chatter.
     * @param chatters A users that is wished to be added as a chatter of the ChatRoom.
     * @return All the chatters of the ChatRoom.
     */
    public Seszt<User<?>> add(User<?>[] chatters) {
        ifExists(chatters,() -> {
            for (User<?> chatter : chatters) {
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
    public boolean exists(User<?> chatter) {
        for (User<?> user : _chatters)
            if (
                user.getClass() == chatter.getClass()
                && user.get_identity() == chatter.get_identity()
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
            if (_messages.Get(i).get_identity() == message.get_identity()) {
                _messages.remove(_messages.Get(i));
                break;
            }
        }

        return _messages;
    }

    /**
     * Edits a Mail of the ChatRoom.
     * @param message The Mail that is an updated version of a previous Mail, which will be updated.
     * @return True if it will be edited correctly.
     */
    public boolean edit(Message message) {
        for (int i = 1; i <= _messages.size(); i++) {
            if (_messages.Get(i).get_identity() == message.get_identity()) {
                _messages.set(i, message);
                return message == _messages.get(i);
            }
        }

        return false;
    }

    public static class Id extends CommonIdentity<Signature.UUID> {

        public Id(Signature.UUID signature) {
            super(signature);
        }

        public Id(java.util.UUID signature) {
            super(new Signature.UUID(signature));
        }

        @Override
        public Class<?> getOwnerClassType() {
            return Id.class;
        }
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._identity,
                Model.Fields._title,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_identity),
                _title,
                String.valueOf(_timestamp)
            }
        );
    }

    @Getter
    public static class Template extends Model<Template.Id, Signature.UUID> {

        private Seszt<BusinessUser<?>> _chatters;

        public Template(DTO settings) {
            this(
                    new Id(new Signature.UUID(settings.getId())),
                    settings.getTitle(),
                    Seszt.copy(settings.getChatters(), UserService::fromBusinessUser),
                    settings.getTimestamp()
            );
        }

        public Template(
                Id id,
                String title,
                Seszt<BusinessUser<?>> chatters,
                Instant timestamp
        ) {
            super(id, title, timestamp);
            _chatters = chatters;
        }

        public static class Id extends CommonIdentity<Signature.UUID> {

            public Id(Signature.UUID identifier) {
                super(identifier);
            }

            public Id(java.util.UUID identifier) {
                super(new Signature.UUID(identifier));
            }

            @Override
            public Class<?> getOwnerClassType() {
                return Id.class;
            }
        }

    @Getter @FieldNameConstants @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO extends ModelDTO<ChatRoom.Template.Id, Signature.UUID, java.util.UUID> {

            private Set<BusinessUserDTO<? extends User.Id>> chatters;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty java.util.UUID id,
                    @JsonProperty String title,
                    @JsonProperty Instant timestamp,
                    @JsonProperty Set<BusinessUserDTO<?>> chatters
            ) {
                super(id, title, timestamp);
                this.chatters = chatters;
            }

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
    public static class DTO extends ModelDTO<ChatRoom.Id, Signature.UUID, java.util.UUID> {

        /** All the Mails that has been sent will be stored here. */
        private Set<Message.DTO> mails;

        /** The Users, except the responsible, that can write with each other. */
        private Set<UserDTO<? extends User.Id>> chatters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty java.util.UUID id,
                @JsonProperty String title,
                @JsonProperty Situation situation,
                @JsonProperty Instant timestamp,
                @JsonProperty Set<Message.DTO> mails,
                @JsonProperty Set<UserDTO<? extends User.Id>> chatters
        ) {
            super(id, title, situation, timestamp);
            this.mails = mails;
            this.chatters = chatters;
        }

        /**
         * Converts into this DTO Object.
         * @param chatRoom The Object to be converted.
         */
        public DTO(ChatRoom chatRoom) {
            super(chatRoom);
            mails = chatRoom.get_messages().asSet(Message.DTO::new);
            chatters = chatRoom.get_chatters().asSet(UserService::from);
        }
    }
}
