package laustrup.bandwichpersistence.models.users.subscriptions;

import laustrup.bandwichpersistence.models.dtos.users.subscriptions.SubscriptionOfferDTO;
import laustrup.bandwichpersistence.utilities.Printer;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.InputMismatchException;

/**
 * This offer determines, if the price of a Subscription should be changed through its attributes.
 */
@ToString
public class SubscriptionOffer {

    /**
     * Determines when this offer is no longer valid.
     */
    @Getter
    private LocalDateTime _expires;

    /**
     * An enum that defines, which kind of offer this class is.
     */
    @Getter
    private Type _type;

    /**
     * This attribute will be multiplied with the price of the Subscription.
     * It must be between 0 -> 1, seen as percentages, where 1 = 100%
     */
    private double _effect;

    public SubscriptionOffer(SubscriptionOfferDTO offer) {
        _expires = offer.getExpires();
        _type = Type.valueOf(offer.getType().toString());
        try {
            set_effect(offer.getEffect());
        } catch (InputMismatchException e) {
            Printer.get_instance().print("Couldn't create effect to subscription offer...", e);
        }
    }
    public SubscriptionOffer(LocalDateTime expires, Type type, double effect) {
        _expires = expires;
        _type = type;
        try {
            set_effect(effect);
        } catch (InputMismatchException e) {
            Printer.get_instance().print("Couldn't create effect to subscription offer...", e);
        }
    }

    /**
     * Checks if the type of the offer is a sale offer.
     * @return If it ain't a sale offer, it returns 1,
     * otherwise the effect, that is between 0 -> 1.
     */
    public double get_effect() {
        if (_type == Type.SALE)
            return _effect;
        return 1;
    }

    /**
     * Checks if effect is between 0 -> 1, in that case it will set the effect value.
     * @param effect The double value that is wished to be set.
     * @return If no exception is thrown, it will return the effect value.
     * @throws InputMismatchException Will be thrown, if the effect value is not between 0 -> 1.
     */
    public double set_effect(double effect) throws InputMismatchException {
        if (effect >= 0 && effect <= 1)
            _effect = effect;
        else
            throw new InputMismatchException();

        return effect;
    }

    /**
     * An enum of the different values a SubscriptionOffer can be of.
     */
    public enum Type {
        FREE_TRIAL,
        SALE
    }
}
