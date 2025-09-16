package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class ContactInfoJoins {

    public static final Join
        LEFT_PHONE_TO_CONTACT_INFO = left(PHONE, CONTACT_INFO),
            LEFT_ADDRESS_TO_CONTACT_INFO = left(ADDRESS, CONTACT_INFO),
            LEFT_COUNTRY_TO_CONTACT_INFO = left(COUNTRY, CONTACT_INFO);
}
