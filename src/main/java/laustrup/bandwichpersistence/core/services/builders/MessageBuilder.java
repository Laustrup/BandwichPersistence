package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.chats.messages.MessageBase;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.getInstant;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public class MessageBuilder extends BuilderService<Message> {

    private final UserBuilder _userBuilder = new UserBuilder();

    protected MessageBuilder() {
        super(Message.class, MessageBuilder.class);
    }

    @Override
    protected void completion(Message collective, Message part) {

    }

    @Override
    protected Function<Function<String, JDBCService.Field>, Message> logic(ResultSet resultSet) {
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
                        _userBuilder.complete(author, resultSet);
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
