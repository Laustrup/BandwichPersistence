package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@Getter @FieldNameConstants
public class Venue extends Model {

    /**
     * The location that the Venue is located at, which could be an address or simple a place.
     */
    @Setter
    private ContactInfo.Address _location;

    private String _description;

    private Seszt<Organisation> _organisations;

    private Seszt<Album> _albums;

    private Seszt<Post> _posts;

    private Seszt<Rating> _ratings;

    /**
     * The description of the gear that the Venue posses.
     * Kind of the opposite of a runner.
     */
    @Setter
    private String _stageSetup;

    /**
     * The size of the stage and room, that Events can be held at.
     */
    @Setter
    private int _size;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param venue The transport object to be transformed.
     */
    public Venue(DTO venue) {
        this(
                venue.getId(),
                venue.getTitle(),
                venue.getDescription(),
                new Seszt<>(venue.getOrganisations().stream().map(Organisation::new)),
                new Seszt<>(venue.getAlbums().stream().map(Album::new)),
                new ContactInfo.Address(venue.getLocation()),
                venue.getStageSetup(),
                new Seszt<>(venue.getPosts().stream().map(Post::new)),
                new Seszt<>(venue.getRatings().stream().map(Rating::new)),
                venue.getSize(),
                venue.getHistory(),
                venue.getTimestamp()
        );
    }

    public Venue(
            UUID id,
            String title,
            String description,
            Seszt<Organisation> organisations,
            Seszt<Album> albums,
            ContactInfo.Address location,
            String stageSetup,
            Seszt<Post> posts,
            Seszt<Rating> ratings,
            int size,
            History history,
            Instant timestamp
    ) {
        super(id, title, history, timestamp);
        _description = description;
        _organisations = organisations;
        _albums = albums;
        _location = location;
        _stageSetup = stageSetup;
        _posts = posts;
        _ratings = ratings;
        _size = size;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
                new String[] {
                    Model.Fields._id,
                    Fields._location,
                    Fields._stageSetup,
                    Model.Fields._timestamp
                },
                new String[] {
                    String.valueOf(get_id()),
                    get_location().toString(),
                    get_stageSetup(),
                    String.valueOf(get_timestamp())
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
         * The location that the Venue is located at, which could be an address or simple a place.
         */
        private ContactInfo.Address.DTO location;

        private String description;

        private Set<Organisation.DTO> organisations;

        private Set<Album.DTO> albums;

        private Set<Post.DTO> posts;

        private Set<Rating.DTO> ratings;

        /**
         * The description of the gear that the Venue posses.
         */
        private String stageSetup;

        /**
         * The size of the stage and room, that Events can be held at.
         */
        private int size;

        /**
         * Converts into this DTO Object.
         * @param venue The Object to be converted.
         */
        public DTO(Venue venue) {
            super(venue);

            location = new ContactInfo.Address.DTO(venue.get_location());
            description = venue.get_description();
            organisations = venue.get_organisations().stream().map(Organisation.DTO::new).collect(Collectors.toSet());
            albums = venue.get_albums().stream().map(Album.DTO::new).collect(Collectors.toSet());
            posts = venue.get_posts().stream().map(Post.DTO::new).collect(Collectors.toSet());
            ratings = venue.get_ratings().stream().map(Rating.DTO::new).collect(Collectors.toSet());
            stageSetup = venue.get_stageSetup();
            size = venue.get_size();
        }
    }

    @Getter
    public static class Rating extends laustrup.bandwichpersistence.core.models.Rating {

        private Organisation _organisation;

        public Rating(DTO rating) {
            this(
                    rating.getValue(),
                    rating.getAppointedId(),
                    rating.getReviewerId(),
                    rating.getComment(),
                    new Organisation(rating.getOrganisation()),
                    rating.getTimestamp()
            );
        }

        public Rating(
                int value,
                UUID appointedId,
                UUID reviewerId,
                String comment,
                Organisation organisation,
                Instant timestamp
        ) {
            super(value, appointedId, reviewerId, comment, timestamp);
            _organisation = organisation;
        }

        @Getter
        public static class DTO extends laustrup.bandwichpersistence.core.models.Rating.DTO {

            private Organisation.DTO organisation;

            public DTO(Rating rating) {
                super(rating);
                organisation = new Organisation.DTO(rating.get_organisation());
            }
        }
    }
}
