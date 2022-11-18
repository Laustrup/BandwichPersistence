package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Message;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * An Artist can either be a solo Performer or member of a Band, which changes the Subscription, if it ain't freemium.
 * Extends from Performer.
 */
@NoArgsConstructor @ToString
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

    public Artist(long id, String username, String firstName, String lastName, String description,
                  ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                  Liszt<ChatRoom> chatRooms, Liszt<Message> messages, Subscription subscription,
                  Liszt<Bulletin> bulletins, LocalDateTime timestamp, Liszt<Album> music,
                  Liszt<Band> bands, String runner, Liszt<Participant> fans, Liszt<User> followings) {
        super(id, username, firstName, lastName, description, contactInfo, images, ratings,
                events, chatRooms, messages, subscription, bulletins, timestamp, music, fans, followings);
        _bands = bands;
        _runner = runner;
    }

    public Artist(String username, String firstName, String lastName, String description, Subscription subscription,
                  Liszt<Band> bands, String runner) {
        super(username, firstName, lastName, description, subscription);
        _bands = bands;
        _runner = runner;
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
}
