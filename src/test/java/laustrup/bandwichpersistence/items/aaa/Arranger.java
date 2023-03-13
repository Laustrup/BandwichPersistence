package laustrup.bandwichpersistence.items.aaa;

import java.util.function.Function;

/** Will arrange a setup of a test and calculate its performance */
public abstract class Arranger<T,R> extends TestCalculator {

    /** The time the arrangement has performed */
    protected long _arrangement;

    /** Will add to the print that there isn't any arrangement in the test. */
    protected void arrange() {
        addToPrint("There isn't any arrangement...");
    }

    /**
     * Will apply the function and measure the arrangement performance.
     * @param function The function for the arrangement.
     * @return The performance of the arrangement.
     */
    protected R arrange(Function<T,R> function) {
        return arrange(null, function);
    }

    /**
     * Will apply the function and measure the arrangement performance.
     * @param input The input for the function.
     * @param function The function for the arrangement.
     * @return The performance of the arrangement.
     */
    protected R arrange(T input, Function<T,R> function) {
        begin();
        R arranged = function.apply(input);
        _arrangement = calculatePerformance();
        addToPrint("The arrangement is: " + arranged);
        return arranged;
    }
}
