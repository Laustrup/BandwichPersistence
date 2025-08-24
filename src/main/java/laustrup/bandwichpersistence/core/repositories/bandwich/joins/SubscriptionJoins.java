package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class SubscriptionJoins {

    public static final Join
            LEFT_SUBSCRIPTION = left(
                    SUBSCRIPTION.get_title(),
                    Condition.equals(Field.of(ARTIST, SUBSCRIPTION.get_idReference()), Field.of(SUBSCRIPTION)),
                    Condition.equals(Field.of(ORGANISATION_EMPLOYEE, SUBSCRIPTION.get_idReference()), Field.of(SUBSCRIPTION))
            );
}
