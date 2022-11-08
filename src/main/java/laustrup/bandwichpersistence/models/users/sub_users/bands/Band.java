package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.MusicalUser;
import laustrup.bandwichpersistence.utilities.Liszt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public class Band extends MusicalUser {

    @Getter
    private List<Artist> _members;

    public Band(long id, String username, String firstName, String lastName, String description, String password,
                ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                Liszt<ChatRoom> chatRooms, LocalDateTime timestamp, Liszt<Album> music, List<Artist> members) {
        super(id, username, firstName, lastName, description, password, contactInfo, images, ratings, events, chatRooms, timestamp, music);
        _members = members;
    }

    public Band(String username, String firstName, String lastName, String description, String password,
                ContactInfo contactInfo, Liszt<Album> music, List<Artist> members) {
        super(username, firstName, lastName, description, password, contactInfo, music);
        _members = members;
    }
}
