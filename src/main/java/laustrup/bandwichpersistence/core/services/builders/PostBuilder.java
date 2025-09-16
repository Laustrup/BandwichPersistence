package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.chats.messages.Message;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;

import java.util.UUID;

public class PostBuilder extends BuilderService<Post> {

    private static PostBuilder _instance;

    public static PostBuilder get_instance() {
        if (_instance == null)
            _instance = new PostBuilder();

        return _instance;
    }

    private PostBuilder() {

    }

    @Override
    protected void completion(Post reference, Post object) {

    }

    @Override
    protected Post construct() {
        return new Post(
                new Post.Id((UUID) get_field(Model.Fields._identity)),
                get_field(Message.Fields._author),
                get_field(Post.Fields._receiver),
                get_field(Message.Fields._content),
                get_field(Message.Fields._sent),
                get_field(Message.Fields._edited),
                get_field(Message.Fields._read),
                get_field(Model.Fields._timestamp)
        );
    }
}
