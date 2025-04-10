package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.Participant;
import laustrup.bandwichpersistence.core.services.UserService;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Extends performer and contains Artists as members
 */
@Getter @FieldNameConstants
public class Band extends Model {

    private String _description;

    private Subscription _subscription;

    private Seszt<Album> _albums;

    private Seszt<Event> _events;

    private Seszt<User> _fans;

    private Seszt<Post> _posts;

    private String _name;

    private String _runner;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param band The transport object to be transformed.
     */
    public Band(Band.DTO band) {
        this(
                band.getId(),
                band.getName(),
                band.getDescription(),
                new Seszt<>(band.getAlbums().stream().map(Album::new)),
                new Seszt<>(band.getEvents().stream().map(Event::new)),
                new Subscription(band.getSubscription()),
                new Seszt<>(band.getPosts().stream().map(Post::new)),
                band.getRunner(),
                new Seszt<>(band.getFans().stream().map(UserService::from)),
                band.getTimestamp()
        );
    }

    public Band(
            UUID id,
            String name,
            String description,
            Seszt<Album> albums,
            Seszt<Event> events,
            Subscription subscription,
            Seszt<Post> posts,
            String runner,
            Seszt<User> fans,
            Instant timestamp
    ) {
        super(id, name + "|" + id, timestamp);

        _name = name;
        _description = description;
        _albums = albums;
        _events = events;
        _fans = fans;
        _subscription = subscription;
        _posts = posts;
        _runner = runner;
    }

    /**
     * Removes a Fan of the Liszt of fans.
     * @param fan An object of Fan, that is wished to be removed.
     * @return The whole Liszt of fans.
     */
    public Seszt<User> remove(Participant fan) {
        return _fans.remove(new Participant[]{fan});
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._id,
                User.Fields._username,
                User.Fields._description,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_id()),
                get_name(),
                get_description(),
                Model.Fields._timestamp
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
@Getter @Setter@FieldNameConstants @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO extends ModelDTO {


        private String description;

        private Subscription.DTO subscription;

        private Set<Album.DTO> albums;

        private Set<Event.DTO> events;

        private Set<User.UserDTO> fans;

        private Set<Post.DTO> posts;

        private String name;

        private String runner;

        /**
         * Converts into this DTO Object.
         * @param band The Object to be converted.
         */
        public DTO(Band band) {
            super(band);
            description = band.get_description();
            subscription = new Subscription.DTO(band.get_subscription());
            albums = Arrays.stream(band.get_albums().get_data())
                    .map(Album.DTO::new)
                    .collect(Collectors.toSet());
            events = Arrays.stream(band.get_events().get_data())
                    .map(Event.DTO::new)
                    .collect(Collectors.toSet());
            fans = Arrays.stream(band.get_fans().get_data())
                    .map(UserService::from)
                    .collect(Collectors.toSet());
            posts = Arrays.stream(band.get_posts().get_data())
                    .map(Post.DTO::new)
                    .collect(Collectors.toSet());

            name = band.get_name();
            runner = band.get_runner();
        }
    }

    @Getter
    public static class Membership {

        private Artist _member;

        private Association _association;

        public Membership(DTO membership) {
            _member = new Artist(membership.getMember());
            _association = membership.getAssociation();
        }

        public Membership(Artist member, Association association) {
            _member = member;
            _association = association;
        }

        public enum Association {
            SESSION,
            OWNER
        }

        @Getter @FieldNameConstants @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DTO {

            private Artist.DTO member;

            private Association association;

            public DTO(Membership membership) {
                member = new Artist.DTO(membership.get_member());
                association = membership.get_association();
            }
        }
    }
}
