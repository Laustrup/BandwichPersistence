package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;

public class AddressBuilder extends BandwichBuilderService<ContactInfo.Address> {

  private static AddressBuilder _instance;

  public static AddressBuilder get_instance() {
    if (_instance == null)
      _instance = new AddressBuilder();

    return _instance;
  }

  private AddressBuilder() {
  }

  @Override
  protected void completion(ContactInfo.Address reference, ContactInfo.Address object) {

  }
}
