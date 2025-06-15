package laustrup.bandwichpersistence.core.managers;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.BandwichTester;
import laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems.OrganisationEmployeeTitle.JENS_JENSEN;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class UserDetailsManagerTests extends BandwichTester {

    @Test
    void canGetOrganisationEmployee() {
        String email = "john@arena.com";

        test(() -> {
            Employee expected;

            try {
                expected = arrange(OrganisationEmployeeTestItems.generateOrganisationEmployee(JENS_JENSEN));
            } catch (NotImplementedException e) {
                throw new RuntimeException(e);
            }

            UserDetails actual = act(UserDetailsManager.getUserDetails(email));

            assertingOrganisationEmployee(expected, actual);
        });
    }

    private void assertingOrganisationEmployee(Employee expected, UserDetails actual) {
        asserting(expected)
                .is(ex -> ex.get_username().equals(actual.getUsername()));
    }
}