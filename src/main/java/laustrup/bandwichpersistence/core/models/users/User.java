package laustrup.bandwichpersistence.core.models.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract class, which is meant to be extended to a user type of object.
 * It extends from Model class.
 * Can calculate full name from first- and last name.
 */
@Getter @FieldNameConstants
public abstract class User<IDENTITY extends User.Id> extends Model<IDENTITY, Signature.UUID> {

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

  //TODO Implement
  protected String _password;

  /**
   * This subscription defines details of subscription,
   * including its status.
   * Only Artists and Bands can have a premium membership,
   * since they are the only paying users.
   */
  protected Subscription _subscription;

  protected History _history;

  /**
   * Will translate a transport object of this object into a construct of this object.
   * @param user The transport object to be transformed.
   */
  public User(UserDTO<IDENTITY> user, IDENTITY identity) {
    this(
        identity,
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getDescription(),
        new ContactInfo(user.getContactInfo()),
        Seszt.copy(user.getParticipations(), User.Participation::new),
        new Subscription(user.getSubscription()),
            user.getHistory(),
                user.getTimestamp()
        );
    }

    public User(
            IDENTITY id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Seszt<Participation> participations,
            Subscription subscription,
            History history,
            Instant timestamp
    ) {
        super(id, username + "-" + id, timestamp);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        _contactInfo = contactInfo;
        _description = description;
        _participations = participations;
        _subscription = subscription;
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

    public static class Id extends CommonIdentity<Signature.UUID> {

        public Id(Signature.UUID identifier) {
            super(identifier);
        }

        public Id(java.util.UUID identifier) {
            super(new Signature.UUID(identifier));
        }

        @Override
        public Class<?> getOwnerClassType() {
            return User.class;
        }
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

        public Event.Id get_eventId() {
            return _event.get_identity();
        }

        @Getter @FieldNameConstants @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DTO extends ParticipationBase.DTO {

            private Event.DTO event;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty Event.DTO event,
                    @JsonProperty ParticipationBase.Type type,
                    @JsonProperty Instant timestamp
            ) {
                super(type, timestamp);
                this.event = event;
            }

            public DTO(Participation participation) {
                this(
                        new Event.DTO(participation.get_event()),
                        participation.get_type(),
                        participation.get_timestamp()
                );
            }
        }
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter @FieldNameConstants
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class UserDTO<IDENTITY extends Identity<Signature.UUID>> extends ModelDTO<IDENTITY, Signature.UUID, java.util.UUID> {

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

        protected History history;

        public UserDTO(
                java.util.UUID id,
                String username,
                String firstName,
                String lastName,
                String description,
                ContactInfo.DTO contactInfo,
                Set<Participation.DTO> participations,
                Subscription.DTO subscription,
                History history,
                Instant timestamp
        ) {
            super(id, username + "-" + id, timestamp);
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.fullName = firstName + " " + lastName;
            this.description = description;
            this.contactInfo = contactInfo;
            this.participations = participations;
            this.chatRooms = new HashSet<>();
            this.subscription = subscription;
            this.history = history;
            this.timestamp = timestamp;
        }

        public UserDTO(User<? extends User.Id> user) {
            this(
                    user.get_identity().get_value(),
                    user.get_username(),
                    user.get_firstName(),
                    user.get_lastName(),
                    user.get_description(),
                    new ContactInfo.DTO(user.get_contactInfo()),
                    user.get_participations().asSet(Participation.DTO::new),
                    new Subscription.DTO(user.get_subscription()),
                    user.get_history(),
                    user.get_timestamp()
            );
            fullName = user.get_fullName();
        }
    }
}
