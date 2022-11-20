package laustrup.bandwichpersistence.models.albums;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor @ToString
public class Album extends Model {

    @Getter
    public Liszt<String> _endpoints;
    @Getter
    public Model _author;
    @Getter
    public Kind _kind;
    @Getter
    public LocalDateTime _timestamp;

    // Constructor for from database
    public Album(long id, String title, Liszt<String> urls, User author, Kind kind, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _endpoints = urls;
        _author = author;
        _kind = kind;
    }

    // Constructor to add database
    public Album(String title, Liszt<String> endpoints, User author, Kind kind) {
        super(title);
        _endpoints = endpoints;
        _author = author;
        _kind = kind;
    }

    // Add methods
    public Liszt<String> add(String endpoint) { return add(new String[]{endpoint}); }

    public Liszt<String> add(String[] endpoints) {
        _endpoints.add(endpoints);
        return _endpoints;
    }

    // Remove methods
    public Liszt<String> remove(String url) {
        _endpoints.remove(url);
        return _endpoints;
    }

    public enum Kind { IMAGE,MUSIC; }
}
