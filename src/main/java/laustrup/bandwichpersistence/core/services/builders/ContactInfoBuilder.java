package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;

import java.util.UUID;

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

  @Override
  protected ContactInfo construct() {
    return new ContactInfo(
        new ContactInfo.Id((UUID) get_field(Model.Fields._identity)),
        get_field(ContactInfo.Fields._email),
        get_field(ContactInfo.Fields._phones),
        get_field(ContactInfo.Fields._address),
        get_field(ContactInfo.Fields._country)
    );
  }
}
