package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.chats.messages.MessageBase;
import laustrup.bandwichpersistence.core.persistence.Field;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.getInstant;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public class MessageBuilder extends BuilderService<Message> {

    private static final Logger _logger = Logger.getLogger(MessageBuilder.class.getName());

    private static MessageBuilder _instance;

    public static MessageBuilder get_instance() {
        if (_instance == null)
            _instance = new MessageBuilder();

        return _instance;
    }

    private MessageBuilder() {
        super(Message.class, _logger);
    }

    @Override
    protected void completion(Message collective, Message part) {

    }

    @Override
    protected Function<Function<String, Field>, Message> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<User> author = new AtomicReference<>();
            AtomicReference<String> content = new AtomicReference<>();
            AtomicReference<Instant>
                    isSent = new AtomicReference<>(),
                    isRead = new AtomicReference<>(),
                    timestamp = new AtomicReference<>();
            AtomicReference<Boolean> isEdited = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        UserBuilder.get_instance().complete(author, resultSet);
                        set(content, table.apply(MessageBase.Fields._content));
                        set(isSent, table.apply(MessageBase.Fields._sent));
                        set(isRead, table.apply(MessageBase.Fields._read));
                        set(isEdited, table.apply(MessageBase.Fields._edited));
                        timestamp.set(getInstant(table.apply(Model.ModelDTO.Fields.timestamp)));
                    },
                    id
            );

            return new Message(
                    id.get(),
                    author.get(),
                    content.get(),
                    isSent.get(),
                    isEdited.get(),
                    isRead.get(),
                    timestamp.get()
            );
        };
    }
}
