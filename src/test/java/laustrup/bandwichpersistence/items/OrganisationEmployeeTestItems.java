package laustrup.bandwichpersistence.items;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.time.Instant;

import static laustrup.bandwichpersistence.core.models.Organisation.Employee.Role.LEADER;
import static laustrup.bandwichpersistence.core.models.Subscription.Kind.PAYING;
import static laustrup.bandwichpersistence.core.models.Subscription.Status.ACCEPTED;
import static laustrup.bandwichpersistence.core.models.Subscription.UserType.ORGANISATION_EMPLOYEE;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityDataService.get_tableTitle;
import static laustrup.bandwichpersistence.core.services.DatabaseEntityDataService.toAlias;
import static laustrup.bandwichpersistence.items.OrganisationTestItems.generateIværkstedContactInfo;
import static laustrup.bandwichpersistence.items.SubscriptionTestItems.generateSubscription;
import static laustrup.bandwichpersistence.items.TestItems.generateUUID;

public class OrganisationEmployeeTestItems {

    public static Employee generateOrganisationEmployee(OrganisationEmployeeTitle title) throws NotImplementedException {
        return switch (title) {
            case JENS_JENSEN -> generateJensJensen();
            case BIRTHE_BERTHELSEN, HANS_HANSEN, SIMONE_SIMONSEN, JOANNA_EDEL_JOHANSEN, JOHN_JOHNSON, JAMES_JAMERSON,
                 XI_XANG, JIMMY_JENSEN, HANNE_HANSEN, TUE_TIRSDAG -> throw new NotImplementedException(title.name() + " employee test data not implemented yet!");
            case null -> null;
        };
    }

    private static Employee generateJensJensen() throws NotImplementedException {
        String email = "jens@ivaerkstedet.dk";
        Employee.Id id = generateEmployeeId(email);

        return new Employee(
                id,
                "jens",
                "Jens",
                "Jensen",
                "Jeg hedder Jens",
                generateIværkstedContactInfo("contact@ivaerkstedet.dk"),
                generateSubscription(id, ACCEPTED, PAYING, ORGANISATION_EMPLOYEE),
                new Seszt<>(LEADER),
                new Seszt<>(),
                new Seszt<>(),
                new Seszt<>(),
                null,
                Instant.now()
        );
    }

    private static Employee.Id generateEmployeeId(String email) {
        String
                employeeTable = get_tableTitle(Employee.class),
                contactInfoTable = get_tableTitle(ContactInfo.class);

        return new Employee.Id(generateUUID(
                employeeTable,
                selecting(new Properties(
                        employeeTable,
                        complying()
                                .which(Condition.of(
                                        DatabaseField.of(
                                                toAlias(contactInfoTable),
                                                ContactInfo.DTO.Fields.email
                                        ),
                                        EQUALS,
                                        email
                                ))
                )).addJoin(Join.inner(
                        contactInfoTable,
                        DatabaseField.of(toAlias(contactInfoTable), Model.ModelDTO.Fields.id),
                        DatabaseField.of(toAlias(employeeTable), fieldToColumnName(ContactInfo.class.getSimpleName() + "_id"))
                ))
        ));
    }

    public enum OrganisationEmployeeTitle {
        JENS_JENSEN,
        BIRTHE_BERTHELSEN,
        HANS_HANSEN,
        SIMONE_SIMONSEN,
        JOANNA_EDEL_JOHANSEN,
        JOHN_JOHNSON,
        JAMES_JAMERSON,
        XI_XANG,
        JIMMY_JENSEN,
        HANNE_HANSEN,
        TUE_TIRSDAG
    }
}
