package laustrup.bandwichpersistence.items;

import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.Subscription.*;
import laustrup.bandwichpersistence.core.models.users.User;

public class SubscriptionTestItems {

    public static Subscription generateSubscription(
            User.Id id,
            Status status,
            Kind kind,
            UserType userType
    ) {
        return new Subscription(
                new Id(id.get_signature()),
                status,
                kind,
                userType
        );
    }

    public static Subscription generateSubscription(
            Id id,
            Status status,
            Kind kind,
            UserType userType
    ) {
        return new Subscription(
            id,
            status,
            kind,
            userType
        );
    }
}
