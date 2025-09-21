package laustrup.bandwichpersistence.items;

import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Address;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Country;
import laustrup.bandwichpersistence.core.models.users.ContactInfo.Phone;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import static laustrup.bandwichpersistence.core.persistence.DatabaseField.Configuration.databaseFieldConfiguration;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.items.TestItems.generateUUID;

public class ContactInfoTestItems {

    public static ContactInfo generateContactInfo(
            String email,
            Seszt<Phone> phones,
            Address address,
            Country country
    ) {
        Class<?> clazz = ContactInfo.class;

        return new ContactInfo(
                new ContactInfo.Id(generateUUID(
                        clazz,
                        selecting(new Properties(
                                clazz,
                                complying().which(Condition.equals(
                                        DatabaseField.of(databaseFieldConfiguration(ContactInfo.class, ContactInfo.DTO.Fields.email)),
                                        email
                                ))
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
        Class<?> clazz = Address.class;

        return new Address(
                new Address.Id(generateUUID(
                        clazz,
                        complying().which(Condition.equals(
                                DatabaseField.of(databaseFieldConfiguration(clazz, Address.Fields._street)),
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
        Class<?> clazz = Country.class;

        return new Country(
                new Country.Id(generateUUID(
                        clazz,
                        complying().which(Condition.equals(
                                DatabaseField.of(databaseFieldConfiguration(clazz, Country.DTO.Fields.title)),
                                title
                        ))
                )),
                title,
                code
        );
    }
}
