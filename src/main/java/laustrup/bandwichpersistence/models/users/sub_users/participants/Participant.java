package laustrup.bandwichpersistence.models.users.sub_users.participants;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Message;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.SubscriptionOffer;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@NoArgsConstructor @ToString
public class Participant extends User {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    @Getter
    private Liszt<User> _followings;

    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Liszt<Message> messages,
                       Subscription.Status subscriptionStatus, SubscriptionOffer subscriptionOffer,
                       LocalDateTime timestamp, Liszt<User> followings) {
        super(id, username, firstName, lastName, description, contactInfo, images, ratings, events, chatRooms, messages,
                new Subscription(new Venue(), Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, null),
                timestamp);
        _followings = followings;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    public Participant(String username, String firstName, String lastName, String description,
                       SubscriptionOffer subscriptionOffer, Liszt<User> followings) {
        super(username, firstName, lastName, description,
                new Subscription(new Venue(), Subscription.Type.FREEMIUM,
                        Subscription.Status.ACCEPTED, subscriptionOffer, null));
        _followings = followings;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    /**
     * Adds a User to the followings of the Participant.
     * @param following A User, that is wished to be added.
     * @return All the followings of the Participant.
     */
    public Liszt<User> add(User following) {
        _followings.add(following);
        return _followings;
    }

    /**
     * Removes a User from the followings of the Participant.
     * @param following a User, that is wished to be removed.
     * @return All the followings of the Participant.
     */
    public Liszt<User> remove(User following) {
        _followings.remove(following);
        return _followings;
    }
}