package laustrup.bandwichpersistence.models.dtos.users;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.RatingDTO;
import laustrup.bandwichpersistence.models.dtos.albums.AlbumDTO;
import laustrup.bandwichpersistence.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.BulletinDTO;
import laustrup.bandwichpersistence.models.dtos.events.EventDTO;
import laustrup.bandwichpersistence.models.dtos.users.contact_infos.ContactInfoDTO;
import laustrup.bandwichpersistence.models.dtos.users.subscriptions.SubscriptionDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.collections.Liszt;

import lombok.*;

import java.time.LocalDateTime;

/**
 * An abstract class, which is meant to be extended to a user type of object.
 * It extends from Model class.
 * Can calculate full name from first- and last name.
 */
@NoArgsConstructor @Data
public abstract class UserDTO extends ModelDTO {

    /**
     * The title of the user, that the user uses to use as a title for the profile.
     */
    protected String username;

    /**
     * The real first name of the user's name.
     */
    protected String firstName;

    /**
     * The real last name of the user's name.
     */
    protected String lastName;

    /**
     * The real full name of the user's name.
     * Is generated by first- and last name.
     */
    protected String fullName;

    /**
     * This is what the user uses to describe itself.
     */
    protected String description;

    /**
     * An object that has the different attributes,
     * that can be used to contact this user.
     */
    protected ContactInfoDTO contactInfo;

    /**
     * The amount of time it takes, before the responsible have answered the chatroom,
     * measured from the first message.
     * Is calculated in minutes.
     */
    protected Long answeringTime;

    /**
     * An album consisting of images.
     */
    protected AlbumDTO[] albums;

    /**
     * Ratings made from other users on this user based on a value.
     */
    protected RatingDTO[] ratings;

    /**
     * The Events that this user is included in.
     */
    protected EventDTO[] events;

    /**
     * These ChatRooms can be used to communicate with other users.
     */
    protected ChatRoomDTO[] chatRooms;

    /**
     * This subscription defines details of subscription,
     * including its status.
     * Only Artists and Bands can have a premium membership,
     * since they are the only paying users.
     */
    protected SubscriptionDTO subscription;

    /**
     * Messages by other Users.
     */
    protected BulletinDTO[] bulletins;

    protected Authority authority;

    public UserDTO(long id, String username, String firstName, String lastName, String description,
                   ContactInfoDTO contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                   Liszt<ChatRoom> chatRooms, SubscriptionDTO subscription,
                   Liszt<Bulletin> bulletins, Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        getFullName();
        this.contactInfo = contactInfo;
        this.description = description;
        this.albums = new AlbumDTO[albums.size()];
        for (int i = 0; i < this.albums.length; i++)
            this.albums[i] = new AlbumDTO(albums.get(i+1));
        this.ratings = new RatingDTO[ratings.size()];
        for (int i = 0; i < this.ratings.length; i++)
            this.ratings[i] = new RatingDTO(ratings.get(i+1));
        this.events = new EventDTO[events.size()];
        for (int i = 0; i < this.events.length; i++)
            this.events[i] = new EventDTO(events.get(i+1));
        this.chatRooms = new ChatRoomDTO[chatRooms.size()];
        for (int i = 0; i < this.chatRooms.length; i++)
            this.chatRooms[i] = new ChatRoomDTO(chatRooms.get(i+1));
        this.subscription = subscription;
        this.bulletins = new BulletinDTO[bulletins.size()];
        for (int i = 0; i < this.bulletins.length; i++)
            this.bulletins[i] = new BulletinDTO(bulletins.get(i+1));
        this.authority = authority;
    }

    public UserDTO(long id, String username, String description,
                   ContactInfoDTO contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                   Liszt<ChatRoom> chatRooms, SubscriptionDTO subscription,
                   Liszt<Bulletin> bulletins, Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        this.username = username;
        this.contactInfo = contactInfo;
        this.description = description;
        if (albums != null) {
            this.albums = new AlbumDTO[albums.size()];
            for (int i = 0; i < this.albums.length; i++)
                this.albums[i] = new AlbumDTO(albums.get(i+1));
        }
        if (ratings != null) {
            this.ratings = new RatingDTO[ratings.size()];
            for (int i = 0; i < this.ratings.length; i++)
                this.ratings[i] = new RatingDTO(ratings.get(i+1));
        }
        if (events != null) {
            this.events = new EventDTO[events.size()];
            for (int i = 0; i < this.events.length; i++)
                this.events[i] = new EventDTO(events.get(i+1));
        }
        if (chatRooms != null) {
            this.chatRooms = new ChatRoomDTO[chatRooms.size()];
            for (int i = 0; i < this.chatRooms.length; i++)
                this.chatRooms[i] = new ChatRoomDTO(chatRooms.get(i+1));
        }
        this.subscription = subscription;
        if (bulletins != null) {
            this.bulletins = new BulletinDTO[bulletins.size()];
            for (int i = 0; i < this.bulletins.length; i++)
                this.bulletins[i] = new BulletinDTO(bulletins.get(i+1));
        }
        this.authority = authority;
    }

    //TODO Participant needs description in constructor
    public UserDTO(long id, String username, String firstName, String lastName, ContactInfoDTO contactInfo,
                   Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms,
                   SubscriptionDTO subscription, Liszt<Bulletin> bulletins, User.Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        getFullName();
        this.contactInfo = contactInfo;

        if (albums != null) {
            this.albums = new AlbumDTO[albums.size()];
            for (int i = 0; i < this.albums.length; i++)
                this.albums[i] = new AlbumDTO(albums.get(i+1));
        }
        if (ratings != null) {
            this.ratings = new RatingDTO[ratings.size()];
            for (int i = 0; i < this.ratings.length; i++)
                this.ratings[i] = new RatingDTO(ratings.get(i+1));
        }
        if (events != null) {
            this.events = new EventDTO[events.size()];
            for (int i = 0; i < this.events.length; i++)
                this.events[i] = new EventDTO(events.get(i+1));
        }
        if (chatRooms != null) {
            this.chatRooms = new ChatRoomDTO[chatRooms.size()];
            for (int i = 0; i < this.chatRooms.length; i++)
                this.chatRooms[i] = new ChatRoomDTO(chatRooms.get(i+1));
        }
        this.subscription = subscription;
        if (bulletins != null) {
            this.bulletins = new BulletinDTO[bulletins.size()];
            for (int i = 0; i < this.bulletins.length; i++)
                this.bulletins[i] = new BulletinDTO(bulletins.get(i+1));
        }
        this.authority = authority != null ? Authority.valueOf(authority.toString()) : null;
    }

    /**
     * Sets the full name from first- and last name with a white space between.
     * @return The calculated full name.
     */
    public String getFullName() {
        fullName = firstName + " " + lastName;
        return fullName;
    }

    public enum Authority {
        VENUE,
        ARTIST,
        BAND,
        PARTICIPANT
    }
}
