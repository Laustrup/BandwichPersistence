package laustrup.bandwichpersistence.quality_assurance.inheritances.aaa;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Is used for acting of tests. Will also print the performances of arrangement and act after an act,
 * also saves the print of the action.
 */
@Slf4j
public abstract class Actor extends Arranger {

    /** The performance of an act that has been calculated. */
    private long _performance;

    /** The value used for dividing the print of arrange and act. */
    private final String _printDivider = "\n\n-:-\n\n";

    /**
     * Generates the output that will be printed with the Printer to the console of an Act.
     * @return The generated output.
     */
    private String generateActualPrint() {
        return "The acting performance " + measurePerformance(_performance);
    }

    /**
     * Generates the output that will be printed with the Printer to the console of an Act.
     * @param title If a test should be specified with a title,
     *              in case there is multiple acts, this will be the title.
     * @return The generated output.
     */
    private String generateActualPrint(String title) {
        return String.format(
                "The acting performance %s %s %s",
                (!title.isEmpty() ? "of " : ""),
                title,
                measurePerformance(_performance)
        );
    }

    /**
     * Generates the output that will be printed with the Printer to the console of an Arrangement.
     * @return The generated output.
     */
    private String generateArrangementPrint() {
        return "The arrangement performance of current test " + measurePerformance(_arrangement);
    }

    protected <T> T act(T supply) {
        return act(() -> supply);
    }

    protected void act(Void supply) {
        act(supply);
    }

    /**
     * Will call the Supplier and measure the act performance.
     * @param supplier The Supplier that will be acted with a call.
     * @return The return of the Supplier.
     */
    protected <T> T act(Supplier<T> supplier) {
        return act(supplier, "");
    }

    /**
     * Will call the callable and measure the act performance.
     * @param runnable The runnable that will be acted with a run.
     */
    protected void act(Runnable runnable) {
        act(runnable, "");
    }

    /**
     * Will apply the function and measure the act performance.
     * @param input The input for the function.
     * @param function The function that should be acted with an apply.
     * @return The return of the function.
     */
    protected <T> T act(Object input, Function<Object, T> function) {
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
    protected <T> T act(Supplier<T> supplier, String title) {
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
    protected <T> T act(Object input, Function<Object, T> function, String title) {
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

    /**
     * Calculates and measures a performance from the start to now.
     * @param performance The performance measured in milliseconds.
     * @return The calculated and measured performance in writing.
     */
    private static String measurePerformance(long performance) {
        long milliseconds = performance >= 1000 ? performance%1000 : performance,
                seconds = performance >= 1000 ? (performance%60000)/1000 : 0,
                minutes = performance >= 3600000 ? (performance%3600000)/60000 : performance/60000,
                hours = performance/3600000;

        return measurePerformance(milliseconds, seconds, minutes, hours);
    }

    /**
     * Calculates and measures a performance from the start to now.
     * @param milliseconds The performance in milliseconds.
     * @param seconds The performance in seconds.
     * @param minutes The performance in minutes.
     * @param hours The performance in hours.
     * @return A String with the result written as a statement.
     */
    private static String measurePerformance(long milliseconds, long seconds, long minutes, long hours) {
        String hour = hours > 0 ? measurementStatement(hours, "hour", new boolean[] {
                minutes > 0 && (seconds > 0 || milliseconds > 0),
                minutes > 0 || seconds > 0 || milliseconds > 0
        }) : "",
                minute = minutes > 0 ? measurementStatement(minutes, "minute", new boolean[] {
                        seconds > 0 && milliseconds > 0,
                        seconds > 0 || milliseconds > 0
                }) : "",
                second = seconds > 0 ? measurementStatement(seconds, "second", new boolean[] {
                        milliseconds > 0
                }) : "",
                millisecond = milliseconds > 0 ? measurementStatement(milliseconds, "millisecond", new boolean[]{}) : "";

        return milliseconds < 0
                ? "...\nPerformance is negative..."
                : milliseconds == 0 && seconds == 0 && minutes == 0 && hours == 0
                ? " took less than a millisecond!"
                : " took " +
                (hours > 0
                        ? hour + minute + second + millisecond
                        : minutes > 0
                        ? minute + second + millisecond
                        : seconds > 0
                        ? second + millisecond
                        : milliseconds > 0
                        ? millisecond
                        : "...\n Couldn't measure performance..."
                );
    }

    public static String measurePerformance(LocalDateTime start) {
        return measurePerformance(Duration.between(start,LocalDateTime.now()).toMillis());
    }

    /**
     * Will insert values into isPlural and splitter statement, to create a statement for measuring.
     * @param amount The amount of the first mentioning unit.
     * @param word The title of the first mentioning unit.
     * @param statements boolean statements needed to determine amount of measurements.
     * @return The generated measuring statement.
     */
    private static String measurementStatement(long amount, String word, boolean[] statements) {
        return isPlural(amount, word) + splitter(statements);
    }

    /**
     * Will decide whether to put a , or and between statements as a splitter.
     * @param statements boolean statements needed to determine amount of measurements.
     * @return , or and.
     */
    private static String splitter(boolean[] statements) {
        return statements.length == 0 ? "!"
                : statements.length == 1 ? (statements[0] ? " and " : "!")
                : statements.length == 2 ? (statements[0] ? ", " : statements[1] ? " and " : "!")
                : ""
                ;
    }

    /**
     * Will determine if the amount is more than one
     * and in that case will change the word into plural.
     * @param amount The amount of the word.
     * @param word The name of the amount that will occur.
     * @return The amount followed by the word in singular or plural.
     */
    private static String isPlural(long amount, String word) {
        return amount + (amount > 1 ? " " + word + "s" : " " + word);
    }
}
