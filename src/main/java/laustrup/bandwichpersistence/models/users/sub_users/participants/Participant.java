package laustrup.bandwichpersistence.models.users.sub_users.participants;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.subscriptions.SubscriptionOffer;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@NoArgsConstructor
public class Participant extends User {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    @Getter
    private Liszt<User> _idols;

    public Participant(long id) {
        super(id);
    }
    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, null),
                bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
        setSubscriptionUser();
    }

    public Participant(long id, String username, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, null),
                bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
        setSubscriptionUser();
    }

    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins,
                       Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    public Participant(long id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                       Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, Subscription subscription,
                       Liszt<Bulletin> bulletins, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    public Participant(String username, String firstName, String lastName, String description,
                       SubscriptionOffer subscriptionOffer, Liszt<User> idols) {
        super(username, firstName, lastName, description,
                new Subscription(new Participant(), Subscription.Type.FREEMIUM,
                        Subscription.Status.ACCEPTED, subscriptionOffer, null),
                Authority.PARTICIPANT);
        _idols = idols;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
        setSubscriptionUser();
    }

    public Participant(String username, String firstName, String lastName, String description,
                       Subscription subscription) {
        super(username, firstName, lastName, description, subscription, Authority.PARTICIPANT);
        _idols = new Liszt<>();
    }

    public Participant(String username, String description, Subscription subscription) {
        super(username, description, subscription, Authority.PARTICIPANT);
        _idols = new Liszt<>();
    }

    /**
     * Sets the followings. Is only allowed under assembling.
     * @param followings The Users that will be set to be the followings.
     * @return All the followings.
     */
    public Liszt<User> set_idols(Liszt<User> followings) {
        if (followings != null && _assembling)
            _idols = followings;
        return _idols;
    }

    /**
     * Adds a User to the followings of the Participant.
     * @param following A User, that is wished to be added.
     * @return All the followings of the Participant.
     */
    public Liszt<User> add(User following) {
        _idols.add(following);
        return _idols;
    }

    /**
     * Removes a User from the followings of the Participant.
     * @param following a User, that is wished to be removed.
     * @return All the followings of the Participant.
     */
    public Liszt<User> remove(User following) {
        _idols.remove(following);
        return _idols;
    }

    @Override
    public String toString() {
        return "Artist(id="+_primaryId+
                ",username="+_username+
                ",description="+_description+
                ",timestamp="+_timestamp+
                ")";
    }
}
