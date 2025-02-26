package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;

import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Arrays;
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

    private Seszt<Album> _albums;

    private Seszt<Event> _events;

    private Seszt<ChatRoom> _chatRooms;

    private Seszt<Post> _posts;

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
        super(venue);

        _location = new ContactInfo.Address(venue.getLocation());

        _stageSetup = venue.getGearDescription();
        _size = venue.getSize();
    }

    public Venue(
            UUID id,
            String title,
            String description,
            Seszt<Album> albums,
            Seszt<Event> events,
            Seszt<ChatRoom> chatRooms,
            ContactInfo.Address location,
            String stageSetup,
            Seszt<Post> posts,
            int size,
            History history,
            Instant timestamp
    ) {
        super(id, title, history, timestamp);
        _description = description;
        _albums = albums;
        _events = events;
        _chatRooms = chatRooms;
        _location = location;
        _stageSetup = stageSetup;
        _posts = posts;
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

        private Set<Album.DTO> albums;

        private Set<Event.DTO> events;

        private Set<ChatRoom.DTO> chatRooms;

        private Set<Post.DTO> posts;

        /**
         * The description of the gear that the Venue posses.
         */
        private String gearDescription;

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
            albums = Arrays.stream(venue.get_albums().get_data()).map(Album.DTO::new).collect(Collectors.toSet());
            events = Arrays.stream(venue.get_events().get_data()).map(Event.DTO::new).collect(Collectors.toSet());
            chatRooms = Arrays.stream(venue.get_chatRooms().get_data()).map(ChatRoom.DTO::new).collect(Collectors.toSet());
            posts = Arrays.stream(venue.get_posts().get_data()).map(Post.DTO::new).collect(Collectors.toSet());

            gearDescription = venue.get_stageSetup();
            size = venue.get_size();

        }
    }
}
