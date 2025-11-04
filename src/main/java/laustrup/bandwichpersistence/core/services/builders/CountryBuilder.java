package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;

import java.util.UUID;

public class CountryBuilder extends BuilderService<ContactInfo.Country> {

  private static CountryBuilder _instance;

  public static CountryBuilder get_instance() {
    if (_instance == null)
      _instance = new CountryBuilder();

    return _instance;
  }

  private CountryBuilder() {

  }

  @Override
  protected void completion(ContactInfo.Country reference, ContactInfo.Country object) {

  }

  @Override
  protected ContactInfo.Country construct() {
    return new ContactInfo.Country(
        new ContactInfo.Country.Id((UUID) get_field(Model.Fields._identity)),
        get_field(ContactInfo.Country.Fields.title),
        get_field(ContactInfo.Country.Fields.code)
    );
  }
}
