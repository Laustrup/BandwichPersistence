package laustrup.bandwichpersistence.models.users.sub_users.participants;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor @ToString
public class Participant extends User {

    @Getter
    private Liszt<User> _followings;

    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, LocalDateTime timestamp, Liszt<User> followings) {
        super(id, username, firstName, lastName, description, contactInfo, images, ratings, events, chatRooms, timestamp);
        _followings = followings;
    }

    public Participant(String username, String firstName, String lastName, String description,
                       Liszt<User> followings) {
        super(username, firstName, lastName, description);
        _followings = followings;
    }

    public Liszt<User> add(User following) {
        _followings.add(following);
        return _followings;
    }
}
