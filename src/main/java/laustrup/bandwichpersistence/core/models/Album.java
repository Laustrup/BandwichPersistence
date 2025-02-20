package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

/**
 * Contains objects that are describing data of either photos or music.
 * The items contain the link to the file source.
 */
@Getter @FieldNameConstants
public class Album extends Model {

    /**
     * Items containing endpoints that are being used for getting the image/music file.
     */
    private Seszt<Item> _items;

    /**
     * The id of the main author of the current Album.
     * Is meant for if there is a Band, then the Band is the first mention
     * or if there is an image, then multiple people might be on the image,
     * but there will then be only one who made the upload.
     */
    private UUID _authorId;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param album The transport object to be transformed.
     */
    public Album(Album.DTO album) {
        super(album);
        _items = new Seszt<>();
        convert(album.getItems());
        _authorId = album.getAuthorId();
    }

    /**
     * Will translate some transport objects of this object into a construct of this object.
     * @param dtos The transport objects to be transformed.
     * @return The current object of AlbumItems.
     */
    private Seszt<Item> convert(Item.DTO[] dtos) {
        for (Item.DTO item : dtos)
            _items.add(new Item(item));

        return _items;
    }

    /**
     * A constructor with all the values of an Album.
     * @param id The primary id that identifies this unique Object.
     * @param title The title of the Album.
     * @param items The items contained on this Album.
     * @param authorId The creator of the Album.
     * @param history The Events for this object.
     * @param timestamp The date this Album was created.
     */
    public Album(
            UUID id,
            String title,
            Seszt<Item> items,
            UUID authorId,
            History history,
            Instant timestamp
    ) {
        super(id, title, history, timestamp);
        _items = items;
        _authorId = authorId;
    }

    /**
     * This constructor can be used to generate a new Album.
     * The timestamp will be for now.
     * @param title The title of the Album.
     * @param authorId The creator of the Album.
     */
    public Album(
            String title,
            UUID authorId
    ) {
        this(title, new Seszt<>(), authorId);
    }

    /**
     * This constructor can be used to generate a new Album.
     * The timestamp will be for now.
     * @param title The title of the Album.
     * @param items The items contained on this Album.
     * @param authorId The creator of the Album.
     */
    public Album(
            String title,
            Seszt<Item> items,
            UUID authorId
    ) {
        super(title);
        _items = items;
        _authorId = authorId;
    }

    /**
     * Will add an item to the Album.
     * @param item The item that will be added to the Album.
     * @return All the endpoints of the item.
     */
    public Seszt<Item> add(Item item) {
        return add(new Seszt<>(item));
    }

    /**
     * Will add items to the Album.
     * If the item is music and the author is not an Artist or Band, it will not be added.
     * @param items Items of the contents of the Album.
     * @return All the items of the Album.
     */
    public Seszt<Item> add(Item[] items) {
        return add(new Seszt<>(items));
    }

    /**
     * Will add items to the Album.
     * If the item is music and the author is not an Artist or Band, it will not be added.
     * @param items Items of the contents of the Album.
     * @return All the items of the Album.
     */
    public Seszt<Item> add(Seszt<Item> items) {
        _items.addAll(items);
        return _items;
    }

    /**
     * Will add an item to the items of the Album.
     * Will identify by the endpoint, so endpoints can not be set...
     * @param item The item that should be replaced with its common id in items.
     * @return All the items.
     */
    public Seszt<Item> set(Item item) {
        return set(new Seszt<>(new Item[]{item}));
    }

    /**
     * Will set an item of the Album.
     * Will identify by the endpoint, so endpoints can not be set...
     * @param items The Items that will be replaced with its common endpoint in items.
     * @return All the items.
     */
    public Seszt<Item> set(Seszt<Item> items) {
        for (Item item : items) {
            for (int i = 1; i <= _items.size(); i++) {
                if (_items.Get(i).get_endpoint().equals(item.get_endpoint())) {
                    _items.set(i,item);
                    break;
                }
            }
        }

        return _items;
    }

    /**
     * Will remove an item of the Album.
     * @param item The item that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Seszt<Item> remove(Item item) { return remove(new Item[]{item}); }

    /**
     * Will remove some items of the Album.
     * @param items The items that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Seszt<Item> remove(Liszt<Item> items) { return remove(items.get_data()); }

    /**
     * Will remove some items of the Album.
     * @param items The items that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Seszt<Item> remove(Item[] items) {
        return _items.remove(items);
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._primaryId,
                Model.Fields._title,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_primaryId),
                _title,
                String.valueOf(_timestamp)
        });
    }

    /**
     * An item of an album that can be either a photos or music.
     * Has a link to the endpoint of the file source.
     */
    @Getter @FieldNameConstants
    public static class Item extends Model {

        /** Categories the tagged people, who have participated on the item of the album. */
        private Seszt<UUID> _tags;

