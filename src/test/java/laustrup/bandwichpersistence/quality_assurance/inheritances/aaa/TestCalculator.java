package laustrup.quality_assurance.inheritances.aaa;

import java.time.Duration;
import java.time.LocalDateTime;

/** Will be used to calculate performances of tests with a beginning and an end. */
public abstract class TestCalculator {

    /** The content of all the data observations of the test. */
    protected String _print = "-- Information of the test";

    /**
     * Will be the start of the ACT in tests
     * Needs to be set before the ACT.
     * Is use for checking performance
     */
    private LocalDateTime _start;

    /**
     * Sets the start time of ACT for measuring of performance time.
     * Must only use before act.
     * Is also used in @BeforeEach, so in case of no ARRANGE, this method is not needed.
     */
    void begin() {
        _start = LocalDateTime.now();
    }

    /**
     * Calculates the performance time from start to this moment and prints it in milliseconds.
     * @return The duration of the performance in milliseconds
     */
    long calculatePerformance() {
        // TODO Needs scenario where _start is null, so act can be performed in arrange.
        long performance = Duration.between(_start, LocalDateTime.now()).toMillis();
        _start = null;
        return performance;
    }

    /**
     * Will add some content to the print.
     * @param content The content tha will be added.
     * @return The current print with the content.
     */
    protected String addToPrint(String content) {
        _print += "\n\n" + content;
        return _print;
    }
}
