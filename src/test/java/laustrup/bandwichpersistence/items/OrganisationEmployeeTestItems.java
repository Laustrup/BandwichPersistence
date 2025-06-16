package laustrup.bandwichpersistence.items;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.services.StringService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.time.Instant;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.models.Organisation.Employee.Role.LEADER;
import static laustrup.bandwichpersistence.core.models.Subscription.Kind.PAYING;
import static laustrup.bandwichpersistence.core.models.Subscription.Status.ACCEPTED;
import static laustrup.bandwichpersistence.core.models.Subscription.UserType.ORGANISATION_EMPLOYEE;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.Area.INNER;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition.Equation.EQUALS;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.complying;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.services.StringService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.services.TableAnnotationService.get_tableTitle;
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
        UUID id = generateEmployeeId(email);

        return new Employee(
                id,
                "jens",
                "Jens",
                "Jensen",
                "Jeg hedder Jens",
                generateIværkstedContactInfo("contact@ivaerkstedet.dk"),
                generateSubscription(
                        id,
                        ACCEPTED,
                        PAYING,
                        ORGANISATION_EMPLOYEE
                ),
                new Seszt<>(LEADER),
                new Seszt<>(),
                new Seszt<>(),
                new Seszt<>(),
                new History(History.JoinTableDetails.ORGANISATION_EMPLOYEE),
                Instant.now()
        );
    }

    private static UUID generateEmployeeId(String email) {
        String
                employeeTable = get_tableTitle(Employee.class),
                contactInfoTable = get_tableTitle(ContactInfo.class);

        return generateUUID(
                employeeTable,
                selecting(new Properties(
                        employeeTable,
                        complying()
                                .that(Condition.of(
                                        Field.of(
                                                contactInfoTable,
                                                ContactInfo.DTO.Fields.email
                                        ),
                                        EQUALS,
                                        email
                                ))
                )).addJoin(Join.of(
                        INNER,
                        Field.of(
                                contactInfoTable,
                                Model.ModelDTO.Fields.id
                        ),
                        Field.of(
                                employeeTable,
                                fieldToColumnName(ContactInfo.class.getSimpleName() + Model.Fields._id)
                        )
                ))
        );
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
