package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Subscription;

import java.util.UUID;

public class SubscriptionBuilder extends BuilderService<Subscription> {

  private static SubscriptionBuilder _instance;

  public static SubscriptionBuilder get_instance() {
    if (_instance == null)
      _instance = new SubscriptionBuilder();

    return _instance;
  }

  private SubscriptionBuilder() {

  }

  @Override
  protected void completion(Subscription reference, Subscription object) {

  }

  @Override
  protected Subscription construct() {
    return new Subscription(
        new Subscription.Id((UUID) get_field(Model.Fields._identity)),
        get_field(Subscription.Fields._status),
        get_field(Subscription.Fields._kind),
        get_field(Subscription.Fields._userType)
    );
  }
}
