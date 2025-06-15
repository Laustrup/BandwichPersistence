package laustrup.bandwichpersistence.items;

import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.Subscription.*;

import java.util.UUID;

public class SubscriptionTestItems {

    public static Subscription generateSubscription(
            UUID id,
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
