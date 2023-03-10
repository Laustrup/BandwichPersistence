package laustrup.bandwichpersistence.models.users.sub_users.participants;

import laustrup.bandwichpersistence.models.dtos.RatingDTO;
import laustrup.bandwichpersistence.models.dtos.albums.AlbumDTO;
import laustrup.bandwichpersistence.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.BulletinDTO;
import laustrup.bandwichpersistence.models.dtos.events.EventDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.subscriptions.SubscriptionOffer;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.collections.Liszt;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
public class Participant extends User {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    @Getter
    private Liszt<User> _idols;

    public Participant(ParticipantDTO participant) {
        super(participant.getPrimaryId(), participant.getUsername(), participant.getFirstName(), participant.getLastName(),
                participant.getDescription(), new ContactInfo(participant.getContactInfo()), participant.getAlbums(),
                participant.getRatings(), participant.getEvents(), participant.getChatRooms(),
                new Subscription(participant.getSubscription()), participant.getBulletins(), Authority.PARTICIPANT,
                participant.getTimestamp());
        _idols = new Liszt<>();
        for (UserDTO idol : participant.getIdols())
            _idols.add(DTOService.get_instance().convertFromDTO(idol));
    }
    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, AlbumDTO[] albums, RatingDTO[] ratings,
                       EventDTO[] events, ChatRoomDTO[] chatRooms, Subscription subscription, BulletinDTO[] bulletins,
                       UserDTO[] idols, LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = new Liszt<>();
        for (UserDTO idol : idols)
            _idols.add(DTOService.get_instance().convertFromDTO(idol));
    }
    public Participant(long id) {
        super(id);
    }
    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, Long cardId, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, cardId),
                bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.set_user(this);
        setSubscriptionUser();
    }

    public Participant(long id, String username, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, Long cardId, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, cardId),
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
        _subscription.set_user(this);
    }

    public Participant(long id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                       Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, Subscription subscription,
                       Liszt<Bulletin> bulletins, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.set_user(this);
    }

    public Participant(String username, String firstName, String lastName, String description,
                       SubscriptionOffer subscriptionOffer, Liszt<User> idols) {
        super(username, firstName, lastName, description,
                new Subscription(new Participant(0), Subscription.Type.FREEMIUM,
                        Subscription.Status.ACCEPTED, subscriptionOffer, null),
                Authority.PARTICIPANT);
        _idols = idols;
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
        return "Participant(" +
                    "id=" + _primaryId +
                    ",username=" + _username +
                    ",description=" + _description +
                    ",timestamp=" + _timestamp +
                ")";
    }
}
