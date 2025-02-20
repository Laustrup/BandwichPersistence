package laustrup.bandwichpersistence.core.models.users;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @FieldNameConstants
public class Artist extends BusinessUser {

    /**
     * The Bands that the Artist is a member of.
     */
    private Seszt<Band> _bands;

    /**
     * The Requests requested for this Artist.
     */
    private Seszt<Request> _requests;

    private Seszt<Album> _albums;

    private Seszt<Follow> _follows;

    private Seszt<Event.Gig> _gigs;

    /**
     * A description of the gear, that the Artist possesses and what they require for an Event.
     */
    @Setter
    private String _runner;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param artist The transport object to be transformed.
     */
    public Artist(DTO artist) {
        super(artist);
        _bands = new Seszt<>();
        for (Band.DTO band : artist.getBands())
            _bands.add(new Band(band));

        _requests = new Seszt<>();
        for (Request.DTO request : artist.getRequests())
            _requests.add(new Request(request));
    }

    public Artist(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Seszt<Album> albums,
            Seszt<Event> events,
            Subscription subscription,
            Seszt<Authority> authorities,
            Seszt<ChatRoom> chatRooms,
            Seszt<Band> bands,
            Seszt<Event.Gig> gigs,
            String runner,
            Seszt<Follow> follows,
            Seszt<Request> requests,
            History history,
            Instant timestamp
    ) {
        super(
            id,
                username,
                firstName,
                lastName,
                description,
                contactInfo,
                events,
                subscription,
                authorities,
                chatRooms,
                history,
                timestamp
        );
        _albums = albums;
        _bands = bands;
        _requests = requests;
        _runner = runner;
        _gigs = gigs;
        _follows = follows;

    }

    /**
     * Adds a Band to the Liszt of bands.
     * @param band A specific Band, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> add(Band band) {
        return add(new Band[]{band});
    }

    /**
     * Adds multiple Bands to the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> add(Band[] bands) {
        return _bands.Add(bands);
    }

    /**
     * Removes a Band from the Liszt of bands.
     * @param band A specific Band, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> remove(Band band) {
        return remove(new Band[]{band});
    }

    /**
     * Removes multiple Bands from the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> remove(Band[] bands) {
        return _bands.remove(bands);
    }

    /**
     * Adds a Request to the Liszt of Requests.
     * @param request An object of Request, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Seszt<Request> add(Request request) {
        return _requests.Add(request);
    }

    /**
     * Removes a Request of the Liszt of Requests.
     * @param request An object of Request, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Seszt<Request> remove(Request request) {
        return _requests.remove(new Request[]{request});
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._primaryId,
                User.Fields._username,
                User.Fields._description,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_primaryId()),
                get_username(),
                get_description(),
                String.valueOf(get_timestamp())
            });
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends BusinessUserDTO {

        /**
         * The Bands that the Artist is a member of.
         */
        private Set<Band.DTO> bands;

        /**
         * The Requests requested for this Artist.
         */
        private Set<Request.DTO> requests;

        /**
         * Describes all the gigs, that the Performer is a part of an act.
         */
        protected Set<Event.Gig.DTO> gigs;

        /**
         * All the participants that are following this Performer, is included here.
         */
        protected Set<Follow.DTO> follows;

        protected Set<Album.DTO> albums;

        /**
         * A description of the gear, that the Artist possesses and what they require for an Event.
         */
        @Setter
        private String runner;

        /**
         * Converts into this DTO Object.
         * @param artist The Object to be converted.
         */
        public DTO(Artist artist) {
            super(artist);
            bands = artist.get_bands().stream().map(Band.DTO::new).collect(Collectors.toSet());
            requests = artist.get_requests().stream().map(Request.DTO::new).collect(Collectors.toSet());
            gigs = artist.get_gigs().stream().map(Event.Gig.DTO::new).collect(Collectors.toSet());
            follows = artist.get_follows().stream().map(Follow.DTO::new).collect(Collectors.toSet());
            albums = artist.get_albums().stream().map(Album.DTO::new).collect(Collectors.toSet());
            runner = artist.get_runner();
        }
    }
}
