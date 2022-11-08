package laustrup.bandwichpersistence.models.users;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
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

    @Getter @Setter
    protected String _username,_firstName,_lastName, _description, _password;
    @Getter
    protected ContactInfo _contactInfo;
    protected String _fullName;

    /**
     * The amount of time it takes, before the responsible have answered the chatroom,
     * measured from the first message.
     * Is calculated in minutes.
     */
    protected Long _answeringTime;
    @Getter
    protected Album _images;
    @Getter
    protected Liszt<Rating> _ratings;
    @Getter
    protected Liszt<Event> _events;
    @Getter
    protected Liszt<ChatRoom> _chatRooms;

    public User(long id, String username, String firstName, String lastName, String description, String password,
                ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                Liszt<ChatRoom> chatRooms, LocalDateTime timestamp) {
        super(id,username,timestamp);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _description = description;
        _password = password;
        _contactInfo = contactInfo;
        _images = images;
        _ratings = ratings;
        _events = events;
        _chatRooms = chatRooms;
    }

    public User(String username, String firstName, String lastName, String description, String password,
                ContactInfo contactInfo) {
        super(username);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _description = description;
        _password = password;
        _contactInfo = contactInfo;

        _images = new Album();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
    }

    public String get_fullName() {
        _fullName = _firstName + " " + _lastName;
        return _fullName;
    }

    public Album add(String url) {
        _images.add(url);
        return _images;
    }
    public List<Rating> add(Rating rating) {
        if (!_ratings.contains(rating)) _ratings.add(rating);
        else edit(rating);
        return _ratings;
    }
    public List<Event> add(Event event) {
        _events.add(event);
        return _events;
    }
    public List<ChatRoom> add(ChatRoom chatRoom) {
        _chatRooms.add(chatRoom);
        return _chatRooms;
    }

    public Album remove(String url) {
        _images.remove(url);
        return _images;
    }
    public List<Event> remove(Event event) {
        for (int i = 1; i <= _events.size(); i++) {
            if (_events.get(i).get_id() == event.get_id()) {
                _events.remove(_events.get(i));
                break;
            }
        }

        return _events;
    }
    public List<ChatRoom> remove(ChatRoom chatRoom) {
        for (int i = 1; i <= _chatRooms.size(); i++) {
            if (_chatRooms.get(i).get_id() == chatRoom.get_id()) {
                _chatRooms.remove(_chatRooms.get(i));
                break;
            }
        }

        return _chatRooms;
    }

    public List<Rating> edit(Rating rating) {
        for (int i = 1; i <= _ratings.size(); i++) {
            if (_ratings.get(i).get_ratingId() == rating.get_ratingId()) {
                _ratings.set(i,rating);
                break;
            }
        }

        return _ratings;
    }

    public Long get_answeringTime() {
        _answeringTime = TimeService.get_instance().getTotalAnswerTimes(_chatRooms);
        return _answeringTime;
    }
}
