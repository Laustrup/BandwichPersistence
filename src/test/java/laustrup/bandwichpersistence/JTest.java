package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.utilities.Printer;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Adds a few functions to test methods to reuse.
 */
public abstract class JTest {

    /**
     * Will be the start of the ACT in tests
     * Needs to be set before the ACT.
     * Is use for checking performance
     */
    protected LocalDateTime _start;

    /**
     * Contains different generated items to use for testing.
     * Are being reset for each method.
     */
    protected TestItems _items;

    /**
     * Will automatically begin the time of start.
     * Uses the @BeforeEach notation.
     * If there is an ARRANGE, please use begin() before ACT,
     * in order not to get an untrustworthy performance time result.
     */
    @BeforeEach
    public void setup() {
        _items = new TestItems();
        begin();
    }

    /**
     * Sets the start time of ACT for measuring of performance time.
     * Must only use before act.
     * Is also used in @BeforeEach, so in case of no ARRANGE, this method is not needed.
     */
    protected void begin() { _start = LocalDateTime.now(); }

    /**
     * Calculates the performance time from start to this moment and prints it in milliseconds.
     * @return The duration of the performance in milliseconds
     */
    protected long calculatePerformance() {
        long performance = Duration.between(_start, LocalDateTime.now()).toMillis();

        Printer.get_instance().print("The performance of current test is " + performance +
                " in milliseconds and " + (performance / 1000) + " in minutes." );

        return performance;
    }

    protected void compare(Participant expected, Participant actual) {
    }

    /*
    private void compare(User expected, User actual) {
        assertEquals(expected.,);
    }
     */
}
