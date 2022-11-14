package laustrup.bandwichpersistence.models.users.sub_users.venues;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor @ToString
public class Venue extends User {

    @Getter @Setter
    private String _location, _gearDescription;
    @Getter
    private Liszt<Event> _events;
    @Getter @Setter
    private int _size;

    public Venue(long id, String username, String description,
                 ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                 Liszt<ChatRoom> chatRooms, LocalDateTime timestamp, String location, String gearDescription,
                 Liszt<Event> event, int size) {
        super(id, username, new String(), new String(), description, contactInfo, images, ratings, events, chatRooms, timestamp);
        _location = location;
        _gearDescription = gearDescription;
        _events = events;
        _size = size;
    }

    public Venue(String username, String description,
                 String location, String gearDescription, int size) {
        super(username, new String(), new String(), description);
        _location = location;
        _gearDescription = gearDescription;
        _events = new Liszt<>();
        _size = size;
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
