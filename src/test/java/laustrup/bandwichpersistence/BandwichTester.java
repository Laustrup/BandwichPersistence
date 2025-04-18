package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.quality_assurance.Tester;
import org.junit.jupiter.api.BeforeAll;

import static laustrup.bandwichpersistence.Program.testMode;

public class BandwichTester extends Tester {

    @BeforeAll
    protected static void beforeAll() {
        testMode("bandwich");
    }
}
