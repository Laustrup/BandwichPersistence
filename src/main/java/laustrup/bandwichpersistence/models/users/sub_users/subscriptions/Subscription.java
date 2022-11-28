package laustrup.bandwichpersistence.models.users.sub_users.subscriptions;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Defines the kind of subscription a user is having.
 * Only Artists and Bands can have a paying subscription.
 */
@ToString
public class Subscription extends Model {

    /**
     * The User that uses this Subscription.
     */
    @Getter
    private User _user;

    /**
     * An enum that contains different kinds of types, that this Subscription can be.
     * Will also determine the price.
     */
    @Getter
    private Type _type;

    /**
     * An enum that determines what kind of status, the situation of the Subscription is in.
     */
    @Getter @Setter
    private Status _status;

    /**
     * How much the User should pay per month.
     */
    private int _price;

    /**
     * An object class that specifies the offer if any, that this Subscription has.
     */
    @Getter @Setter
    private SubscriptionOffer _offer;

    /**
     * The id in the database of the card, that is connected to this subscription.
     * Is only the id and not the object for security reasons.
     */
    @Getter
    private Long _cardId;

    public Subscription(User user, Type type, Status status, SubscriptionOffer offer, Long cardId, LocalDateTime timestamp) {
        super(user.get_primaryId(), cardId, user.get_username()+"-Subscription: " + user.get_primaryId(), timestamp);
        _type = defineType(type);
        _status = status;
        _offer = offer;
        _cardId = cardId;
    }

    public Subscription(long id, Type type, Status status, SubscriptionOffer offer, Long cardId) {
        super(id, cardId, "Unkown_user-Subscription: " + id);
        _type = defineType(type);
        _status = status;
        _offer = offer;
        _cardId = cardId;
        _timestamp = null;
    }

    public Subscription(User user, Type type, Status status, SubscriptionOffer offer, Long cardId) {
        super(user.get_title() + "-Subscription");
        _user = user;
        _type = defineType(type);
        _status = status;
        _offer = offer;
        _cardId = cardId;
    }

    /**
     * Set the id of the card, that is connection to this Subscription,
     * but will only do so, if the id is not already set.
     * @param id The id value that will be set as the connection between this subscription and the card of that id.
     * @return The id of the card, that is connected to this Subscription.
     */
    public Long set_cardId(long id) {
        if (_cardId==null)
            _cardId = id;

        return _cardId;
    }

    /**
     * This is only meant to be used in User class after assemble.
     * Will only set the User, if the User is null.
     * @param user The User that will be set for this Subscription.
     * @return The User of this Subscription.
     */
    public User set_user(User user) {
        if (user == null)
            _user = user;

        return _user;
    }

    /**
     * Uses the defineType() method to set the type and also the price from the new type value.
     * @param type An enum of a SubscriptionType, that is wished to be set.
     * @return The SubscriptionType of this Subscription.
     */
    public Type set_type(Type type) { return defineType(type); }

    /**
     * Sets the type of this Subscription and also determines the price for that type of Subscription.
     * @param type An enum of a type of Subscription, that is wished to be set.
     * @return This Subscription's type as the enum.
     */
    private Type defineType(Type type) {
        _type = type;

        if (_user.getClass() != Artist.class && _user.getClass() != Band.class) {
            switch (_type) {
                case PREMIUM_BAND -> {
                    if (_user.getClass() == Band.class) _price = 100;
                }
                case PREMIUM_ARTIST -> {
                    if (_user.getClass() == Artist.class && ((Artist) _user).get_bands().size() == 0) _price = 60;
                }
                default -> _price = 0;
            }
        }
        return _type;
    }

    /**
     * Checks if there is a free trial offer or the User is either a Band or Artist,
     * since they are the only paying Users.
     * Also calculates the Offer effect with the price.
     *
     * @return The price per month of the User as a double, since it is multiplied with the Offer effect.
     */
    public double get_price() {
        if (_user != null &&
                ((_user.getClass() == Band.class || _user.getClass() == Artist.class) &&
                (_offer.get_type() != SubscriptionOffer.Type.FREE_TRIAL && !isOfferExpired())))
            return isOfferExpired() ? _price : _price * _offer.get_effect();
        else return 0;
    }

    /**
     * Determines whether the date that this offer's date has been reached or not.
     * Counts from LocalDateTime.now.
     * @return True if the moment now is after the date that the offer of this Subscription will expire, otherwise false.
     */
    public boolean isOfferExpired() { return LocalDateTime.now().isAfter(_offer.get_expires()); }

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
