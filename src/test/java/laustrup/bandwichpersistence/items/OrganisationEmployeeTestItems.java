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
        Class<?> employeeClass = Employee.class,
                ContactInfoClass = ContactInfo.class;

        return new Employee.Id(generateUUID(
                employeeClass,
                selecting(new Properties(
                        employeeClass.getSimpleName(),
                        complying()
                                .which(Condition.of(
                                        DatabaseField.of(new DatabaseField.Configuration(
                                                ContactInfoClass,
                                                ContactInfo.Fields._email
                                        )),
                                        EQUALS,
                                        email
                                ))
                )).addJoin(Join.inner(
                        ContactInfoClass.getSimpleName(),
                        DatabaseField.of(new DatabaseField.Configuration(ContactInfoClass, Model.ModelDTO.Fields.id)),
                        DatabaseField.of(new DatabaseField.Configuration(employeeClass, fieldToColumnName(ContactInfo.class.getSimpleName() + "_id")))
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
