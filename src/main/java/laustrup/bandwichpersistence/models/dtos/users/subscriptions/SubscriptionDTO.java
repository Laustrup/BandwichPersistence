package laustrup.bandwichpersistence.models.dtos.users.subscriptions;

import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines the kind of subscription a user is having.
 * Only Artists and Bands can have a paying subscription.
 */
@NoArgsConstructor @Data
public class SubscriptionDTO extends ModelDTO {

    /**
     * The User that uses this Subscription.
     */
    private UserDTO user;

    /**
     * An enum that contains different kinds of types, that this Subscription can be.
     * Will also determine the price.
     */
    private Type type;

    /**
     * An enum that determines what kind of status, the situation of the Subscription is in.
     */
    private Status status;

    /**
     * How much the User should pay per month.
     */
    private int price;

    /**
     * An object class that specifies the offer if any, that this Subscription has.
     */
    private SubscriptionOfferDTO offer;

    /**
     * The id in the database of the card, that is connected to this subscription.
     * Is only the id and not the object for security reasons.
     */
    private Long cardId;

    public SubscriptionDTO(Subscription subscription) {
        super(subscription.get_user().get_primaryId(), subscription.get_cardId() == null ? 0 : subscription.get_cardId(),
                subscription.get_user().get_username()+"-Subscription: " + subscription.get_user().get_primaryId(),
                subscription.get_timestamp());
        type = Type.valueOf(subscription.get_type().toString());
        status = Status.valueOf(subscription.get_status().toString());
        offer = new SubscriptionOfferDTO(subscription.get_offer());
        cardId = subscription.get_cardId();
    }

    /**
     * An enum that can be of different types, determining the type of Subscription.
     */
    public enum Type {
        FREEMIUM,
        PREMIUM_BAND,
        PREMIUM_ARTIST
    }

    /**
     * An enum that defines Status that a Subscription is currently in.
     */
    public enum Status {
        ACCEPTED,
        BLOCKED,
        DISACTIVATED,
        CLOSED
    }
}
