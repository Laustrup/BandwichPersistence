package laustrup.bandwichpersistence.models.albums;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;
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
    public Liszt<String> _endpoints;
    @Getter
    public User _author;
    @Getter
    public AlbumKind _type;
    @Getter
    public LocalDateTime _timestamp;

    // Constructor for from database
    public Album(long id, String title, Liszt<String> urls, User author, AlbumKind type, LocalDateTime timestamp) {
        _id = id;
        _title = title;
        _endpoints = urls;
        _author = author;
        _type = type;
        _timestamp = timestamp;
    }

    // Constructor to add database
    public Album(String title, Liszt<String> endpoints, User author, AlbumKind type) {
        _title = title;
        _endpoints = endpoints;
        _author = author;
        _type = type;
        _timestamp = LocalDateTime.now();
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
