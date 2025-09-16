package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class AuthorizationJoins {

    public static final Join
            LEFT_ORGANISATION_EMPLOYEE_AUTHORIZATIONS = left(ORGANISATION_EMPLOYEE_AUTHORIZATION, ORGANISATION_EMPLOYEE),
            LEFT_ORGANISATION_ARTIST_AUTHORIZATIONS = left(ARTIST_AUTHORIZATION, ARTIST),
            LEFT_AUTHORITIES = left(AUTHORITY, ARTIST_AUTHORIZATION, ORGANISATION_EMPLOYEE_AUTHORIZATION);
}
