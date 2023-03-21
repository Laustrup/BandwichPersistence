package laustrup.bandwichpersistence.items.aaa;

import laustrup.bandwichpersistence.utilities.console.Printer;

import java.util.function.Function;

/** Is used for acting of tests. Will also print the performances of arrangement and act after an act. */
public abstract class Actor<T,R> extends Arranger<T,R> {

    /** The performance of an act that has been calculated. */
    private long _performance;

    /** The value used for dividing the print of arrange and act. */
    private final String _printDivider = "\n\n-:-\n\n";

    /**
     * Generates the output that will be printed with the Printer to the console of an Act.
     * @return The generated output.
     */
    private String generateActualPrint() {
        return "The acting performance" + Printer.get_instance().measurePerformance(_performance);
    }

    /**
     * Generates the output that will be printed with the Printer to the console of an Act.
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     * @return The generated output.
     */
    private String generateActualPrint(String title) {
        return "The acting performance of " + title + Printer.get_instance().measurePerformance(_performance);
    }

    /**
     * Generates the output that will be printed with the Printer to the console of an Arrangement.
     * @return The generated output.
     */
    private String generateArrangementPrint() {
        return "The arrangement performance of current test" + Printer.get_instance().measurePerformance(_arrangement);
    }

    /**
     * Will apply the function and measure the act performance.
     * @param function The function that should be acted with an apply.
     * @return The return of the function.
     */
    protected R act(Function<T,R> function) {
        return act(null, function);
    }

    /**
     * Will apply the function and measure the act performance.
     * @param input The input for the function.
     * @param function The function that should be acted with an apply.
     * @return The return of the function.
     */
    protected R act(T input, Function<T,R> function) {
        begin();
        R actual = function.apply(input);
        _performance = calculatePerformance();
        addToPrint(generateArrangementPrint() +
                _printDivider +
                generateActualPrint());
        return actual;
    }

    /**
     * Will apply the function and measure the act performance.
     * @param function The function that should be acted with an apply.
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     * @return The return of the function.
     */
    protected R act(Function<T,R> function, String title) {
        return act(null, function, title);
    }

    /**
     * Will apply the function and measure the act performance.
     * @param input The input for the function.
     * @param function The function that should be acted with an apply.
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     * @return The return of the function.
     */
    protected R act(T input, Function<T,R> function, String title) {
        begin();
        R actual = function.apply(input);
        _performance = calculatePerformance();
        addToPrint(generateArrangementPrint() +
                _printDivider +
                generateActualPrint(title));
        return actual;
    }
}
