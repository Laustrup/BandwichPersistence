package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

/**
 * An abstract class, which is meant to be extended to a user type of object.
 * It extends from Model class.
 * Can calculate full name from first- and last name.
 */
@Getter @FieldNameConstants
public abstract class User extends Model {

    /**
     * The title of the user, that the user uses to use as a title for the profile.
     */
    @Setter
    protected String _username;

    /**
     * The real first name of the user's name.
     */
    @Setter
    protected String _firstName;

    /**
     * The real last name of the user's name.
     */
    @Setter
    protected String _lastName;

    /**
     * This is what the user uses to describe itself.
     */
    @Setter
    protected String _description;

    /**
     * An object that has the different attributes,
     * that can be used to contact this user.
     */
    protected ContactInfo _contactInfo;

    /**
     * The participation of the Events that this user is included in.
     */
    protected Seszt<Participation> _participations;

    /**
     * This subscription defines details of subscription,
     * including its status.
     * Only Artists and Bands can have a premium membership,
     * since they are the only paying users.
     */
    protected Subscription _subscription;

    protected Seszt<Authority> _authorities;

    protected History _history;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param user The transport object to be transformed.
     */
    public User(UserDTO user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getDescription(),
                new ContactInfo(user.getContactInfo()),
                Seszt.copy(user.getParticipations(), User.Participation::new),
                new Subscription(user.getSubscription()),
                new Seszt<>(user.getAuthorities().stream()),
                user.getHistory(),
                user.getTimestamp()
        );
    }

    public User(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Seszt<Participation> participations,
            Subscription subscription,
            Seszt<Authority> authorities,
            History history,
            Instant timestamp
    ) {
        super(
                id,
                username + "-" + id,
                timestamp
        );
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        _contactInfo = contactInfo;
        _description = description;
        _participations = participations;
        _subscription = subscription;
        _authorities = authorities;
        _history = history;
    }

    /**
     * For creating a new User.
     * @param username The name that this User identifies by.
     * @param firstName The real first name of this User.
     * @param lastName The real last name of this User.
     * @param description A description to inform other Users of this User.
     * @param subscription Defines the details of this User's subscription.
     */
    public User(
            String username,
            String firstName,
            String lastName,
            String description,
            Subscription subscription
    ) {
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        _description = description;

        _participations = new Seszt<>();

        _subscription = subscription;

        _timestamp = Instant.now();
    }

    /**
     * For creating a new User.
     * @param username The name that this User identifies by.
     * @param description A description to inform other Users of this User.
     * @param subscription Defines the details of this User's subscription.
     */
    public User(
            String username,
            String description,
            Subscription subscription
    ) {
        _username = username;
        _description = description;

        _participations = new Seszt<>();

        _subscription = subscription;

        _timestamp = Instant.now();
    }

    /**
     * Combines first and last name.
     * @return The calculated full name.
     */
    public String get_fullName() {
        return _firstName + " " + _lastName;
    }

    /**
     * Sets the status of the subscription.
     * @param status The status that is wished to be set as the status of the Subscription.
     * @return The Subscription of the User.
     */
    public Subscription change(Subscription.Status status) {
        _subscription.set_status(status);
        return _subscription;
    }

    public Seszt<Participation> add(Participation participation) {
        return _participations.Add(participation);
    }

    public Seszt<Participation> remove(Participation participation) {
        for (int i = 1; i <= _participations.size(); i++) {
            if (_participations.Get(i).get_eventId() == participation.get_eventId()) {
                _participations.remove(_participations.Get(i));
                break;
            }
        }

        return _participations;
    }

    public enum Authority {
        STANDARD,
        ADMIN
    }

    @Getter
    public static class Participation extends ParticipationBase {

        private Event _event;

        public Participation(DTO participation) {
            this(
                new Event(participation.getEvent()),
                participation.getType(),
                participation.getTimestamp()
            );
        }

        public Participation(Event event, Type type) {
            this(event, type, Instant.now());
        }

        public Participation(Event event, Type type, Instant timestamp) {
            super(type, timestamp);
            _event = event;
        }

        public UUID get_eventId() {
            return _event.get_id();
        }

        @Getter
        public static class DTO extends ParticipationBase.DTO {

            private Event.DTO event;

            public DTO(Participation participation) {
                super(participation);
                event = new Event.DTO(participation.get_event());
            }
        }
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public abstract static class UserDTO extends ModelDTO {

        /**
         * The title of the user, that the user uses to use as a title for the profile.
         */
        protected String username;

        /**
         * The real first name of the user's name.
         */
        protected String firstName;

        /**
         * The real last name of the user's name.
         */
        protected String lastName;

        /**
         * The real full name of the user's name.
         * Is generated by first- and last name.
         */
        protected String fullName;

        /**
         * This is what the user uses to describe itself.
         */
        protected String description;

        /**
         * An object that has the different attributes,
         * that can be used to contact this user.
         */
        protected ContactInfo.DTO contactInfo;

        /**
         * The Events that this user is included in.
         */
        protected Set<User.Participation.DTO> participations;

        /**
         * These ChatRooms can be used to communicate with other users.
         */
        protected Set<ChatRoom.DTO> chatRooms;

        /**
         * This subscription defines details of subscription,
         * including its status.
         * Only Artists and Bands can have a premium membership,
         * since they are the only paying users.
         */
        protected Subscription.DTO subscription;

        protected ZoneId zoneId;

        protected Set<Authority> authorities;

        protected History history;

        public UserDTO(User user) {
            super(user);
            username = user.get_username();
            firstName = user.get_firstName();
            lastName = user.get_lastName();
            fullName = user.get_fullName();
            description = user.get_description();
            contactInfo = new ContactInfo.DTO(user.get_contactInfo());
            participations = user.get_participations().asSet(Participation.DTO::new);
            subscription = new Subscription.DTO(user.get_subscription());
            authorities = user.get_authorities();
            history = user.get_history();
        }
    }
}
