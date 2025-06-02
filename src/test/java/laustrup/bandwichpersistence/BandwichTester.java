package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.quality_assurance.Tester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

import static laustrup.bandwichpersistence.Program.testMode;
import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.Action.ROOT_PATH;
import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.execute;

public class BandwichTester extends Tester {

    private final String _schema = "bandwich_test";

    private final Query _cleanseDatabaseQuery = new Query(/*language=MySQL*/ "drop database " + _schema);

    @BeforeEach
    public void beforeEach() {
        testMode(_schema);
    }

    @AfterEach
    void afterEach() {
        try {
            execute(_cleanseDatabaseQuery, ROOT_PATH);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
