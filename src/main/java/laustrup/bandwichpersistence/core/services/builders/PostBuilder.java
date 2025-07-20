package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.chats.messages.MessageBase;
import laustrup.bandwichpersistence.core.persistence.Field;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class PostBuilder extends BuilderService<Post> {

    private static final Logger _logger = Logger.getLogger(PostBuilder.class.getName());

    private static PostBuilder _instance;

    public static PostBuilder get_instance() {
        if (_instance == null)
            _instance = new PostBuilder();

        return _instance;
    }

    private PostBuilder() {
        super(Post.class, _logger);
    }

    private static UserBuilder _userBuilder = UserBuilder.get_instance();

    private static ModelBuilder _modelBuilder = ModelBuilder.get_instance();

    @Override
    protected void completion(Post reference, Post object) {

    }

    @Override
    protected Function<Function<String, Field>, Post> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<User<? extends User.Id>> author = new AtomicReference<>();
            AtomicReference<Model<? extends Identity<?>, ?>> receiver = new AtomicReference<>();
            AtomicReference<String> content = new AtomicReference<>();
            AtomicReference<Boolean> isEdited = new AtomicReference<>();
            AtomicReference<Instant>
                    isSent = new AtomicReference<>(),
                    read = new AtomicReference<>(),
                    timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        _userBuilder.complete(author, resultSet);
                        _modelBuilder.complete(receiver, resultSet);
                        set(content, table.apply(MessageBase.DTO.Fields.content));
                        set(isEdited, table.apply(MessageBase.DTO.Fields.isEdited));
                        set(isSent, table.apply(MessageBase.DTO.Fields.sent));
                        set(read, table.apply(MessageBase.DTO.Fields.read));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Post(
                    new Post.Id(id.get()),
                    author.get(),
                    receiver.get(),
                    content.get(),
                    isSent.get(),
                    isEdited.get(),
                    read.get(),
                    timestamp.get()
            );
        };
    }
}
