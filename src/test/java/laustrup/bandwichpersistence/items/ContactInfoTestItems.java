package laustrup.bandwichpersistence.items;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Address;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Phone;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import laustrup.bandwichpersistence.items.OrganisationTestItems.OrganisationTitle;

import java.util.Locale;
import java.util.UUID;

public class ContactInfoTestItems {

  public static ContactInfo generateContactInfo(OrganisationTitle organisationTitle) {
    return switch (organisationTitle) {
      case IVÆRKSTED -> iværkstedetContactInfo();
      case ARENA, TWOGETHER, JAMSTER -> null;
    };
  }

  private static ContactInfo iværkstedetContactInfo() {
    return new ContactInfo(
        new ContactInfo.Id(UUID.fromString("11111111-1111-1111-a111-111111111112")),
        "contact@ivaerkstedet.dk",
        new Seszt<>(new Phone(
            45,
            12345678,
            true,
            true
        )),
        iværkstedetAddress(),
        Locale.of("da", "DK")
    );
  }

  private static Address iværkstedetAddress() {
    return new Address(
        new Address.Id(UUID.fromString("11111111-1111-1111-a111-111111111111")),
        "Værkstedsvej 57",
        null,
        "Sjælland",
        "4600",
        "Køge"
    );
  }
}
