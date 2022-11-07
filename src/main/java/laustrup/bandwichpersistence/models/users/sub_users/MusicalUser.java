package laustrup.bandwichpersistence.models.users.sub_users;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public abstract class MusicalUser extends User {

    @Getter
    protected List<Album> _music;

    public MusicalUser(long id, String username, String firstName, String lastName, String description, String password,
                       String email, Album images, Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms,
                       LocalDateTime timestamp, Liszt<Album> music) {
        super(id, username, firstName, lastName, description, password, email, images, ratings, events, chatRooms, timestamp);
        _music = music;
    }

    public MusicalUser(String username, String firstName, String lastName, String description, String password,
                       String email, Liszt<Album> music) {
        super(username, firstName, lastName, description, password, email);
        _music = music;

        _images = new Album();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
    }

    public Album add(String url, long albumId) {
        for (int i = 1; i <= _music.size(); i++) {
            if (_music.get(i).get_id() == albumId) {
                _music.get(i).add(url);
                return _music.get(i);
            }
        }
        return null;
    }

    public Album remove(String url, long albumId) {
        for (Album album : _music) {
            if (album.get_id() == albumId) {
                album.remove(url);
                return album;
            }
        }
        return null;
    }
}
