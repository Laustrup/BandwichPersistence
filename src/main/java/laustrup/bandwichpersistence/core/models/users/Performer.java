package laustrup.bandwichpersistence.core.models.users;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.services.DTOService;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * An abstract class object, that can be extended by classes such as Artist and Band.
 * Extends from User.
 */
@Getter @FieldNameConstants
public abstract class Performer extends Participant {

    /**
     * Describes all the gigs, that the Performer is a part of an act.
     */
    protected Seszt<Event.Gig> _gigs;

    /**
     * All the participants that are following this Performer, is included here.
     */
    protected Seszt<User> _fans;

    /**
     * A description of the gear, that the Artist possesses and what they require for an Event.
     */
    @Setter
    private String _runner;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param performer The transport object to be transformed.
     */
    public Performer(PerformerDTO performer) {
        super(performer);

        _gigs = new Seszt<>();
        for (Event.Gig.DTO gig : performer.getGigs())
            _gigs.add(new Event.Gig(gig));

        _fans = new Seszt<>();
        for (UserDTO fan : performer.getFans())
            _fans.add((User) DTOService.convert(fan));

        _runner = performer.getRunner();
    }

    /**
     * Constructor with all fields.
     * @param id The primary unique id.
     * @param username The name that this User identifies by.
     * @param firstName The real first name of this User.
     * @param lastName The real last name of this User.
     * @param description A description to inform other Users of this User.
     * @param contactInfo An Object that will describe ways to come in contact with this User.
     * @param albums Any kinds of files that this User has stored.
     * @param ratings Values given to this User.
     * @param events Events that this User joins or hosts.
     * @param gigs Describes all the gigs, that the Performer is a part of an act.
     * @param chatRooms Rooms that Users can communicate in.
     * @param subscription Defines the details of this User's subscription.
     * @param posts Messages that can be written publicly on dashboard.
     * @param fans All the participants that are following this Performer, is included here.
     * @param idols These are the Users that the Participant can follow,
     *              indicating that new content will be shared with the Participant.
     * @param runner A description of the gear, that the Artist possesses and what they require for an Event.
     * @param history The Events for this object.
     * @param timestamp The time this User was created.
     */
    public Performer(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<Event.Gig> gigs,
            Seszt<ChatRoom> chatRooms,
            Subscription subscription,
            Liszt<Post> posts,
            Seszt<User> fans,
            Seszt<User> idols,
            String runner,
            History history,
            LocalDateTime timestamp
    ) {
        super(
                id,
                username,
                firstName,
                lastName,
                description,
                contactInfo,
                albums,
                ratings,
                events,
                chatRooms,
                subscription,
                posts,
                idols,
                history,
                timestamp
        );
        _gigs = gigs;
        _fans = fans;
        _runner = runner;
    }

    /**
     * Will add a Gig to the Performer.
     * @param gig A Gig object, that is wished to be added.
     * @return All the Gigs of the Performer.
     */
    public Seszt<Event.Gig> add(Event.Gig gig) {
        _gigs.add(gig);
        return _gigs;
    }

    /**
     * Removes a Gig from the Liszt of Gigs of the Performer.
     * @param gig A Gig object, that is wished to be removed.
     * @return All the Gigs of the Performer.
     */
    public Seszt<Event.Gig> remove(Event.Gig gig) {
        _gigs.remove(gig);
        return _gigs;
    }

    /**
     * Adds a Fan to the Liszt of fans.
     * @param fan An object of Fan, that is wished to be added.
     * @return The whole Liszt of fans.
     */
    public Seszt<User> add(User fan) {
        return add(new User[]{fan});
    }

    /**
     * Adds Fans to the Liszt of fans.
     * @param fans An array of fans, that is wished to be Added.
     * @return The whole Liszt of fans.
     */
    public Seszt<User> add(User[] fans) {
        return _fans.Add(fans);
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public abstract static class PerformerDTO extends Participant.DTO {

        /**
         * Describes all the gigs, that the Performer is a part of an act.
         */
        public Event.Gig.DTO[] gigs;

        /**
         * All the participants that are following this Performer, is included here.
         */
        public UserDTO[] fans;

        /**
         * A description of the gear, that the Artist possesses and what they require for an Event.
         */
        private String runner;

        /**
         * Converts into this DTO Object.
         * @param performer The Object to be converted.
         */
        public PerformerDTO(Performer performer) {
            super(performer);
            gigs = new Event.Gig.DTO[performer.get_gigs().size()];
            for (int i = 0; i < gigs.length; i++)
                gigs[i] = new Event.Gig.DTO(performer.get_gigs().get(i));
            fans = new UserDTO[performer.get_fans().size()];
            for (int i = 0; i < fans.length; i++)
                fans[i] = DTOService.convert(performer.get_fans().get(i));
            runner = performer.get_runner();
        }
    }
}
