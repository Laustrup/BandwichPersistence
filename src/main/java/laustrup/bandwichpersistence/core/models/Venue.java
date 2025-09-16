package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Set;

import static laustrup.bandwichpersistence.core.utilities.collections.Seszt.copy;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@Getter @FieldNameConstants
public class Venue extends Model<Venue.Id, Signature.UUID> {

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

    private Seszt<String> _areas;

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
                new Id(venue.getId()),
                venue.getTitle(),
                venue.getDescription(),
                copy(venue.getOrganisations(),Organisation::new),
                copy(venue.getAlbums(), Album::new),
                new ContactInfo.Address(venue.getLocation()),
                venue.getStageSetup(),
                copy(venue.getPosts(), Post::new),
                copy(venue.getRatings(), Rating::new),
                copy(venue.getAreas(), area -> area),
                venue.getSize(),
                venue.getTimestamp()
        );
    }

    public Venue(
            Id id,
            String title,
            String description,
            Seszt<Organisation> organisations,
            Seszt<Album> albums,
            ContactInfo.Address location,
            String stageSetup,
            Seszt<Post> posts,
            Seszt<Rating> ratings,
            Seszt<String> areas,
            int size,
            Instant timestamp
    ) {
        super(id, title, timestamp);
        _description = description;
        _organisations = organisations;
        _albums = albums;
        _location = location;
        _stageSetup = stageSetup;
        _posts = posts;
        _ratings = ratings;
        _areas = areas;
        _size = size;
    }

    public static class Id extends CommonIdentity<Signature.UUID> {

        public Id(Signature.UUID signature) {
            super(signature);
        }

        public Id(java.util.UUID signature) {
            super(new Signature.UUID(signature));
        }

        @Override
        public Class<Venue> getOwnerClassType() {
            return Venue.class;
        }
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
                new String[] {
                    Model.Fields._identity,
                    Fields._location,
                    Fields._stageSetup,
                    Model.Fields._timestamp
                },
                new String[] {
                    String.valueOf(get_identity()),
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
    @Getter @Setter @FieldNameConstants @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO extends ModelDTO<Venue.Id, Signature.UUID, java.util.UUID> {

        /**
         * The location that the Venue is located at, which could be an address or simple a place.
         */
        private ContactInfo.Address.DTO location;

        private String description;

        private Set<Organisation.DTO> organisations;

        private Set<Album.DTO> albums;

        private Set<Post.DTO> posts;

        private Set<Rating.DTO> ratings;

        private Set<String> areas;

        /**
         * The description of the gear that the Venue posses.
         */
        private String stageSetup;

        /**
         * The size of the stage and room, that Events can be held at.
         */
        private int size;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty java.util.UUID id,
                @JsonProperty String title,
                @JsonProperty Instant timestamp,
                @JsonProperty ContactInfo.Address.DTO location,
                @JsonProperty String description,
                @JsonProperty Set<Organisation.DTO> organisations,
                @JsonProperty Set<Album.DTO> albums,
                @JsonProperty Set<Post.DTO> posts,
                @JsonProperty Set<Rating.DTO> ratings,
                @JsonProperty Set<String> areas,
                @JsonProperty String stageSetup,
                @JsonProperty int size
        ) {
            super(id, title, timestamp);
            this.location = location;
            this.description = description;
            this.organisations = organisations;
            this.albums = albums;
            this.posts = posts;
            this.ratings = ratings;
            this.areas = areas;
            this.stageSetup = stageSetup;
            this.size = size;
        }

        /**
         * Converts into this DTO Object.
         * @param venue The Object to be converted.
         */
        public DTO(Venue venue) {
            super(venue);
            location = new ContactInfo.Address.DTO(venue.get_location());
            description = venue.get_description();
            organisations = venue.get_organisations().asSet(Organisation.DTO::new);
            albums = venue.get_albums().asSet(Album.DTO::new);
            posts = venue.get_posts().asSet(Post.DTO::new);
            ratings = venue.get_ratings().asSet(Rating.DTO::new);
            areas = venue.get_areas().asSet();
            stageSetup = venue.get_stageSetup();
            size = venue.get_size();
        }
    }

    @Getter @FieldNameConstants
    public static class Rating extends laustrup.bandwichpersistence.core.models.Rating {

        private Organisation _organisation;

        public Rating(DTO rating) {
            this(
                    rating.getValue(),
                    new User.Id(rating.getAppointedId()),
                    new User.Id(rating.getReviewerId()),
                    rating.getComment(),
                    new Organisation(rating.getOrganisation()),
                    rating.getTimestamp()
            );
        }

        public Rating(
                int value,
                User.Id appointedId,
                User.Id reviewerId,
                String comment,
                Organisation organisation,
                Instant timestamp
        ) {
            super(value, appointedId, reviewerId, comment, timestamp);
            _organisation = organisation;
        }

        @Getter @FieldNameConstants
        public static class DTO extends laustrup.bandwichpersistence.core.models.Rating.DTO {

            private Organisation.DTO organisation;

            public DTO(Rating rating) {
                super(rating);
                organisation = new Organisation.DTO(rating.get_organisation());
            }
        }
    }
}
