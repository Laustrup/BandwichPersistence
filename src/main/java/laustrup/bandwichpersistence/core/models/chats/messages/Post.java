package laustrup.bandwichpersistence.core.models.chats.messages;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.services.ModelService;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.from;

/**
 * A kind of post that can be posted at any Model Object.
 */
@Getter
public class Post extends MessageBase {

    /**
     * The Model receiver that are having the Bulletin posted at its dashboard.
     */
    public Model _receiver;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param post The transport object to be transformed.
     */
    public Post(DTO post) {
        super(post);
        _receiver = from(post);
    }

    public Post(
            UUID id,
            User author,
            Model receiver,
            String content,
            Instant isSent,
            boolean isEdited,
            Instant read,
            Instant timestamp
    ) {
        super(id, author, content, isSent, isEdited, read, timestamp);
        _receiver = receiver;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._id,
                MessageBase.Fields._content,
                MessageBase.Fields._sent,
                MessageBase.Fields._edited,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_id),
                _content,
                String.valueOf(_sent),
                String.valueOf(_edited),
                String.valueOf(_timestamp)
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @FieldNameConstants
    public static class DTO extends MessageBase.DTO {

        /**
         * The Model that are receiving this Bulletin.
         */
        public ModelDTO receiver;

        /**
         * Converts into this DTO Object.
         * @param post The Object to be converted.
         */
        public DTO(Post post) {
            super(post);
            receiver = ModelService.from(post.get_receiver());
        }
    }
}
