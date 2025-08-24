package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class OrganisationJoins {

    public static final Join
            LEFT_ORGANISATION_EMPLOYEE_TO_CONTACT_INFO = left(ORGANISATION_EMPLOYEE.get_title(), Condition.equals(
                    Field.of(ORGANISATION_EMPLOYEE, CONTACT_INFO.get_idReference()),
                    Field.of(CONTACT_INFO)
            )), LEFT_ORGANISATION_EMPLOYMENT_TO_EMPLOYEE = left(ORGANISATION_EMPLOYMENT.get_title(), Condition.equals(
                    Field.of(ORGANISATION_EMPLOYMENT, ORGANISATION_EMPLOYEE.get_idReference()),
                    Field.of(ORGANISATION_EMPLOYEE)
            ))
    ;
}
