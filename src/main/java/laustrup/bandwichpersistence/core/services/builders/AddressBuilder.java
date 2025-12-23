package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressBuilder extends BandwichBuilderService<ContactInfo.Address> {

  private static AddressBuilder _instance;

  public static AddressBuilder get_instance() {
    if (_instance == null)
      _instance = new AddressBuilder();

    return _instance;
  }

  @Override
  protected void completion(ContactInfo.Address reference, ContactInfo.Address object) {

  }
}
