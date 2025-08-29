package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class AuthorizationJoins {

    public static final Join
            LEFT_ORGANISATION_EMPLOYEE_AUTHORIZATIONS = left(ORGANISATION_EMPLOYEE_AUTHORIZATION.get_title(), Condition.equals(
                    DatabaseField.of(ORGANISATION_EMPLOYEE_AUTHORIZATION, ORGANISATION_EMPLOYEE.get_idReference()),
                    DatabaseField.of(ORGANISATION_EMPLOYEE)
            )), LEFT_ORGANISATION_ARTIST_AUTHORIZATIONS = left(ARTIST_AUTHORIZATION.get_title(), Condition.equals(
                    DatabaseField.of(ARTIST_AUTHORIZATION, ARTIST.get_idReference()),
                    DatabaseField.of(ARTIST)
            )), LEFT_AUTHORITIES = left(
                    AUTHORITY.get_title(),
                    Condition.equals(DatabaseField.of(AUTHORITY), DatabaseField.of(ARTIST_AUTHORIZATION, AUTHORITY.get_idReference())),
                    Condition.equals(DatabaseField.of(AUTHORITY), DatabaseField.of(ORGANISATION_EMPLOYEE_AUTHORIZATION, AUTHORITY.get_idReference()))
            );
}
