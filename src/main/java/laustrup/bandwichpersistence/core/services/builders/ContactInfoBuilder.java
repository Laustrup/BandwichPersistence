package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactInfoBuilder extends BandwichBuilderService<ContactInfo> {

  private static ContactInfoBuilder _instance;

  public static ContactInfoBuilder get_instance() {
    if (_instance == null)
      _instance = new ContactInfoBuilder();

    return _instance;
  }

  @Override
  protected void completion(ContactInfo reference, ContactInfo object) {
    combine(reference.get_phones(), object.get_phones());
  }
}
