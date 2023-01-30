package laustrup.bandwichpersistence.models.dtos.users.subscriptions;

import laustrup.bandwichpersistence.models.users.subscriptions.SubscriptionOffer;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This offer determines, if the price of a Subscription should be changed through its attributes.
 */
@NoArgsConstructor @Data
public class SubscriptionOfferDTO {

    /**
     * Determines when this offer is no longer valid.
     */
    private LocalDateTime expires;

    /**
     * An enum that defines, which kind of offer this class is.
     */
    @Getter
    private Type type;

    /**
     * This attribute will be multiplied with the price of the Subscription.
     * It must be between 0 -> 1, seen as percentages, where 1 = 100%
     */
    private double effect;

    public SubscriptionOfferDTO(SubscriptionOffer subscriptionOffer) {
        expires = subscriptionOffer.get_expires();
        type = Type.valueOf(subscriptionOffer.get_type().toString());
        effect = subscriptionOffer.get_effect();
    }
    /**
     * An enum of the different values a SubscriptionOffer can be of.
     */
    public enum Type {
        FREE_TRIAL,
        SALE
    }
}
