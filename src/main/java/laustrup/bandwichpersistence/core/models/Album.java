package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Set;
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
    private Seszt<Media> _media;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param album The transport object to be transformed.
     */
    public Album(Album.DTO album) {
        this(
                album.getId(),
                album.getTitle(),
                Seszt.copy(album.getMedia(), Media::new),
                album.getTimestamp()
        );
    }

    /**
     * A constructor with all the values of an Album.
     * @param id The primary id that identifies this unique Object.
     * @param title The title of the Album.
     * @param media The items contained on this Album.
     * @param timestamp The date this Album was created.
     */
    public Album(
            UUID id,
            String title,
            Seszt<Media> media,
            Instant timestamp
    ) {
        super(id, title, timestamp);
        _media = media;
    }

    /**
     * This constructor can be used to generate a new Album.
     * The timestamp will be for now.
     * @param title The title of the Album.
     */
    public Album(String title) {
        this(title, new Seszt<>());
    }

    /**
     * This constructor can be used to generate a new Album.
     * The timestamp will be for now.
     * @param title The title of the Album.
     * @param media The items contained on this Album.
     */
    public Album(
            String title,
            Seszt<Media> media
    ) {
        super(title);
        _media = media;
    }

    /**
     * Will add an item to the Album.
     * @param media The item that will be added to the Album.
     * @return All the endpoints of the item.
     */
    public Seszt<Media> add(Media media) {
        return add(new Seszt<>(media));
    }

    /**
     * Will add items to the Album.
     * If the item is music and the author is not an Artist or Band, it will not be added.
     * @param media Items of the contents of the Album.
     * @return All the items of the Album.
     */
    public Seszt<Media> add(Media[] media) {
        return add(new Seszt<>(media));
    }

    /**
     * Will add items to the Album.
     * If the item is music and the author is not an Artist or Band, it will not be added.
     * @param media Items of the contents of the Album.
     * @return All the items of the Album.
     */
    public Seszt<Media> add(Seszt<Media> media) {
        _media.addAll(media);
        return _media;
    }

    /**
     * Will remove an item of the Album.
     * @param media The item that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Seszt<Media> remove(Media media) { return remove(new Media[]{media}); }

    /**
     * Will remove some items of the Album.
     * @param media The items that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Seszt<Media> remove(Liszt<Media> media) { return remove(media.get_data()); }

    /**
     * Will remove some items of the Album.
     * @param media The items that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Seszt<Media> remove(Media[] media) {
        return _media.remove(media);
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._id,
                Model.Fields._title,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_id),
                _title,
                String.valueOf(_timestamp)
        });
    }

    /**
     * An item of an album that can be either a photos or music.
     * Has a link to the endpoint of the file source.
     */
    @Getter @FieldNameConstants
    public static class Media extends Model {

        /** The endpoint for a URL, that is used to get the file of the item. */
        private String _endpoint;

        /**
         * This is an Enum.
         * The Album might either be a MUSIC or IMAGE Album.
         */
        private Kind _kind;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param item The transport object to be transformed.
         */
        public Media(DTO item) {
            this(
                    item.getId(),
                    item.getTitle(),
                    item.getEndpoint(),
                    item.getKind(),
                    item.getTimestamp()
            );
        }

        public Media(
                UUID id,
                String title,
                String endpoint,
                Kind kind,
                Instant timestamp
        ) {
            super(title, timestamp);
            _endpoint = endpoint;
            _kind = kind;
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
        @Getter @FieldNameConstants
        public static class DTO extends ModelDTO {

            /** The endpoint for a URL, that is used to get the file of the item. */
            private String endpoint;

            /**
             * This is an Enum.
             * The Album might either be a MUSIC or IMAGE Album.
             */
            private Kind kind;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty UUID id,
                    @JsonProperty String title,
                    @JsonProperty Instant timestamp,
                    @JsonProperty String endpoint,
                    @JsonProperty Kind kind
            ) {
                super(id, title, timestamp);
                this.endpoint = endpoint;
                this.kind = kind;
            }

            /**
             * Converts into this DTO Object.
             * @param media The Object to be converted.
             */
            public DTO(Media media) {
                super(media);
                endpoint = media.get_endpoint();
                kind = Kind.valueOf(media.get_kind().toString());
            }
        }

        /** An enum that will describe the type of Album. */
        public enum Kind {
            IMAGE,
            MUSIC
        }
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
        private Set<Media.DTO> media;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty UUID id,
                @JsonProperty String title,
                @JsonProperty Instant timestamp,
                @JsonProperty Set<Media.DTO> media
        ) {
            super(id, title, timestamp);
            this.media = media;
        }

        /**
         * Converts into this DTO Object.
         * @param album The Object to be converted.
         */
        public DTO(Album album) {
            super(album);
            media = album.get_media().asSet(Media.DTO::new);
        }
    }
}
