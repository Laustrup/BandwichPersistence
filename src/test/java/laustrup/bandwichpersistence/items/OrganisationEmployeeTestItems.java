package laustrup.bandwichpersistence.items;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import laustrup.bandwichpersistence.items.OrganisationTestItems.OrganisationTitle;

import java.time.Instant;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.models.Organisation.Employee.Role.LEADER;
import static laustrup.bandwichpersistence.core.models.Subscription.Kind.PAYING;
import static laustrup.bandwichpersistence.core.models.Subscription.Status.ACCEPTED;
import static laustrup.bandwichpersistence.core.models.Subscription.UserType.ORGANISATION_EMPLOYEE;
import static laustrup.bandwichpersistence.items.ContactInfoTestItems.generateContactInfo;
import static laustrup.bandwichpersistence.items.SubscriptionTestItems.generateSubscription;

public class OrganisationEmployeeTestItems {

  public static Employee generateOrganisationEmployee(OrganisationEmployeeTitle title) throws NotImplementedException {
    return switch (title) {
      case JENS_JENSEN -> generateJensJensen();
      case BIRTHE_BERTHELSEN, HANS_HANSEN, SIMONE_SIMONSEN, JOANNA_EDEL_JOHANSEN, JOHN_JOHNSON, JAMES_JAMERSON,
           XI_XANG, JIMMY_JENSEN, HANNE_HANSEN, TUE_TIRSDAG ->
          throw new NotImplementedException(title.name() + " employee test data not implemented yet!");
      case null -> null;
    };
  }

  private static Employee generateJensJensen() {
    Employee.Id id = new Employee.Id(UUID.fromString("11111111-1111-1111-a111-111111111113"));

    return new Employee(
        id,
        "jens",
        "Jens",
        "Jensen",
        "Jeg hedder Jens",
        generateContactInfo(OrganisationTitle.IVÆRKSTED),
        generateSubscription(id, ACCEPTED, PAYING, ORGANISATION_EMPLOYEE),
        new Seszt<>(LEADER),
        new Seszt<>(),
        new Seszt<>(),
        new Seszt<>(),
        Instant.now()
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
