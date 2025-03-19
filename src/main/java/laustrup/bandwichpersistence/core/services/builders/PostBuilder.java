package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.chats.messages.MessageBase;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class PostBuilder {

    private static final Logger _logger = Logger.getLogger(AlbumBuilder.class.getSimpleName());

    public static Post build(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<User> author = new AtomicReference<>();
        AtomicReference<Model> receiver = new AtomicReference<>();
        AtomicReference<String> content = new AtomicReference<>();
        AtomicReference<Boolean> isEdited = new AtomicReference<>();
        AtomicReference<Instant>
                isSent = new AtomicReference<>(),
                read = new AtomicReference<>(),
                timestamp = new AtomicReference<>();
        
        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(get(
                                JDBCService::getUUID,
                                Model.ModelDTO.Fields.id
                        ));
                        author.set(UserBuilder.build(resultset));
                        receiver.set(ModelBuilder.build(resultset));
                        content.set(getString(MessageBase.DTO.Fields.content));
                        isEdited.set(get(
                                JDBCService::getBoolean,
                                MessageBase.DTO.Fields.isEdited
                        ));
                        isSent.set(getInstant(Model.ModelDTO.Fields.timestamp));
                        read.set(getInstant(Model.ModelDTO.Fields.timestamp));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    }, primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (Exception exception) {
                _logger.log(
                        Level.WARNING,
                        String.format(
                                "Couldn't build Post with id %s, message is:\n\n%s",
                                id.get(),
                                exception.getMessage()
                        ),
                        exception
                );
        }

        return new Post(
                id.get(),
                author.get(),
                receiver.get(),
                content.get(),
                isSent.get(),
                isEdited.get(),
                read.get(),
                timestamp.get()
        );
    }
}
