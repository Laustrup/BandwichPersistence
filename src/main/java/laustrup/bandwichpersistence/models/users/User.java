package laustrup.bandwichpersistence.models.users;

import laustrup.bandwichpersistence.models.dtos.RatingDTO;
import laustrup.bandwichpersistence.models.dtos.albums.AlbumDTO;
import laustrup.bandwichpersistence.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.BulletinDTO;
import laustrup.bandwichpersistence.models.dtos.events.EventDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.services.TimeService;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * An abstract class, which is meant to be extended to a user type of object.
 * It extends from Model class.
 * Can calculate full name from first- and last name.
 */
public abstract class User extends Model {

    /**
     * The title of the user, that the user uses to use as a title for the profile.
     */
    @Getter @Setter
    protected String _username;

    /**
     * The real first name of the user's name.
     */
    @Getter @Setter
    protected String _firstName;

    /**
     * The real last name of the user's name.
     */
    @Getter @Setter
    protected String _lastName;

    /**
     * The real full name of the user's name.
     * Is generated by first- and last name.
     */
    protected String _fullName;

    /**
     * This is what the user uses to describe itself.
     */
    @Getter @Setter
    protected String _description;

    /**
     * An object that has the different attributes,
     * that can be used to contact this user.
     */
    @Getter
    protected ContactInfo _contactInfo;

    /**
     * The amount of time it takes, before the responsible have answered the chatroom,
     * measured from the first message.
     * Is calculated in minutes.
     */
    protected Long _answeringTime;

    /**
     * An album consisting of images.
     */
    @Getter
    protected Liszt<Album> _albums;

    /**
     * Ratings made from other users on this user based on a value.
     */
    @Getter
    protected Liszt<Rating> _ratings;

    /**
     * The Events that this user is included in.
     */
    @Getter
    protected Liszt<Event> _events;

    /**
     * These ChatRooms can be used to communicate with other users.
     */
    @Getter
    protected Liszt<ChatRoom> _chatRooms;

    /**
     * This subscription defines details of subscription,
     * including its status.
     * Only Artists and Bands can have a premium membership,
     * since they are the only paying users.
     */
    @Getter
    protected Subscription _subscription;

    /**
     * Messages by other Users.
     */
    @Getter
    protected Liszt<Bulletin> _bulletins;

    @Getter
    protected Authority _authority;

    public User(long id, String username, String firstName, String lastName,
                String description, ContactInfo contactInfo, AlbumDTO[] albums,
                RatingDTO[] ratings, EventDTO[] events, ChatRoomDTO[] chatRooms,
                Subscription subscription, BulletinDTO[] bulletins, Authority authority, LocalDateTime timestamp) {
        this(id, username, description, contactInfo, albums, ratings, events, chatRooms, subscription, bulletins, authority, timestamp);
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
    }
    public User(long id, String username, String description, ContactInfo contactInfo,
    AlbumDTO[] albums, RatingDTO[] ratings, EventDTO[] events, ChatRoomDTO[] chatRooms,
    Subscription subscription, BulletinDTO[] bulletins, Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id, timestamp);
        _username = username;
        _contactInfo = contactInfo;
        _description = description;

        _albums = new Liszt<>();
        for (AlbumDTO album : albums)
        _albums.add(new Album(album));

        _ratings = new Liszt<>();
        for (RatingDTO rating : ratings)
        _ratings.add(new Rating(rating));

        _events = new Liszt<>();
        for (EventDTO event : events)
        _events.add(new Event(event));

        _chatRooms = new Liszt<>();
        for (ChatRoomDTO chatRoom : chatRooms)
        _chatRooms.add(new ChatRoom(chatRoom));

        _subscription = subscription;

        _bulletins = new Liszt<>();
        for (BulletinDTO bulletin : bulletins)
        _bulletins.add(new Bulletin(bulletin));

