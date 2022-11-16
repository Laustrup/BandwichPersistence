package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Message;
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
import java.util.InputMismatchException;

/**
 * Extends performer and contains Artists as members
 */
@NoArgsConstructor @ToString
public class Band extends Performer {

    /**
     * Contains all the Artists, that are members of this band.
     *
     */
    @Getter
    private Liszt<Artist> _members;

    /**
     * A description of the gear, that the band possesses and what they require for an Event.
     */
    @Getter @Setter
    private String _runner;

    /**
     * All the participants that are following this Band, is included here.
     */
    @Getter
    private Liszt<Participant> _fans;

    public Band(long id, String username, String description,
                ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                Liszt<ChatRoom> chatRooms, Liszt<Message> messages, Subscription subscription, LocalDateTime timestamp,
                Liszt<Album> music, Liszt<Artist> members, String runner, Liszt<Participant> fans)
            throws InputMismatchException {
        super(id, username, null, null, description, contactInfo, images, ratings,
                events, chatRooms, messages, subscription, timestamp, music);

        if (_members.size() > 0)
            _members = members;
        else
            throw new InputMismatchException();

        _runner = runner;
        _fans = fans;
    }

    public Band(String username, String description, Subscription subscription,
                Liszt<Album> music, Liszt<Artist> members) throws InputMismatchException {
        super(username, null, null, description, subscription, music);

        if (_members.size() > 0)
            _members = members;
        else
            throw new InputMismatchException();

        _fans = new Liszt<>();
    }

    /**
     * Adds an Artist to the Liszt of members.
     * @param artist An object of Artist, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> addMember(Artist artist) { return addMembers(new Artist[]{artist}); }

    /**
     * Adds Artists to the Liszt of members.
     * @param artists An array of artists, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> addMembers(Artist[] artists) {
        _members.add(artists);
        return _members;
    }

    /**
     * Removes an Artist of the Liszt of members.
     * @param artist An object of Artist, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> removeMember(Artist artist) { return removeMembers(new Artist[]{artist}); }

    /**
     * Removes Artists of the Liszt of members.
     * @param artists An array of artists, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> removeMembers(Artist[] artists) {
        _members.remove(artists);
        return _members;
    }

    /**
     * Adds a Fan to the Liszt of fans.
     * @param fan An object of Fan, that is wished to be added.
     * @return The whole Liszt of fans.
     */
    public Liszt<Participant> addFan(Participant fan) { return addFans(new Participant[]{fan}); }

    /**
     * Adds Fans to the Liszt of fans.
     * @param fans An array of fans, that is wished to be Added.
     * @return The whole Liszt of fans.
     */
    public Liszt<Participant> addFans(Participant[] fans) {
        _fans.add(fans);
        return _fans;
    }

    /**
     * Removes a Fan of the Liszt of fans.
     * @param fan An object of Fan, that is wished to be removed.
     * @return The whole Liszt of fans.
     */
    public Liszt<Participant> removeFan(Participant fan) { return removeFans(new Participant[]{fan}); }

    /**
     * Removes Fans of the Liszt of fans.
     * @param fans An array of fans, that is wished to be removed.
     * @return The whole Liszt of fans.
     */
    public Liszt<Participant> removeFans(Participant[] fans) {
        _fans.remove(fans);
        return _fans;
    }
}