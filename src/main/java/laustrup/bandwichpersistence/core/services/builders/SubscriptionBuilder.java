package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Subscription;

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
}
