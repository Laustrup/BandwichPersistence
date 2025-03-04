package laustrup.bandwichpersistence.quality_assurance.inheritances.aaa;

import laustrup.bandwichpersistence.core.utilities.console.Printer;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Is used for acting of tests. Will also print the performances of arrangement and act after an act,
 * also saves the print of the action.
 * @param <T> The return type.
 */
public abstract class Actor<T> extends Arranger<T> {

    /** The performance of an act that has been calculated. */
    private long _performance;

    /** The value used for dividing the print of arrange and act. */
    private final String _printDivider = "\n\n-:-\n\n";

    /**
     * Generates the output that will be printed with the Printer to the console of an Act.
     * @return The generated output.
     */
    private String generateActualPrint() {
        new Printer();
        return "The acting performance " + Printer.get_instance().measurePerformance(_performance);
    }

    /**
     * Generates the output that will be printed with the Printer to the console of an Act.
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     * @return The generated output.
     */
    private String generateActualPrint(String title) {
        return "The acting performance" + (!title.isEmpty() ? " of " : "") + title + Printer.get_instance().measurePerformance(_performance);
    }

    /**
     * Generates the output that will be printed with the Printer to the console of an Arrangement.
     * @return The generated output.
     */
    private String generateArrangementPrint() {
        return "The arrangement performance of current test" + Printer.get_instance().measurePerformance(_arrangement);
    }

    /**
     * Will call the Supplier and measure the act performance.
     * @param supplier The Supplier that will be acted with a call.
     * @return The return of the Supplier.
     */
    protected T act(Supplier<T> supplier) {
        return act(supplier,new String());
    }

    /**
     * Will call the callable and measure the act performance.
     * @param runnable The runnable that will be acted with a run.
     */
    protected void act(Runnable runnable) {
        act(runnable,new String());
    }

    /**
     * Will apply the function and measure the act performance.
     * @param input The input for the function.
     * @param function The function that should be acted with an apply.
     * @return The return of the function.
     */
    protected T act(Object input, Function<Object,T> function) {
        begin();
        T actual = function.apply(input);
        _performance = calculatePerformance();
        addToPrint(generateArrangementPrint() +
                _printDivider +
                generateActualPrint());
        return actual;
    }

    /**
     * Will apply the Supplier and measure the act performance.
     * @param supplier The Supplier that will be acted with a get().
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     * @return The return of the Supplier.
     */
    protected T act(Supplier<T> supplier, String title) {
        begin();
        T actual = supplier.get();
        _performance = calculatePerformance();
        addToPrint(
            generateArrangementPrint() +
            _printDivider +
            generateActualPrint(title)
        );
        return actual;
    }

    /**
     * Will run the Runnable and measure the act performance.
     * @param runnable The runnable that should be acted with a run().
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     */
    protected void act(Runnable runnable, String title) {
        begin();
        runnable.run();
        _performance = calculatePerformance();
        addToPrint(
            generateArrangementPrint() +
            _printDivider +
            generateActualPrint(title)
        );
    }

    /**
     * Will apply the function and measure the act performance.
     * @param input The input for the function.
     * @param function The function that should be acted with an apply.
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     * @return The return of the function.
     */
    protected T act(Object input, Function<Object,T> function, String title) {
        begin();
        T actual = function.apply(input);
        _performance = calculatePerformance();
        addToPrint(
            generateArrangementPrint() +
            _printDivider +
            generateActualPrint(title)
        );
        return actual;
    }
}
