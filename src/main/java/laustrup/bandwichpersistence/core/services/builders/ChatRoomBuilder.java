package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.BusinessUser;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.chats.messages.MessageBase;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ChatRoomBuilder extends BuilderService {

    private static final Logger _logger = Logger.getLogger(ChatRoomBuilder.class.getName());

    public static ChatRoom build(ResultSet resultSet) {
        Class<ChatRoom> clazz = ChatRoom.class;

        return handle(
                clazz,
                field -> {
                    AtomicReference<UUID> id = new AtomicReference<>();
                    AtomicReference<String> title = new AtomicReference<>();
                    Seszt<Message> messages = new Seszt<>();
                    Seszt<User> chatters = new Seszt<>();
                    AtomicReference<Instant> timestamp = new AtomicReference<>();

                    try {
                        JDBCService.build(
                                resultSet,
                                () -> {
                                    id.set(getUUID(field.apply(Model.ModelDTO.Fields.id)));
                                    set(title, field.apply(Model.ModelDTO.Fields.title));
                                    messages.add(buildMessage(resultSet));
                                    chatters.add(UserBuilder.build(resultSet));
                                    timestamp.set(getInstant(field.apply(Model.ModelDTO.Fields.timestamp)));
                                },
                                id.get()
                        );
                    } catch (SQLException e) {
                        printError(clazz, id.get(), e, _logger);
                    }

                    return new ChatRoom(
                            id.get(),
                            title.get(),
                            messages,
                            chatters,
                            timestamp.get()
                    );
                }
        );
    }

    public static Message buildMessage(ResultSet resultSet) {
        Class<Message> clazz = Message.class;

        return handle(
                clazz,
                table -> {
                    AtomicReference<UUID> id = new AtomicReference<>();
                    AtomicReference<User> author = new AtomicReference<>();
                    AtomicReference<String> content = new AtomicReference<>();
                    AtomicReference<Instant>
                            isSent = new AtomicReference<>(),
                            isRead = new AtomicReference<>(),
                            timestamp = new AtomicReference<>();
                    AtomicReference<Boolean> isEdited = new AtomicReference<>();

                    try {
                        JDBCService.build(
                                resultSet,
                                () -> {
                                    set(id, table.apply(Model.ModelDTO.Fields.id));
                                    author.set(UserBuilder.build(resultSet));
                                    set(content, table.apply(MessageBase.Fields._content));
                                    set(isSent, table.apply(MessageBase.Fields._sent));
                                    set(isRead, table.apply(MessageBase.Fields._read));
                                    timestamp.set(getInstant(table.apply(Model.ModelDTO.Fields.timestamp)));
                                    set(isEdited, table.apply(MessageBase.Fields._edited));
                                },
                                id.get()
                        );
                    } catch (SQLException e) {
                        printError(clazz, id.get(), e, _logger);
                    }

                    return new Message(
                            id.get(),
                            author.get(),
                            content.get(),
                            isSent.get(),
                            isEdited.get(),
                            isRead.get(),
                            timestamp.get()
                    );
                }
        );
    }

    public static ChatRoom.Template buildTemplate(ResultSet resultSet) {
        Class<ChatRoom.Template> clazz = ChatRoom.Template.class;

        return handle(
                clazz,
                table -> {
                    AtomicReference<UUID> id = new AtomicReference<>();
                    AtomicReference<String> title = new AtomicReference<>();
                    Seszt<BusinessUser> chatters = new Seszt<>();
                    AtomicReference<Instant> timestamp = new AtomicReference<>();

                    try {
                        JDBCService.build(
                                resultSet,
                                () -> {

                                },
                                id.get()
                        );
                    } catch (SQLException e) {
                        printError(clazz, id.get(), e, _logger);
                    }

                    return new ChatRoom.Template(
                            id.get(),
                            title.get(),
                            chatters,
                            timestamp.get()
                    );
                }
        );
    }
}
