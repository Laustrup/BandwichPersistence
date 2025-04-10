package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.BusinessUser;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.getInstant;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public class ChatRoomTemplateBuilder extends BuilderService<ChatRoom.Template> {

    private final UserBuilder _userBuilder = new UserBuilder();

    protected ChatRoomTemplateBuilder() {
        super(ChatRoom.Template.class, ChatRoomTemplateBuilder.class);
    }

    @Override
    protected void completion(ChatRoom.Template collective, ChatRoom.Template part) {
        combine(collective.get_chatters(), part.get_chatters());
    }

    @Override
    protected Function<Function<String, JDBCService.Field>, ChatRoom.Template> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String> title = new AtomicReference<>();
            Seszt<BusinessUser> chatters = new Seszt<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(title, table.apply(Model.ModelDTO.Fields.title));
                        combine(chatters, (BusinessUser) _userBuilder.build(resultSet));
                        timestamp.set(getInstant(table.apply(Model.ModelDTO.Fields.timestamp)));
                    },
                    id
            );

            return new ChatRoom.Template(
                    id.get(),
                    title.get(),
                    chatters,
                    timestamp.get()
            );
        };
    }
}
