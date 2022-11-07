package laustrup.bandwichpersistence.models.albums;

import laustrup.bandwichpersistence.models.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public class Album {

    @Getter
    public long _id;
    @Getter @Setter
    public String _title;
    @Getter
    public List<String> _urls;
    @Getter
    public User _author;
    @Getter
    public AlbumKind _type;
    @Getter
    public LocalDateTime _timestamp;

    // Constructor for from database
    public Album(long id, String title, List<String> urls, User author, AlbumKind type, LocalDateTime timestamp) {
        _id = id;
        _title = title;
        _urls = urls;
        _author = author;
        _type = type;
        _timestamp = timestamp;
    }

    // Constructor to add database
    public Album(String title, List<String> urls, User author, AlbumKind type) {
        _title = title;
        _urls = urls;
        _author = author;
        _type = type;
        _timestamp = LocalDateTime.now();
    }

    // Add methods
    public List<String> add(String url) {
        _urls.add(url);
        return _urls;
    }

    // Remove methods
    public List<String> remove(String url) {
        _urls.remove(url);
        return _urls;
    }
}
