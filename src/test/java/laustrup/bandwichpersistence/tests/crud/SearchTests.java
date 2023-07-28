package laustrup.bandwichpersistence.tests.crud;

import laustrup.bandwichpersistence.tests.PersistenceTester;
import laustrup.models.Search;
import laustrup.services.RandomCreatorService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTests extends PersistenceTester<Object> {

    @Test
    void canSearch() {
        test(() -> {
            String query = (String) arrange(
                () -> RandomCreatorService.get_instance().generateSubString(
                    Assembly.get_instance().getUser(1).get_username()
                )
            );

            Search search = (Search) act(() -> Assembly.get_instance().search(query), "search " + query);

            assertTrue(search != null && (search.getUsers().length > 0));
        });
    }
}
