package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class SubscriptionJoins {

    public static final Join
            LEFT_SUBSCRIPTION = left(
                    SUBSCRIPTION.get_title(),
                    Condition.equals(DatabaseField.of(ARTIST, SUBSCRIPTION.get_idReference()), DatabaseField.of(SUBSCRIPTION)),
                    Condition.equals(DatabaseField.of(ORGANISATION_EMPLOYEE, SUBSCRIPTION.get_idReference()), DatabaseField.of(SUBSCRIPTION))
            );
}
