package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ChatRoomBuilder extends BuilderService<ChatRoom> {

    private static final Logger _logger = Logger.getLogger(ChatRoomBuilder.class.getSimpleName());

    private static ChatRoomBuilder _instance;

    private static final MessageBuilder _messageBuilder = MessageBuilder.get_instance();

    private static final UserBuilder _userBuilder = UserBuilder.get_instance();

    public static ChatRoomBuilder get_instance() {
        if (_instance == null)
            _instance = new ChatRoomBuilder();

        return _instance;
    }

    private ChatRoomBuilder() {
        super(ChatRoom.class, _logger);
    }

    @Override
    protected void completion(ChatRoom collective, ChatRoom part) {
        combine(collective.get_messages(), part.get_messages());
        combine(collective.get_chatters(), part.get_chatters());
    }

    @Override
    protected Function<Function<String, Field>, ChatRoom> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String> title = new AtomicReference<>();
            Seszt<Message> messages = new Seszt<>();
            Seszt<User> chatters = new Seszt<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(title, table.apply(Model.ModelDTO.Fields.title));
                        combine(messages, _messageBuilder.build(resultSet));
                        combine(chatters, _userBuilder.build(resultSet));
                        timestamp.set(getInstant(table.apply(Model.ModelDTO.Fields.timestamp)));
                    },
                    id
            );

            return new ChatRoom(
                    id.get(),
                    title.get(),
                    messages,
                    chatters,
                    timestamp.get()
            );
        };
    }
}
