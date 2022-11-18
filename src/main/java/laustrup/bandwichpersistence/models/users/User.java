package laustrup.bandwichpersistence.models.users;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Message;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.services.TimeService;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * An abstract class, which is meant to be extended to a user type of object.
 * It extends from Model class.
 * Can calculate full name from first- and last name.
 */
@NoArgsConstructor @ToString
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
    protected Album _images;

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
     * All the Messages of this user, both sent and unsent.
     */
    @Getter
    protected Liszt<Message> _messages;

    /**
     * This subscription defines details of subscription,
     * including its status.
     * Only Artists and Bands can have a premium membership,
     * since they are the only paying users.
     */
    @Getter
    protected Subscription _subscription;

    @Getter
    protected Liszt<Bulletin> _bulletins;

    public User(long id, String username, String firstName, String lastName, String description,
                ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                Liszt<ChatRoom> chatRooms, Liszt<Message> messages, Subscription subscription,
                Liszt<Bulletin> bulletins, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _contactInfo = contactInfo;
        _description = description;
        _images = images;
        _ratings = ratings;
        _events = events;
        _chatRooms = chatRooms;
        _messages = messages;
        _subscription = subscription;
        _bulletins = bulletins;
    }

    public User(String username, String firstName, String lastName, String description, Subscription subscription) {
        super(username);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _description = description;

        _images = new Album();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
        _messages = new Liszt<>();
        _bulletins = new Liszt<>();

        _subscription = subscription;
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
     * Adds an endpoint linked to an image file.
     * @param endpoint An endpoint to a link, that contains an image file.
     * @return The whole Album of the User.
     */
    public Album add(String endpoint) {
        _images.add(endpoint);
        return _images;
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
     * Will add a Message to this User.
     * @param message A Message object, that is wished to be added to this User.
     * @return All the Messages of this User.
     */
    public Liszt<Message> add(Message message) {
        _messages.add(message);
        return _messages;
    }

    /**
     * Removes a Message of this User.
     * @param message A Message object, that is wished to be removed from this User.
     * @return All the Messages of this User.
     */
    public Liszt<Message> remove(Message message) {
        _messages.remove(message);
        return _messages;
    }

    /**
     * Removes an endpoint for an image file of this User.
     * @param endpoint An endpoint for a URL containing an image file, that is wished to be removed from this User.
     * @return The whole Album of this User.
     */
    public Album remove(String endpoint) {
        _images.remove(endpoint);
        return _images;
    }

    /**
     * Removes an Event of this User.
     * @param event An Event object, that is wished to be added to this User.
     * @return All the Events of this User.
     */
    public List<Event> remove(Event event) {
        for (int i = 1; i <= _events.size(); i++) {
            if (_events.get(i).get_id() == event.get_id()) {
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
            if (_chatRooms.get(i).get_id() == chatRoom.get_id()) {
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
            if (_ratings.get(i).get_id() == rating.get_id()) {
                _ratings.set(i,rating);
                break;
            }
        }

        return _ratings;
    }

    /**
     * Edits a Message of this User.
     * @param message An updated Message object, that is wished to be set as the new Message of this User.
     * @return All the Messages of this User.
     */
    public Liszt<Message> edit(Message message) {
        for (int i = 1; i <= _messages.size(); i++) {
            if (_messages.get(i).get_id() == message.get_id()) {
                _messages.set(i, message);
                break;
            }
        }

        return _messages;
    }

    /**
     * Uses TimeService to calculate the total answering time of all ChatRooms from this User.
     * @return The total answering time in seconds, that has been calculated.
     */
    public Long get_answeringTime() {
        _answeringTime = TimeService.get_instance().getTotalAnswerTimes(_chatRooms);
        return _answeringTime;
    }
}
