package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.dtos.chats.RequestDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.bands.ArtistDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.bands.BandDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.utilities.collections.Liszt;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * An Artist can either be a solo Performer or member of a Band, which changes the Subscription, if it ain't freemium.
 * Extends from Performer.
 */
public class Artist extends Performer {

    /**
     * The Bands that the Artist is a member of.
     */
    @Getter
    private Liszt<Band> _bands;

    /**
     * A description of the gear, that the Artist possesses and what they require for an Event.
     */
    @Getter @Setter
    private String _runner;

    /**
     * The Requests requested for this Artist.
     */
    @Getter
    private Liszt<Request> _requests;

    public Artist(ArtistDTO artist) {
        super(artist.getPrimaryId(), artist.getUsername(), artist.getFirstName(), artist.getLastName(),
                artist.getDescription(), new ContactInfo(artist.getContactInfo()), Authority.ARTIST, artist.getAlbums(),
                artist.getRatings(), artist.getEvents(), artist.getGigs(), artist.getChatRooms(),
                new Subscription(artist.getSubscription()), artist.getBulletins(), artist.getFans(), artist.getIdols(),
                artist.getTimestamp());
        _bands = new Liszt<>();
        for (BandDTO band : artist.getBands())
            _bands.add(new Band(band));

        _runner = artist.getRunner();

        _requests = new Liszt<>();
        for (RequestDTO request : artist.getRequests())
            _requests.add(new Request(request));
    }
    public Artist(long id) {
        super(id);
    }

    public Artist(long id, String username, String firstName, String lastName, String description,
                  ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                  Liszt<Gig> gigs, Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins,
                  Liszt<Band> bands, String runner, Liszt<User> fans, Liszt<User> idols, Liszt<Request> requests,
                  LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, Authority.ARTIST, albums, ratings,
                events, gigs, chatRooms, subscription, bulletins, fans, idols, timestamp);
        _bands = bands;
        _runner = runner;
        _requests = requests;

        _assembling = true;
    }

    public Artist(String username, String firstName, String lastName, String description, Subscription subscription,
                  ContactInfo contactInfo, Liszt<Band> bands, String runner) {
        super(username, firstName, lastName, description, subscription, Authority.ARTIST);
        _contactInfo = contactInfo;
        _bands = bands;
        _runner = runner;
        _requests = new Liszt<>();
        _assembling = true;
    }

    /**
     * Sets the Users of Requests.
     * Will only be done, if it is under assembling.
     * @return The Artist.
     */
    public Artist set_requestUsers() {
        if (_assembling)
            for (int i = 1; i <= _requests.size(); i++)
                _requests.Get(i).set_user(this);

        return this;
    }

    /**
     * Adds a Band to the Liszt of bands.
     * @param band A specific Band, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> addBand(Band band) { return addBands(new Band[]{band});}

    /**
     * Adds multiple Bands to the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> addBands(Band[] bands) {
        _bands.add(bands);
        return _bands;
    }

    /**
     * Removes a Band from the Liszt of bands.
     * @param band A specific Band, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> removeBand(Band band) { return removeBands(new Band[]{band}); }

    /**
     * Removes multiple Bands from the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> removeBands(Band[] bands) {
        _bands.remove(bands);
        return _bands;
    }

    /**
     * Adds a Request to the Liszt of Requests.
     * @param request An object of Request, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> add(Request request) { return add(new Request[]{request}); }

    /**
     * Adds Requests to the Liszt of Requests.
     * @param requests An array of Requests, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> add(Request[] requests) {
        _requests.add(requests);
        return _requests;
    }

    /**
     * Removes a Request of the Liszt of Requests.
     * @param request An object of Request, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Request request) { return remove(new Request[]{request}); }

    /**
     * Removes Requests of the Liszt of Requests.
     * @param requests An array of Requests, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Request[] requests) {
        _requests.remove(requests);
        return _requests;
    }

    @Override
    public String toString() {
        return "Artist(" +
                    "id="+_primaryId+
                    ",username="+_username+
                    ",description="+_description+
                    ",timestamp="+_timestamp+
                    ",runner="+_runner+
                ")";
    }
}
