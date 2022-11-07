package laustrup.bandwichpersistence.models.users;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public abstract class User extends Model {

    @Getter @Setter
    protected String _username,_firstName,_lastName,_password,_email;
    protected String _fullName;
    @Getter
    protected Album _images;
    @Getter
    protected List<Album> _music;
    @Getter
    protected List<Rating> _ratings;
    @Getter
    protected List<Event> _events;
    @Getter
    protected List<ChatRoom> _chatRooms;

    // Constructor for from database
    public User(String id, String username, String title, String firstName, String lastName, String password,
                String email, Album images, List<Album> music, List<Rating> ratings, List<Event> events,
                List<ChatRoom> chatRooms, LocalDateTime timestamp) {
        super(id,title,timestamp);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _password = password;
        _email = email;
        _images = images;
        _music = music;
        _ratings = ratings;
        _events = events;
        _chatRooms = chatRooms;
    }

    // Constructor add to database
    public User(String username, String title, String firstName, String lastName, String password, String email) {
        super(title);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        get_fullName();
        _password = password;
        _email = email;
        _images = new Album();
        _music = new Liszt<>();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
    }

    public String get_fullName() {
        _fullName = _firstName + " " + _lastName;
        return _fullName;
    }

    // Add methods
    public Album add(String url) {
        _images.add(url);
        return _images;
    }
    public Album add(String url, String albumId) {
        for (int i = 0; i < _music.size(); i++) {
            if (_music.get(i).get_id().equals(albumId)) {
                _music.get(i).add(url);
                return _music.get(i);
            }
        }
        return null;
    }
    public List<Rating> add(Rating rating) {
        if (!_ratings.contains(rating)) {
            _ratings.add(rating);
        } else {
            edit(rating);
        }
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

    // Remove methods
    public Album remove(String url) {
        _images.remove(url);
        return _images;
    }
    public Album remove(String url,String albumId) {
        for (Album album : _music) {
            if (album.get_id().equals(albumId)) {
                album.remove(url);
                return album;
            }
        }
        return null;
    }
    public List<Event> remove(Event event) {
        for (int i = 0; i < _events.size(); i++) {
            if (_events.get(i).get_id().equals(event.get_id())) {
                _events.remove(_events.get(i));
                break;
            }
        }
        return _events;
    }
    public List<ChatRoom> remove(ChatRoom chatRoom) {
        for (int i = 0; i < _chatRooms.size(); i++) {
            if (_chatRooms.get(i).get_id().equals(chatRoom.get_id())) {
                _chatRooms.remove(_chatRooms.get(i));
                break;
            }
        }
        return _chatRooms;
    }

    // Edit methods
    public List<Rating> edit(Rating rating) {
        for (int i = 0; i < _ratings.size(); i++) {
            if (_ratings.get(i).get_ratingId().equals(rating.get_ratingId())) {
                _ratings.set(i,rating);
                break;
            }
        }
        return _ratings;
    }
}
