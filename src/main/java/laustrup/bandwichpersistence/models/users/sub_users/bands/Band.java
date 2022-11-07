package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.sub_users.MusicalUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public class Band extends MusicalUser {

    @Getter
    private List<Artist> _members;

    public Band(long id, String username, String firstName, String lastName, String password, String email, Album images,
                List<Rating> ratings, List<Event> events, List<ChatRoom> chatRooms, LocalDateTime timestamp,
                List<Album> music, List<Artist> members) {
        super(id, username, firstName, lastName, password, email, images, ratings, events, chatRooms, timestamp, music);
        _members = members;
    }

    public Band(String username, String firstName, String lastName, String password, String email, List<Album> music, List<Artist> members) {
        super(username, firstName, lastName, password, email, music);
        _members = members;
    }
}
