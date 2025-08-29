package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class ContactInfoJoins {

    public static final Join
        LEFT_PHONE_TO_CONTACT_INFO = left(PHONE.get_title(), Condition.equals(
            DatabaseField.of(PHONE, CONTACT_INFO.get_idReference()),
            DatabaseField.of(CONTACT_INFO)
    )), LEFT_ADDRESS_TO_CONTACT_INFO = left(ADDRESS.get_title(), Condition.equals(
            DatabaseField.of(ADDRESS),
            DatabaseField.of(CONTACT_INFO, ADDRESS.get_idReference())
    )), LEFT_COUNTRY_TO_CONTACT_INFO = left(COUNTRY.get_title(), Condition.equals(
            DatabaseField.of(COUNTRY),
            DatabaseField.of(CONTACT_INFO, COUNTRY.get_idReference())
    ));
}
