package laustrup.bandwichpersistence.models.albums;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.dtos.albums.AlbumItemDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.collections.lists.Liszt;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AlbumItem extends Model {

    /**
     * Categories the tagged people, who have participated on the item of the album.
     */
    @Getter
    private Liszt<User> _tags;

    /**
     * The endpoint for a URL, that is used to get the file of the item.
     */
    @Getter
    private String _endpoint;

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

    public AlbumItem(AlbumItemDTO albumItem) {
        super(albumItem.get_title(), albumItem.get_timestamp());
        _endpoint = albumItem.getEndpoint();
        _kind = Kind.valueOf(albumItem.getKind().toString());
        _tags = new Liszt<>();
        convert(albumItem.getTags());
    }
    private Liszt<User> convert(UserDTO[] tags) {
        for (UserDTO tag : tags)
            _tags.add(DTOService.get_instance().convertFromDTO(tag));
        return _tags;
    }
    public AlbumItem(String title, String endpoint, Kind kind, Liszt<User> tags, LocalDateTime timestamp) {
        super(title, timestamp);
        _endpoint = endpoint;
        _kind = kind;
        _tags = tags;
    }

    public AlbumItem(String title, String endpoint, Kind kind, Liszt<User> tags, Event event, LocalDateTime timestamp) {
        super(title, timestamp);
        _endpoint = endpoint;
        _kind = kind;
        _tags = tags;
        _event = event;
    }

    /**
     * Will add a User as a tag to the item.
     * @param tag The User that will be added as a tag.
     * @return All the tags of the item.
     */
    public Liszt<User> add(User tag) { return add(new User[]{tag}); }

    /**
     * Will add some Users as tags to the item.
     * @param tags The Users that will be added as tags.
     * @return All the tags of the item.
     */
    public Liszt<User> add(User[] tags) {
        _tags.add(tags);
        return _tags;
    }

    /**
     * Removes a tagged User of the item.
     * @param tag The User that will be removed as a tag.
     * @return All the tags of the item.
     */
    public Liszt<User> remove(User tag) {
        _tags.remove(tag);
        return _tags;
    }

    /**
     * An enum that will describe the type of Album.
     */
    public enum Kind { IMAGE,MUSIC }

    @Override
    public String toString() {
        return "AlbumItem(" +
                    "id:" + _primaryId +
                    ",endpoint:" + _endpoint +
                ")";
    }
}
