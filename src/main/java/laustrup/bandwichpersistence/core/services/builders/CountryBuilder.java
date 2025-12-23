package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryBuilder extends BandwichBuilderService<ContactInfo.Country> {

  private static CountryBuilder _instance;

  public static CountryBuilder get_instance() {
    if (_instance == null)
      _instance = new CountryBuilder();

    return _instance;
  }

  @Override
  protected void completion(ContactInfo.Country reference, ContactInfo.Country object) {

  }
}
