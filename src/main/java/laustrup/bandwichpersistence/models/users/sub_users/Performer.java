package laustrup.bandwichpersistence.models.users.sub_users;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
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
                     ContactInfo contactInfo, Authority authority, Liszt<Album> albums, Liszt<Rating> ratings,
                     Liszt<Event> events, Liszt<Gig> gigs, Liszt<ChatRoom> chatRooms, Subscription subscription,
                     Liszt<Bulletin> bulletins, Liszt<User> fans, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events,
                chatRooms, subscription, bulletins, idols, timestamp);
        _authority = authority;
        _gigs = gigs;
        _fans = fans;
    }

    public Performer(long id, String username, String description, ContactInfo contactInfo, Authority authority,
                     Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events, Liszt<Gig> gigs,
                     Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins, Liszt<User> fans,
                     Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events,
                chatRooms, subscription, bulletins, idols, timestamp);
        _authority = authority;
        _gigs = gigs;
        _fans = fans;
    }

    public Performer(String username, String firstName, String lastName, String description, Subscription subscription,
                     Authority authority) {
        super(username, firstName, lastName, description, subscription);
        _authority = authority;
        _gigs = new Liszt<>();
        _fans = new Liszt<>();
    }

    public Performer(String username, String description, Subscription subscription, Authority authority) {
        super(username, description, subscription);
        _authority = authority;
        _gigs = new Liszt<>();
        _fans = new Liszt<>();
    }

    /**
     * Sets the fans. Is only allowed under assembling.
     * @param fans The Participants that will be set to be the fans.
     * @return All the fans.
     */
    public Liszt<User> set_fans(Liszt<User> fans) {
        if (_assembling)
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
        for (int i = 1; i <= _albums.size(); i++)
            _albums.get(i).setAuthor(this);
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
