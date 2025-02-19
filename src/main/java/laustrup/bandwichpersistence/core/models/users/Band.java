package laustrup.bandwichpersistence.core.models.users;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.services.UserService;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
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

    /**
     * Contains all the Artists, that are members of this band.
     */
    private Seszt<Artist> _members;

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
        super(band);
        _description = band.getDescription();
        _subscription = new Subscription(band.getSubscription());
        _albums = new Seszt<>(band.getAlbums().stream().map(Album::new));
        _events = new Seszt<>(band.getEvents().stream().map(Event::new));
        _fans = new Seszt<>(band.getFans().stream().map(UserService::from));
        _posts = new Seszt<>(band.getPosts().stream().map(Post::new));
        _name = band.getName();
        _runner = band.getRunner();
    }

    public Band(
            UUID id,
            String name,
            String description,
            Seszt<Album> albums,
            Seszt<Event> events,
            Subscription subscription,
            Seszt<Post> posts,
            Seszt<Artist> members,
            String runner,
            Seszt<User> fans,
            History history,
            Instant timestamp
    ) {
        super(id, name + "|" + id, history, timestamp);

        _name = name;
        _description = description;
        _albums = albums;
        _events = events;
        _fans = fans;
        _subscription = subscription;
        _posts = posts;
        _members = members;
        _runner = runner;
    }

    /**
     * Adds an Artist to the Liszt of members.
     * @param artist An object of Artist, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> add(Artist artist) {
        return add(new Artist[]{artist});
    }

    /**
     * Adds Artists to the Liszt of members.
     * @param artists An array of artists, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> add(Artist[] artists) {
        return _members.Add(artists);
    }

    /**
     * Removes an Artist of the Liszt of members.
     * @param artist An object of Artist, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> remove(Artist artist) {
        return remove(new Artist[]{artist});
    }

    /**
     * Removes Artists of the Liszt of members.
     * @param artists An array of artists, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> remove(Artist[] artists) {
        return _members.remove(artists);
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
                Model.Fields._primaryId,
                User.Fields._username,
                User.Fields._description,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_primaryId()),
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
    @Getter @Setter
    public static class DTO extends ModelDTO {

        /**
         * Contains all the Artists, that are members of this band.
         */
        private Set<Artist.DTO> members;

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
            members = Arrays.stream(band.get_members().get_data())
                    .map(Artist.DTO::new)
                    .collect(Collectors.toSet());
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
}
