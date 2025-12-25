package laustrup.bandwichpersistence.core.managers;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems;
import org.junit.jupiter.api.Test;

import static laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems.OrganisationEmployeeTitle.JENS_JENSEN;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class UserDetailsManagerTests extends BandwichTester {

  @Test
  void canGetOrganisationEmployee() {
    mocked(() -> {
      Login login;
      Employee expected;

      try {
        expected = arrange(OrganisationEmployeeTestItems.generateOrganisationEmployee(JENS_JENSEN));
        login = new Login(expected.get_contactInfo().get_email(), _testPassword);
      } catch (NotImplementedException e) {
        throw new RuntimeException(e);
      }

      Employee actual = act((Employee) UserDetailsManager.getUser(login).get_object());

      asserting(expected)
          .compare(actual);
    });
  }
}