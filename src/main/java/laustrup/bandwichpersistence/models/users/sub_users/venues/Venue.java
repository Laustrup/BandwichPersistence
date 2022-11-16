package laustrup.bandwichpersistence.models.users.sub_users.venues;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Message;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.SubscriptionOffer;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@NoArgsConstructor @ToString
public class Venue extends User {

    /**
     * The location that the Venue is located at, which could be an address or simple a place.
     *
     */
    @Getter @Setter
    private String _location;

    /**
     * The description of the gear that the Venue posses.
     */
    @Getter @Setter
    private String _gearDescription;

    /**
     * All the Events that this Venue has planned.
     */
    @Getter
    private Liszt<Event> _events;

    /**
     * The size of the stage and room, that Events can be held at.
     */
    @Getter @Setter
    private int _size;

    public Venue(long id, String username, String description,
                 ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                 Liszt<ChatRoom> chatRooms, Liszt<Message> messages, LocalDateTime timestamp,
                 String location, String gearDescription, Subscription.Status subscriptionStatus,
                 SubscriptionOffer subscriptionOffer, int size) {
        super(id, username, null, null, description, contactInfo, images, ratings, events, chatRooms, messages,
                new Subscription(new Venue(), Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, null),
                timestamp);

        if (location == null)
            _location = _contactInfo.getAddressInfo();
        else
            _location = location;

        _gearDescription = gearDescription;
        _events = events;
        _size = size;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    public Venue(String username, String description,
                 String location, String gearDescription, SubscriptionOffer subscriptionOffer, int size) {
        super(username, null, null, description,
                new Subscription(new Venue(), Subscription.Type.FREEMIUM,
                        Subscription.Status.ACCEPTED, subscriptionOffer, null));

        if (location == null)
            _location = _contactInfo.getAddressInfo();
        else
            _location = location;

        _gearDescription = gearDescription;
        _events = new Liszt<>();
        _size = size;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    /**
     * Adds an Event to the Liszt of events.
     * @param event An object of Event, that is wished to be added.
     * @return The whole Liszt of events.
     */
    public Liszt<Event> addEvent(Event event) { return addEvents(new Event[]{event}); }

    /**
     * Adds Events to the Liszt of events.
     * @param events An array of events, that is wished to be added.
     * @return The whole Liszt of events.
     */
    public Liszt<Event> addEvents(Event[] events) {
        _events.add(events);
        return _events;
    }

    /**
     * Removes an Event of the Liszt of events.
     * @param event An object of Event, that is wished to be removed.
     * @return The whole Liszt of events.
     */
    public Liszt<Event> removeEvent(Event event) { return removeEvents(new Event[]{event}); }

    /**
     * Removes Events of the Liszt of events.
     * @param events An array of events, that is wished to be removed.
     * @return The whole Liszt of events.
     */
    public Liszt<Event> removeEvents(Event[] events) {
        _events.remove(events);
        return _events;
    }
}