        /** The endpoint for a URL, that is used to get the file of the item. */
        private String _endpoint;

        /**
         * This is an Enum.
         * The Album might either be a MUSIC or IMAGE Album.
         */
        private Kind _kind;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param albumItem The transport object to be transformed.
         */
        public Item(DTO albumItem) {
            super(albumItem);
            _endpoint = albumItem.getEndpoint();
            _kind = Kind.valueOf(albumItem.getKind().toString());
            _tags = new Seszt<>();
            convert(albumItem.getTags());
        }

        /**
         * Will translate some transport objects of this object into a construct of this object.
         * @param dtos The transport objects to be transformed.
         * @return The current object of tags.
         */
        private Seszt<UUID> convert(UUID[] dtos) {
            Collections.addAll(_tags, dtos);

            return _tags;
        }

        /**
         * Constructor with all values.
         * @param title The title of the item.
         * @param endpoint The endpoint location for the file.
         * @param kind Determines if this is music or an image.
         * @param tags The people joined at this item.
         * @param history The Events for this object.
         * @param timestamp The date this item is posted.
         */
        public Item(
                String title,
                String endpoint,
                Kind kind,
                Seszt<UUID> tags,
                History history,
                Instant timestamp
        ) {
            super(title, history, timestamp);
            _endpoint = endpoint;
            _kind = kind;
            _tags = tags;
        }

        /**
         * For creating a new AlbumItem.
         * Timestamp will be of now.
         * @param title The title of the item.
         * @param endpoint The endpoint location for the file.
         * @param kind Determines if this is music or an image.
         * @param tags The people joined at this item.
         */
        public Item(String title, String endpoint, Kind kind, Seszt<UUID> tags) {
            super(title);
            _endpoint = endpoint;
            _kind = kind;
            _tags = tags;
        }

        /**
         * Will add a User as a tag to the item.
         * @param tag The User that will be added as a tag.
         * @return All the tags of the item.
         */
        public Seszt<UUID> add(UUID tag) {
            return add(new UUID[]{tag});
        }

        /**
         * Will add some Users as tags to the item.
         * @param tags The Users that will be added as tags.
         * @return All the tags of the item.
         */
        public Seszt<UUID> add(UUID[] tags) {
            return _tags.Add(tags);
        }

        /**
         * Removes a tagged User of the item.
         * @param tag The User that will be removed as a tag.
         * @return All the tags of the item.
         */
        public Seszt<UUID> remove(UUID tag) {
            return remove(new UUID[]{tag});
        }

        /**
         * Removes some tagged Users of the item.
         * @param tags The Users that will be removed as tags.
         * @return All the tags of the item.
         */
        public Seszt<UUID> remove(UUID[] tags) {
            return _tags.remove(tags);
        }

        @Override
        public String toString() {
            return defineToString(
                    getClass().getSimpleName(),
                    new String[]{
                            Fields._endpoint,
                            Fields._kind,
                            Model.Fields._timestamp
                    },
                    new String[]{
                            _endpoint,
                            _kind != null ? _kind.toString() : null,
                            _timestamp != null ? _timestamp.toString() : null
                    });
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter
        public static class DTO extends ModelDTO {

            /** Categories the tagged people, who have participated on the item of the album. */
            private UUID[] tags;

            /** The endpoint for a URL, that is used to get the file of the item. */
            private String endpoint;

            /**
             * This is an Enum.
             * The Album might either be a MUSIC or IMAGE Album.
             */
            private DTO.Kind kind;

            /**
             * Converts into this DTO Object.
             * @param item The Object to be converted.
             */
            public DTO(Item item) {
                super(item);
                endpoint = item.get_endpoint();
                kind = DTO.Kind.valueOf(item.get_kind().toString());

                tags = new UUID[item.get_tags().size()];
                for (int i = 0; i < tags.length; i++)
                    tags[i] = item.get_tags().get(i);
            }

            /** An enum that will describe the type of Album. */
            public enum Kind { IMAGE, MUSIC }
        }

        /** An enum that will describe the type of Album. */
        public enum Kind { IMAGE,MUSIC }
    }


    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends ModelDTO {

        /**
         * These endpoints are being used for getting the image/music file.
         */
        private Item.DTO[] items;

        /**
         * The main author of the current Album.
         * Is meant for if there is a Band, then the Band is the first mention
         * or if there is an image, then multiple people might be on the image,
         * but there will then be only one who made the upload.
         */
        private UUID authorId;

        /**
         * Converts into this DTO Object.
         * @param album The Object to be converted.
         */
        public DTO(Album album) {
            super(album);
            items = new Item.DTO[album.get_items().size()];
            for (int i = 0; i < items.length; i++)
                items[i] = new Item.DTO(album.get_items().Get(i+1));
            authorId = album.get_authorId();
        }
    }
}
