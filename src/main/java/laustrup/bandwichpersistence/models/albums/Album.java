package laustrup.bandwichpersistence.models.albums;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public class Album extends Model {

    @Getter
    public Liszt<String> _endpoints;
    @Getter
    public User _author;
    @Getter
    public AlbumKind _type;
    @Getter
    public LocalDateTime _timestamp;

    // Constructor for from database
    public Album(long id, String title, Liszt<String> urls, User author, AlbumKind type, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _endpoints = urls;
        _author = author;
        _type = type;
    }

    // Constructor to add database
    public Album(String title, Liszt<String> endpoints, User author, AlbumKind type) {
        super(title);
        _endpoints = endpoints;
        _author = author;
        _type = type;
    }

    // Add methods
    public List<String> add(String url) {
        _endpoints.add(url);
        return _endpoints;
    }

    // Remove methods
    public List<String> remove(String url) {
        _endpoints.remove(url);
        return _endpoints;
    }
}
