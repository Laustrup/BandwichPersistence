package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.sub_users.MusicalUser;
import laustrup.bandwichpersistence.utilities.Liszt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public class Artist extends MusicalUser {

    @Getter
    private List<Band> _bands;

    public Artist(long id, String username, String firstName, String lastName, String password, String email, Album images,
                  Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, LocalDateTime timestamp,
                  Liszt<Album> music, Liszt<Band> bands) {
        super(id, username, firstName, lastName, password, email, images, ratings, events, chatRooms, timestamp, music);
        _bands = bands;
    }

    public Artist(String username, String firstName, String lastName, String password, String email,
                  Liszt<Album> music, Liszt<Band> bands) {
        super(username, firstName, lastName, password, email, music);
        _bands = bands;
    }

    public List<Band> addBand(Band band) {
        _bands.add(band);
        return _bands;
    }
    public List<Band> addBands(List<Band> bands) {
        _bands.addAll(bands);
        return _bands;
    }

    public List<Band> removeBands(Band bands) {
        _bands.remove(bands);
        return _bands;
    }
    public List<Band> removeBands(List<Band> bands) {
        _bands.removeAll(bands);
        return _bands;
    }
}
