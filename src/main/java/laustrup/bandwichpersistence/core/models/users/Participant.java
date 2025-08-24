package laustrup.bandwichpersistence.core.models.users;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@Getter @FieldNameConstants
public class Participant extends User<Participant.Id> {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    private Seszt<Follow> _follows;

    /**
     * Ratings made from other users on this user based on a value.
     */
    protected Seszt<Rating> _ratings;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param participant The transport object to be transformed.
     */
    public Participant(DTO participant) {
        this(
                new Participant.Id(participant.getId()),
                participant.getUsername(),
                participant.getFirstName(),
                participant.getLastName(),
                participant.getDescription(),
                new ContactInfo(participant.getContactInfo()),
                new Seszt<>(participant.getParticipations().stream().map(Participation::new)),
                new Seszt<>(participant.getRatings().stream().map(Rating::new)),
                new Subscription(participant.getSubscription()),
                new Seszt<>(participant.getFollows().stream().map(Follow::new)),
                participant.getHistory(),
                participant.getTimestamp()
        );
    }

    public Participant(
            Id id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Seszt<Participation> participations,
            Seszt<Rating> ratings,
            Subscription subscription,
            Seszt<Follow> follows,
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
                participations,
                subscription,
                history,
                timestamp
        );
        _ratings = ratings;
        _follows = follows;
    }

    /**
     * Adds a User to the followings of the Participant.
     * @param following A User, that is wished to be added.
     * @return All the followings of the Participant.
     */
    public Seszt<Follow> add(Follow following) {
        return _follows.Add(following);
    }

    /**
     * Removes a User from the followings of the Participant.
     * @param following a User, that is wished to be removed.
     * @return All the followings of the Participant.
     */
    public Seszt<Follow> remove(Follow following) {
        return _follows.remove(new Follow[]{following});
    }

    public static class Id extends User.Id {

        public Id(UUID identifier) {
            super(identifier);
        }

        @Override
        public Class<?> getOwnerClassType() {
            return Participant.class;
        }
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._identity,
                User.Fields._description,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_identity()),
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
    public static class DTO extends UserDTO<User.Id> {

        /**
         * These are the Users that the Participant can follow,
         * indicating that new content will be shared with the Participant.
         */
        private Set<Follow.DTO> follows;

        /**
         * Ratings made from other users on this user based on a value.
         */
        protected Set<Rating.DTO> ratings;

        /**
         * Converts the object to the data transport object.
         * @param participant The object to be converted.
         */
        public DTO(Participant participant) {
            super(participant);
            follows = Arrays.stream(participant.get_follows().get_data())
                    .map(Follow.DTO::new)
                    .collect(Collectors.toSet());
            ratings = Arrays.stream(participant.get_ratings().get_data())
                    .map(Rating.DTO::new)
                    .collect(Collectors.toSet());
        }
    }
}
