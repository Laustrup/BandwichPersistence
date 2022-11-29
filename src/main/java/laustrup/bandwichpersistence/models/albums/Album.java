package laustrup.bandwichpersistence.models.albums;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Album extends Model {

    /**
     * These endpoints are being used for getting the image/music file.
     */
    @Getter
    public Liszt<String> _endpoints;

    /**
     * The main author of the current Album.
     * Is meant for if there is a Band, then the Band is the first mention
     * or if there is an image, then multiple people might be on the image,
     * but there will then be only one who made the upload.
     */
    @Getter
    public Model _author;

    /**
     * Categories other mentionable people, who have participated in the album.
     */
    @Getter
    public Liszt<Model> _coAuthors;

    /**
     * This is an Enum.
     * The Album might either be a MUSIC or IMAGE Album.
     */
    @Getter
    public Kind _kind;

    public Album(long id, String title, Liszt<String> urls, User author, Liszt<Model> coAuthors,
                 Kind kind, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _endpoints = urls;
        _author = author;
        _coAuthors = coAuthors;
        _kind = kind;
    }

    public Album(String title, Liszt<String> endpoints, User author, Liszt<Model> coAuthors, Kind kind) {
        super(title);
        _endpoints = endpoints;
        _author = author;
        _coAuthors = coAuthors;
        _kind = kind;
    }

    public Album(String title, Kind kind) {
        super(title);
        _endpoints = new Liszt<>();
        _kind = kind;
    }

    public Model setAuthor(Model author) {
        if (_author==null)
            _author = author;

        return _author;
    }

    /**
     * Will add an endpoint to the Album.
     * @param endpoint will be used for getting the file of the content.
     * @return All the endpoints of the Album.
     */
    public Liszt<String> add(String endpoint) { return add(new String[]{endpoint}); }

    /**
     * Will add endpoints to the Album.
     * @param endpoints will be used for getting the files of the contents.
     * @return All the endpoints of the Album.
     */
    public Liszt<String> add(String[] endpoints) {
        _endpoints.add(endpoints);
        return _endpoints;
    }

    /**
     * Removes an endpoint of the Album.
     * @param endpoint will be used for getting the file of the content.
     * @return All the endpoints of the Album.
     */
    public Liszt<String> remove(String endpoint) {
        _endpoints.remove(endpoint);
        return _endpoints;
    }

    @Override
    public String toString() {
        return "Album(id:"+_primaryId+
                ",title:"+_title+
                ",kind:"+_kind+
                "timestamp:"+_timestamp+
                ")";
    }

    /**
     * An enum that will describe the type of Album.
     */
    public enum Kind { IMAGE,MUSIC; }
}
