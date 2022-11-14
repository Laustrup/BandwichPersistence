package laustrup.bandwichpersistence.models.users.sub_users.subscriptions;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Defines the kind of subscription a user is having.
 * Only Artists and Bands can have a paying subscription
 */
@ToString
public class Subscription {

    @Getter
    private User _user;
    @Getter
    private SubscriptionType _type;
    @Getter @Setter
    private SubscriptionStatus _status;
    @Getter
    private int _price;
    @Getter
    private long _cardId;

    public Subscription(User user, SubscriptionType type, SubscriptionStatus status, long cardId) {
        _user = user;
        _type = defineType(type);
        _status = status;
        _cardId = cardId;
    }

    public SubscriptionType set_type(SubscriptionType type) { return defineType(type); }

    private SubscriptionType defineType(SubscriptionType type) {
        _type = type;
        if (_user.getClass() != Artist.class && _user.getClass() != Band.class) {
            switch (_type) {
                case PREMIUM_BAND -> {
                    if (_user.getClass() == Band.class) _price = 100;
                }
                case PREMIUM_ARTIST -> {
                    if (_user.getClass() == Artist.class) _price = 60;
                }
                default -> _price = 0;
            }
        }
        return _type;
    }

    public enum SubscriptionType {
        FREEMIUM,
        PREMIUM_BAND,
        PREMIUM_ARTIST
    }

    public enum SubscriptionStatus {
        ACCEPTED,
        BLOCKED,
        DISACTIVATED,
        CLOSED
    }
}
