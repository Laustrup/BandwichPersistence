package laustrup.bandwichpersistence.core.models.users;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.services.DTOService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@Getter @FieldNameConstants
public class Participant extends User {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    private Seszt<User> _idols;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param participant The transport object to be transformed.
     */
    public Participant(DTO participant) {
        super(participant);
        _idols = new Seszt<>();
        for (UserDTO idol : participant.getIdols())
            _idols.add((User) DTOService.convert(idol));
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
     * @param chatRooms Rooms that Users can communicate in.
     * @param subscription Defines the details of this User's subscription.
     * @param posts Messages that can be written publicly on dashboard.
     * @param idols These are the Users that the Participant can follow,
     *              indicating that new content will be shared with the Participant.
     * @param history The Events for this object.
     * @param timestamp The time this User was created.
     */
    public Participant(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<ChatRoom> chatRooms,
            User.Subscription subscription,
            Liszt<Post> posts,
            Seszt<User> idols,
            History history,
            Instant timestamp
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
                history,
                timestamp
        );
        _idols = idols;
    }

    /**
     * Adds a User to the followings of the Participant.
     * @param following A User, that is wished to be added.
     * @return All the followings of the Participant.
     */
    public Seszt<User> add(User following) {
        return _idols.Add(following);
    }

    /**
     * Removes a User from the followings of the Participant.
     * @param following a User, that is wished to be removed.
     * @return All the followings of the Participant.
     */
    public Seszt<User> remove(User following) {
        return _idols.remove(new User[]{following});
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._primaryId,
                User.Fields._description,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_primaryId()),
                get_username(),
                get_description(),
                String.valueOf(get_timestamp())
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends UserDTO {

        /**
         * These are the Users that the Participant can follow,
         * indicating that new content will be shared with the Participant.
         */
        private UserDTO[] idols;

        /**
         * Converts the object to the data transport object.
         * @param participant The object to be converted.
         */
        public DTO(Participant participant) {
            super(participant);
            if (idols != null) {
                idols = new UserDTO[participant.get_idols().size()];
                for (int i = 0; i < idols.length; i++)
                    idols[i] = DTOService.convert(participant.get_idols().Get(i+1));
            }
        }

        /**
         * Converts the object to the data transport object.
         * @param user The object to be converted.
         */
        public DTO(User user) {
            super(user);
            if (user.getClass() == Participant.class) {
                idols = new UserDTO[((Participant) user).get_idols().size()];
                for (int i = 0; i < idols.length; i++)
                    idols[i] = DTOService.convert(((Participant) user).get_idols().Get(i+1));
            }
        }
    }

}
