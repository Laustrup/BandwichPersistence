package laustrup.bandwichpersistence.models.users.sub_users;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Message;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
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
public abstract class Performer extends User {

    /**
     * Contains the music Albums of the Performer.
     */
    @Getter
    protected Liszt<Album> _music;

    public Performer(long id, String username, String firstName, String lastName, String description,
                     ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                     Liszt<ChatRoom> chatRooms, Liszt<Message> messages, Subscription subscription,
                     LocalDateTime timestamp, Liszt<Album> music) {
        super(id, username, firstName, lastName, description, contactInfo, images, ratings, events,
                chatRooms, messages, subscription, timestamp);
        _music = music;
    }

    public Performer(String username, String firstName, String lastName, String description, Subscription subscription, Liszt<Album> music) {
        super(username, firstName, lastName, description, subscription);
        _music = music;

        _images = new Album();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
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
            if (_music.get(i).get_id() == albumId) {
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
     * @return The Album the removed the new endpoint. Null if it hasn't been removed.
     */
    public Album remove(String endpoint, long albumId) {
        for (Album album : _music) {
            if (album.get_id() == albumId) {
                album.remove(endpoint);
                return album;
            }
        }
        return null;
    }
}