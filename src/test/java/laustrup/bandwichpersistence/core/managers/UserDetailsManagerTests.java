package laustrup.bandwichpersistence.core.managers;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.quality_assurance.Asserter;
import org.junit.jupiter.api.Test;

import static laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems.OrganisationEmployeeTitle.JENS_JENSEN;

class UserDetailsManagerTests extends BandwichTester {

    @Test
    void canGetOrganisationEmployee() {
        String email = "john@arena.com";

        mocked(() -> {
            Login login = new Login(email, _testPassword);
            Employee expected;

            try {
                expected = arrange(OrganisationEmployeeTestItems.generateOrganisationEmployee(JENS_JENSEN));
            } catch (NotImplementedException e) {
                throw new RuntimeException(e);
            }

            Employee actual = act((Employee) UserDetailsManager.getUser(login).get_object());

            Asserter.asserting(expected)
                    .compare(actual);
        });
    }
}