package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;

import java.util.UUID;

public class AddressBuilder extends BuilderService<ContactInfo.Address> {

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

  @Override
  protected ContactInfo.Address construct() {
    return new ContactInfo.Address(
        new ContactInfo.Address.Id((UUID) get_field(Model.Fields._identity)),
        get_field(ContactInfo.Address.Fields._street),
        get_field(ContactInfo.Address.Fields._floor),
        get_field(ContactInfo.Address.Fields._municipality),
        get_field(ContactInfo.Address.Fields._zip),
        get_field(ContactInfo.Address.Fields._city)
    );
  }
}
