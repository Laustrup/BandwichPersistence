package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;

public class PhoneBuilder extends BuilderService<ContactInfo.Phone> {

  private static PhoneBuilder _instance;

  public static PhoneBuilder get_instance() {
    if (_instance == null)
      _instance = new PhoneBuilder();

    return _instance;
  }

  private PhoneBuilder() {

  }

  @Override
  protected void completion(ContactInfo.Phone reference, ContactInfo.Phone object) {

  }
}
