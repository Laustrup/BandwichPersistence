package laustrup.bandwichpersistence.models.dtos.users.sub_users;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.dtos.events.GigDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * An abstract class object, that can be extended by classes such as Artist and Band.
 * Extends from User.
 */
@NoArgsConstructor @Data
public abstract class PerformerDTO extends ParticipantDTO {

    /**
     * Describes all the gigs, that the Performer is a part of an act.
     */
    protected GigDTO[] gigs;

    /**
     * All the participants that are following this Performer, is included here.
     */
    protected UserDTO[] fans;

    public PerformerDTO(long id, String username, String firstName, String lastName, String description,
                        ContactInfo contactInfo, Authority authority, Liszt<Album> albums, Liszt<Rating> ratings,
                        Liszt<Event> events, Liszt<Gig> gigs, Liszt<ChatRoom> chatRooms, Subscription subscription,
                        Liszt<Bulletin> bulletins, Liszt<User> fans, Liszt<User> idols, LocalDateTime timestamp) {
        super(new Participant(id, username, firstName, lastName, description, contactInfo, albums, ratings, events,
                chatRooms, subscription, bulletins, idols, timestamp));
        this.authority = authority;
        this.gigs = new GigDTO[gigs.size()];
        for (int i = 0; i < this.gigs.length; i++)
            this.gigs[i] = new GigDTO(gigs.get(i+1));
        this.fans = new UserDTO[fans.size()];
        for (int i = 0; i < this.fans.length; i++)
            this.fans[i] = DTOService.get_instance().convertToDTO(fans.get(i+1));
    }

    public PerformerDTO(long id, String username, String description, ContactInfo contactInfo, Authority authority,
                        Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events, Liszt<Gig> gigs,
                        Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins,
                        Liszt<User> fans, Liszt<User> idols, LocalDateTime timestamp) {
        super(new Participant(id, username, description, contactInfo, albums, ratings, events,
                chatRooms, subscription, bulletins, idols, timestamp));
        this.authority = authority;
        this.gigs = new GigDTO[gigs.size()];
        for (int i = 0; i < this.gigs.length; i++)
            this.gigs[i] = new GigDTO(gigs.get(i+1));
        this.fans = new UserDTO[fans.size()];
        for (int i = 0; i < this.fans.length; i++)
            this.fans[i] = DTOService.get_instance().convertToDTO(fans.get(i+1));
    }
}