        _authority = authority;
    }
    public User(UserDTO user) {
        super(user.getPrimaryId(),user.getUsername() + "-" + user.getPrimaryId(),user.getTimestamp());
        _username = user.getUsername();
        _firstName = user.getFirstName();
        _lastName = user.getLastName();
        get_fullName();
        _contactInfo = new ContactInfo(user.getContactInfo());
        _description = user.getDescription();

        _albums = new Liszt<>();
        for (AlbumDTO album : user.getAlbums())
            _albums.add(new Album(album));

        _ratings = new Liszt<>();
        for (RatingDTO rating : user.getRatings())
            _ratings.add(new Rating(rating));

        _events = new Liszt<>();
        for (EventDTO event : user.getEvents())
            _events.add(new Event(event));

        _chatRooms = new Liszt<>();
        for (ChatRoomDTO chatRoom : user.getChatRooms())
            _chatRooms.add(new ChatRoom(chatRoom));

        _subscription = new Subscription(user.getSubscription());

        _bulletins = new Liszt<>();
        for (BulletinDTO bulletin : user.getBulletins())
            _bulletins.add(new Bulletin(bulletin));

        _authority = Authority.valueOf(user.getAuthority().toString());
    }
    public User(long id) {
        super(id);
    }
    public User(long id, String username, String firstName, String lastName, String description,
                ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                Liszt<ChatRoom> chatRooms, Subscription subscription,
                Liszt<Bulletin> bulletins, Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _contactInfo = contactInfo;
        _description = description;
        _albums = albums;
        _ratings = ratings;
        _events = events;
        _chatRooms = chatRooms;
        _subscription = subscription;
        _bulletins = bulletins;
        _authority = authority;
    }

    public User(long id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, Subscription subscription,
                Liszt<Bulletin> bulletins, Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        _username = username;
        _contactInfo = contactInfo;
        _description = description;
        _albums = albums;
        _ratings = ratings;
        _events = events;
        _chatRooms = chatRooms;
        _subscription = subscription;
        _bulletins = bulletins;
        _authority = authority;
    }

    public User(String username, String firstName, String lastName, String description,
                Subscription subscription, Authority authority) {
        super(username);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _description = description;

        _albums = new Liszt<>();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
        _bulletins = new Liszt<>();

        _subscription = subscription;
        _authority = authority;
    }

    public User(String username, String description, Subscription subscription, Authority authority) {
        super(username);
        _username = username;
        _description = description;

        _albums = new Liszt<>();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
        _bulletins = new Liszt<>();

        _subscription = subscription;
        _authority = authority;
    }

    /**
     * Sets the Events.
     * Will only be done, if it is under assembling.
     * @return The Events of this User.
     */
    public Liszt<Event> set_events(Liszt<Event> events) {
        if (_assembling)
            _events = events;
        return _events;
    }

    public Liszt<Bulletin> set_bulletinReceivers() {
        if (_assembling) {
            for (int i = 1; i <= _bulletins.size(); i++) {
                _bulletins.get(i).set_reciever(this);
                _bulletins.get(i).doneAssembling();
            }
        }
        return _bulletins;
    }

    /**
     * Sets the User of the Subscription as this User.
     * Will only be done, if it is under assembling.
     * @return The Subscription of this User.
     */
    public Subscription setSubscriptionUser() {
        if (_assembling)
            _subscription.set_user(this);
        return _subscription;
    }

    /**
     * Sets the ChatRooms.
     * Will only be done, if it is under assembling.
     * @return The ChatRooms of this User.
     */
    public Liszt<ChatRoom> set_chatRooms(Liszt<ChatRoom> chatRooms) {
        if (_assembling)
            _chatRooms = chatRooms;
        return _chatRooms;
    }

    /**
     * Sets the author of the Albums as this User.
     * Is meant for after assembling.
     * @return The images of this User.
     */
    public Liszt<Album> setAlbumsAuthor() {
        if (_assembling)
            for (int i = 1; i <= _albums.size(); i++)
                _albums.get(i).setAuthor(this);
        return _albums;
    }

    /**
     * Sets the full name from first- and last name with a white space between.
     * @return The calculated full name.
     */
    public String get_fullName() {
        _fullName = _firstName + " " + _lastName;
        return _fullName;
    }

    /**
     * Sets the status of the subscription.
     * @param status The status that is wished to be set as the status of the Subscription.
     * @return The Subscription of the User.
     */
    public Subscription changeSubscriptionStatus(Subscription.Status status) {
        _subscription.set_status(status);
        return _subscription;
    }

    /**
     * Will add a Rating to this User.
     * @param rating A Rating object, that is wished to be added to this User.
     * @return All the Ratings of this User.
     */
    public Liszt<Rating> add(Rating rating) {
        if (!_ratings.contains(rating)) _ratings.add(rating);
        else edit(rating);
        return _ratings;
    }

    /**
     * Will add an Event to this User.
     * @param event An Event object, that is wished to be added to this User.
     * @return All the Events of this User.
     */
    public Liszt<Event> add(Event event) {
        _events.add(event);
        return _events;
    }

    /**
     * Will add a ChatRoom to this User.
     * @param chatRoom A ChatRoom object, that is wished to be added to this User.
     * @return All the ChatRooms of this User.
     */
    public Liszt<ChatRoom> add(ChatRoom chatRoom) {
        _chatRooms.add(chatRoom);
        return _chatRooms;
    }

    /**
     * Removes an Event of this User.
     * @param event An Event object, that is wished to be added to this User.
     * @return All the Events of this User.
     */
    public List<Event> remove(Event event) {
        for (int i = 1; i <= _events.size(); i++) {
            if (_events.get(i).get_primaryId() == event.get_primaryId()) {
                _events.remove(_events.get(i));
                break;
            }
        }

        return _events;
    }

    /**
     * Removes an ChatRoom of this User.
     * @param chatRoom An ChatRoom object, that is wished to be added to this User.
     * @return All the ChatRooms of this User.
     */
    public List<ChatRoom> remove(ChatRoom chatRoom) {
        for (int i = 1; i <= _chatRooms.size(); i++) {
            if (_chatRooms.get(i).get_primaryId() == chatRoom.get_primaryId()) {
                _chatRooms.remove(_chatRooms.get(i));
                break;
            }
        }

        return _chatRooms;
    }

    /**
     * Edits a Rating of this User.
     * @param rating An updated Rating object, that is wished to be set as the new Rating of this User.
     * @return All the Ratings of this User.
     */
    public Liszt<Rating> edit(Rating rating) {
        for (int i = 1; i <= _ratings.size(); i++) {
            if (_ratings.get(i).get_primaryId() == rating.get_primaryId()) {
                _ratings.set(i,rating);
                break;
            }
        }

        return _ratings;
    }

    /**
     * Uses TimeService to calculate the total answering time of all ChatRooms from this User.
     * @return The total answering time in seconds, that has been calculated.
     */
    public Long get_answeringTime() {
        _answeringTime = TimeService.get_instance().getTotalAnswerTimes(_chatRooms);
        return _answeringTime;
    }

    public enum Authority {
        VENUE,
        ARTIST,
        BAND,
        PARTICIPANT
    }
}
