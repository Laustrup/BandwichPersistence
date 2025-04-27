package laustrup.bandwichpersistence;

import org.junit.jupiter.api.BeforeAll;

import static laustrup.bandwichpersistence.Program.testMode;

public class TestPreparation {

    @BeforeAll
    protected static void beforeAll() {
        testMode("bandwich");
    }
}
