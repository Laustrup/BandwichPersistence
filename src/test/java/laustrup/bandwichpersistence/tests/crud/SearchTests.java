package laustrup.bandwichpersistence.tests.crud;

import laustrup.bandwichpersistence.tests.Tester;
import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.services.RandomCreatorService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTests extends Tester<String, Object> {

    @Test
    void canSearch() {
        test(t -> {
            String query = (String) arrange(
                e -> RandomCreatorService.get_instance().generateSubString(
                        Assembly.get_instance().getUser(1).get_username()
                     )
            );

            Search search = (Search) act(e -> Assembly.get_instance().search(query), "search " + query);

            assertTrue(search != null && (search.getUsers().length > 0));

            return true;
        });
    }
}
