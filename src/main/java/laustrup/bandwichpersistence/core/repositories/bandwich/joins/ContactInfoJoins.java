package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.*;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.*;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.*;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class ContactInfoJoins {

    public static final Join
        LEFT_PHONE_TO_CONTACT_INFO = left(PHONE.get_title(), Condition.equals(
            Field.of(PHONE, CONTACT_INFO.get_idReference()),
            Field.of(CONTACT_INFO)
    )), LEFT_ADDRESS_TO_CONTACT_INFO = left(ADDRESS.get_title(), Condition.equals(
            Field.of(ADDRESS),
            Field.of(CONTACT_INFO, ADDRESS.get_idReference())
    )), LEFT_COUNTRY_TO_CONTACT_INFO = left(COUNTRY.get_title(), Condition.equals(
            Field.of(COUNTRY),
            Field.of(CONTACT_INFO, COUNTRY.get_idReference())
    ));
}
