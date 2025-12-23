package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Subscription;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionBuilder extends BandwichBuilderService<Subscription> {

  private static SubscriptionBuilder _instance;

  public static SubscriptionBuilder get_instance() {
    if (_instance == null)
      _instance = new SubscriptionBuilder();

    return _instance;
  }

  @Override
  protected void completion(Subscription reference, Subscription object) {

  }
}
