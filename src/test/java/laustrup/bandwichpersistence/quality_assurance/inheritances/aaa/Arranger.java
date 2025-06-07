package laustrup.bandwichpersistence.quality_assurance.inheritances.aaa;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Will arrange a setup of a test and calculate its performance,
 * also saves a print of the arrangement.
 */
public abstract class Arranger extends TestCalculator {

    /** The time the arrangement has performed */
    protected long _arrangement;

    /** Will add to the print that there isn't any arrangement in the test. */
    protected void arrange() {
        addToPrint("There isn't any arrangement...");
    }

    protected <T> T arrange(T supply) {
        return arrange(() -> supply);
    }

    /**
     * Will perform the Supplier and measure the arrangement performance.
     * @param supplier The Supplier for the arrangement.
     * @return The arrangement.
     */
    protected <T> T arrange(Supplier<T> supplier) {
        begin();
        T arranged;
        arranged = supplier.get();
        _arrangement = calculatePerformance();
        addToPrint("The arrangement is:\n\n" + arranged);
        return arranged;
    }

    /**
     * Will apply the function and measure the arrangement performance and and the arrangement setup to the print.
     * @param input The input for the function.
     * @param function The function for the arrangement.
     * @return The arrangement.
     */
    protected <T> T arrange(Object input, Function<Object,T> function) {
        begin();
        T arranged = function.apply(input);
        _arrangement = calculatePerformance();
        addToPrint("The arrangement is:\n\n" + arranged);
        return arranged;
    }
}
