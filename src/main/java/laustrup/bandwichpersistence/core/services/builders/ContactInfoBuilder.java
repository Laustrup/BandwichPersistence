package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;

public class ContactInfoBuilder extends BuilderService<ContactInfo> {

  private static ContactInfoBuilder _instance;

  public static ContactInfoBuilder get_instance() {
    if (_instance == null)
      _instance = new ContactInfoBuilder();

    return _instance;
  }

  private ContactInfoBuilder() {

  }

  @Override
  protected void completion(ContactInfo reference, ContactInfo object) {
    combine(reference.get_phones(), object.get_phones());
  }
}
