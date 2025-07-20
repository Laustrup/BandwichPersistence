package laustrup.bandwichpersistence.items;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Address;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Country;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Phone;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.services.TableAnnotationService.get_tableTitle;
import static laustrup.bandwichpersistence.items.TestItems.generateUUID;

public class ContactInfoTestItems {

    public static ContactInfo generateContactInfo(
            String email,
            Seszt<Phone> phones,
            Address address,
            Country country
    ) {
        String table = get_tableTitle(ContactInfo.class);

        return new ContactInfo(
                new ContactInfo.Id(generateUUID(
                        table,
                        selecting(new Properties(
                                table,
                                complying().which(Condition.of(Field.of(table, ContactInfo.DTO.Fields.email), EQUALS, email))
                        ))
                )),
                email,
                phones,
                address,
                country
        );
    }

    public static Phone generatePhone(
            int countryDigits,
            int numbers,
            boolean isMobile,
            boolean isBusiness
    ) {
        return new Phone(
                countryDigits,
                numbers,
                isMobile,
                isBusiness
        );
    }

    public static Address generateAddress(
            String street,
            String floor,
            String municipality,
            String zip,
            String city
    ) {
        String table = get_tableTitle(Address.class);

        return new Address(
                new Address.Id(generateUUID(
                        table,
                        complying().which(Condition.of(
                                Field.of(table, Address.DTO.Fields.street),
                                EQUALS,
                                street
                        ))
                )),
                street,
                floor,
                municipality,
                zip,
                city
        );
    }

    public static Country generateCountry(String title, String code) {
        String table = get_tableTitle(Country.class);

        return new Country(
                new Country.Id(generateUUID(
                        table,
                        complying().which(Condition.of(
                                Field.of(table, Country.DTO.Fields.title),
                                EQUALS,
                                title
                        ))
                )),
                title,
                code
        );
    }
}
