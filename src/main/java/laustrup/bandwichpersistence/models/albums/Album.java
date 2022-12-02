package laustrup.bandwichpersistence.models.albums;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Album extends Model {

    /**
     * These endpoints are being used for getting the image/music file.
     */
    @Getter
    private Liszt<String> _endpoints;

    /**
     * The main author of the current Album.
     * Is meant for if there is a Band, then the Band is the first mention
     * or if there is an image, then multiple people might be on the image,
     * but there will then be only one who made the upload.
     */
    @Getter
    private User _author;

    /**
     * Categories the tagged people, who have participated on the album.
     */
    @Getter
    private Liszt<User> _tags;

    /**
     * An Album can have a relation to an Event, but doesn't necessarily have to.
     */
    @Getter @Setter
    private Event _event;

    /**
     * This is an Enum.
     * The Album might either be a MUSIC or IMAGE Album.
     */
    @Getter
    private Kind _kind;

    public Album(long id, String title, Liszt<String> urls, User author, Liszt<User> tags, Event event,
                 Kind kind, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _endpoints = urls;
        _author = author;
        _tags = tags;
        _event = event;
        _kind = kind;
    }

    public Album(String title, Liszt<String> endpoints, User author, Liszt<User> tags, Event event, Kind kind) {
        super(title);
        _endpoints = endpoints;
        _author = author;
        _tags = tags;
        _event = event;
        _kind = kind;
    }

    public Model setAuthor(User author) {
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
     * Will add a User as a tag to the Album.
     * @param tag The User that will be added as a tag.
     * @return All the tags of the Album.
     */
    public Liszt<User> add(User tag) { return add(new User[]{tag}); }

    /**
     * Will add some Users as tags to the Album.
     * @param tags The Users that will be added as tags.
     * @return All the tags of the Album.
     */
    public Liszt<User> add(User[] tags) {
        _tags.add(tags);
        return _tags;
    }

    /**
     * Removes a tagged User of the Album.
     * @param tag The User that will be removed as a tag.
     * @return All the tags of the Album.
     */
    public Liszt<User> remove(User tag) {
        _tags.remove(tag);
        return _tags;
    }

    /**
     * Removes an endpoint of the Album.
     * @param endpoint Will be used for getting the file of the content.
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
