package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.MusicalUser;
import laustrup.bandwichpersistence.models.users.sub_users.audiences.Participant;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor @ToString
public class Band extends MusicalUser {

    @Getter
    private Liszt<Artist> _members;
    @Getter @Setter
    private String _runner;
    @Getter
    private Liszt<Participant> _fans;

    public Band(long id, String username, String firstName, String lastName, String description,
                ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                Liszt<ChatRoom> chatRooms, LocalDateTime timestamp, Liszt<Album> music, Liszt<Artist> members,
                String runner, Liszt<Participant> fans) {
        super(id, username, firstName, lastName, description, contactInfo, images, ratings, events, chatRooms, timestamp, music);
        _members = members;
        _runner = runner;
        _fans = fans;
    }

    public Band(String username, String firstName, String lastName, String description,
                Liszt<Album> music, Liszt<Artist> members) {
        super(username, firstName, lastName, description, music);
        _members = members;
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
