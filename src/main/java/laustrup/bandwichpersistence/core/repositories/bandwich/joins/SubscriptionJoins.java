package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class SubscriptionJoins {

  public static final Join
      LEFT_SUBSCRIPTION = left(SUBSCRIPTION, ARTIST, ORGANISATION_EMPLOYEE);
}
