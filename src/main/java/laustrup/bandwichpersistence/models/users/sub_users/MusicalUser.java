package laustrup.bandwichpersistence.models.users.sub_users;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public abstract class MusicalUser extends User {

    @Getter
    private List<Album> _music;

    public MusicalUser(long id, String username, String firstName, String lastName, String password, String email,
                       Album images, List<Rating> ratings, List<Event> events, List<ChatRoom> chatRooms,
                       LocalDateTime timestamp, List<Album> music) {
        super(id, username, firstName, lastName, password, email, images, ratings, events, chatRooms, timestamp);
        _music = music;
    }

    public MusicalUser(String username, String firstName, String lastName, String password, String email, List<Album> music) {
        super(username, firstName, lastName, password, email);
        _music = music;
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
