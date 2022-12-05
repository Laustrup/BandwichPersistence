package laustrup.bandwichpersistence.models.users.sub_users;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Message;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * An abstract class object, that can be extended by classes such as Artist and Band.
 * Extends from User.
 */
@NoArgsConstructor
public abstract class Performer extends Participant {

    /**
     * Contains the music Albums of the Performer.
     */
    @Getter
    protected Liszt<Album> _music;

    /**
     * Describes all the gigs, that the Performer is a part of an act.
     */
    @Getter
    protected Liszt<Gig> _gigs;

    /**
     * All the participants that are following this Performer, is included here.
     */
    @Getter
    protected Liszt<User> _fans;

    public Performer(long id) {
        super(id);
    }
    public Performer(long id, String username, String firstName, String lastName, String description,
                     ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events, Liszt<Gig> gigs,
                     Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins,
                     LocalDateTime timestamp, Liszt<Album> music, Liszt<User> fans, Liszt<User> idols) {
        super(id, username, firstName, lastName, description, contactInfo, images, ratings, events,
                chatRooms, subscription, bulletins, timestamp, idols);
        _music = music;
        _gigs = gigs;
        _fans = fans;
    }

    public Performer(long id, String username, String description, ContactInfo contactInfo, Album images,
                     Liszt<Rating> ratings, Liszt<Event> events, Liszt<Gig> gigs, Liszt<ChatRoom> chatRooms,
                     Subscription subscription, Liszt<Bulletin> bulletins, LocalDateTime timestamp, Liszt<Album> music,
                     Liszt<User> fans, Liszt<User> idols) {
        super(id, username, description, contactInfo, images, ratings, events,
                chatRooms, subscription, bulletins, timestamp, idols);
        _music = music;
        _gigs = gigs;
        _fans = fans;
    }

    public Performer(String username, String firstName, String lastName, String description, Subscription subscription) {
        super(username, firstName, lastName, description, subscription);
        _music = new Liszt<>();
        _gigs = new Liszt<>();
        _fans = new Liszt<>();
    }

    public Performer(String username, String description, Subscription subscription) {
        super(username, description, subscription);
        _music = new Liszt<>();
        _gigs = new Liszt<>();
        _fans = new Liszt<>();
    }

    /**
     * Sets the fans. Is only allowed under assembling.
     * @param fans The Participants that will be set to be the fans.
     * @return All the fans.
     */
    public Liszt<User> set_fans(Liszt<User> fans) {
        if (fans != null && _assembling)
            _fans = fans;
        return _fans;
    }

    /**
     * Sets the Gigs. Is only allowed under assembling.
     * @param gigs The Gigs that will be set to be the Gigs.
     * @return All the Gigs.
     */
    public Liszt<Gig> set_gigs(Liszt<Gig> gigs) {
        if (_assembling)
            _gigs = gigs;
        return _gigs;
    }

    /**
     * Will set all the albums' author as this Performer.
     * Only use for assembly.
     */
    public void setAuthorOfAlbums() {
        _images.setAuthor(this);
        for (int i = 1; i <= _music.size(); i++)
            _music.get(i).setAuthor(this);
    }

    /**
     * Sets the card id of the Subscription.
     * Purpose is to use first time card information are provided.
     * @param id The id long value of the card, that is wished to be set.
     * @return If the id of the card is already set, it will return null,
     * otherwise it will return the Subscription of the User.
     */
    public Subscription set_cardId(long id) {
        if (_subscription.get_cardId() == null) {
            _subscription.set_cardId(id);
            return _subscription;
        }
        return null;
    }

    /**
     * Sets the type of Subscription and also determines the price from the new kind of type.
     * @param type An Enum of a type of Subscription, that is wished to be set as the new SubscriptionType.
     * @return The Subscription of the Performer.
     */
    public Subscription change_subscriptionType(Subscription.Type type) {
        _subscription.set_type(type);
        return _subscription;
    }

    /**
     * Will add an endpoint to an Album of the Performer.
     * @param endpoint An endpoint from a link containing a music file.
     * @param albumId The id of the Album, that is wished to add the endpoint.
     * @return The Album the has been added the new endpoint. Null if it hasn't been added.
     */
    public Album add(String endpoint, long albumId) {
        for (int i = 1; i <= _music.size(); i++) {
            if (_music.get(i).get_primaryId() == albumId) {
                _music.get(i).add(endpoint);
                return _music.get(i);
            }
        }
        return null;
    }

    /**
     * Removes an endpoint from an Album of the Performer.
     * @param endpoint An endpoint from a link containing a music file.
     * @param albumId The id of the Album, that is wished to remove the endpoint.
     * @return The Album of the removed endpoint. Null if it hasn't been removed.
     */
    public Album remove(String endpoint, long albumId) {
        for (Album album : _music) {
            if (album.get_primaryId() == albumId) {
                album.remove(endpoint);
                return album;
            }
        }
        return null;
    }

    /**
     * Will add a Gig to the Performer.
     * @param gig A Gig object, that is wished to be added.
     * @return All the Gigs of the Performer.
     */
    public Liszt<Gig> add(Gig gig) {
        _gigs.add(gig);
        return _gigs;
    }

    /**
     * Removes a Gig from the Liszt of Gigs of the Performer.
     * @param gig A Gig object, that is wished to be removed.
     * @return All the Gigs of the Performer.
     */
    public Liszt<Gig> remove(Gig gig) {
        _gigs.remove(gig);
        return _gigs;
    }

    /**
     * Adds a Fan to the Liszt of fans.
     * @param fan An object of Fan, that is wished to be added.
     * @return The whole Liszt of fans.
     */
    public Liszt<User> addFan(User fan) { return addFans(new User[]{fan}); }

    /**
     * Adds Fans to the Liszt of fans.
     * @param fans An array of fans, that is wished to be Added.
     * @return The whole Liszt of fans.
     */
    public Liszt<User> addFans(User[] fans) {
        _fans.add(fans);
        return _fans;
    }
}
