package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneBuilder extends BandwichBuilderService<ContactInfo.Phone> {

  private static PhoneBuilder _instance;

  public static PhoneBuilder get_instance() {
    if (_instance == null)
      _instance = new PhoneBuilder();

    return _instance;
  }

  @Override
  protected void completion(ContactInfo.Phone reference, ContactInfo.Phone object) {

  }
}
