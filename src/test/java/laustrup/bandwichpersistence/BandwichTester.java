package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.quality_assurance.Tester;
import org.junit.jupiter.api.AfterEach;

import java.sql.SQLException;

import static laustrup.bandwichpersistence.Program.testMode;
import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.Action.ROOT_PATH;
import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.execute;

public class BandwichTester extends Tester {

    private final String _schema = "bandwich_test";

    private final Query _cleanseDatabaseQuery = new Query(/*language=MySQL*/ "drop database " + _schema);

    protected final String _testPassword = "123";

    private boolean _mockedTest = false;

    @Override
    protected void mocking() {
        _mockedTest = true;
        testMode(_schema);
    }

    @AfterEach
    void afterEach() {
        if (_mockedTest) {
            try {
                execute(_cleanseDatabaseQuery, ROOT_PATH);
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
        _mockedTest = false;
    }
}
