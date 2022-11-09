package laustrup.bandwichpersistence.models.users.sub_users.bands;

import laustrup.bandwichpersistence.models.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.MusicalUser;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor @ToString
public class Artist extends MusicalUser {

    @Getter
    private Liszt<Band> _bands;
    @Getter @Setter
    private String _runner;

    public Artist(long id, String username, String firstName, String lastName, String description,
                  ContactInfo contactInfo, Album images, Liszt<Rating> ratings, Liszt<Event> events,
                  Liszt<ChatRoom> chatRooms, LocalDateTime timestamp, Liszt<Album> music, Liszt<Band> bands,
                  String runner) {
        super(id, username, firstName, lastName, description, contactInfo, images, ratings,
                events, chatRooms, timestamp, music);
        _bands = bands;
        _runner = runner;
    }

    public Artist(String username, String firstName, String lastName, String description,
                  Liszt<Album> music, Liszt<Band> bands, String runner) {
        super(username, firstName, lastName, description, music);
        _bands = bands;
        _runner = runner;
    }

    /**
     * Adds a Band to the Liszt of bands.
     * @param band A specific Band, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> addBand(Band band) { return addBands(new Band[]{band});}

    /**
     * Adds multiple Bands to the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> addBands(Band[] bands) {
        _bands.add(bands);
        return _bands;
    }

    /**
     * Removes a Band from the Liszt of bands.
     * @param band A specific Band, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> removeBand(Band band) { return removeBands(new Band[]{band}); }

    /**
     * Removes multiple Bands from the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Liszt<Band> removeBands(Band[] bands) {
        _bands.remove(bands);
        return _bands;
    }
}
