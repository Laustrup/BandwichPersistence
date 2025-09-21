package laustrup.bandwichpersistence.items;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
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
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.Configuration.databaseFieldConfiguration;
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
        return new Employee.Id(generateUUID(
                Employee.class,
                selecting(new Properties(
                        Employee.class.getSimpleName(),
                        complying()
                                .which(Condition.equals(
                                        DatabaseField.of(databaseFieldConfiguration(
                                                ContactInfo.class,
                                                ContactInfo.Fields._email
                                        )),
                                        email
                                ))
                )).addJoin(Join.inner(
                        ContactInfo.class,
                        DatabaseField.of(databaseFieldConfiguration(ContactInfo.class, ContactInfo.Fields._id)),
                        DatabaseField.referenceOf(Employee.class, ContactInfo.class)
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
