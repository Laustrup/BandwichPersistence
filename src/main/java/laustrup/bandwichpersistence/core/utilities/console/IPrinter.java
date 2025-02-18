package laustrup.bandwichpersistence.core.utilities.console;

import java.time.LocalDateTime;

/** Contains the unique features of a Printer. */ //TODO Consider if methods are to be static.
public interface IPrinter {

    /**
     * Prints the input as in the traditional System.out.println way,
     * formatted in a different intended way.
     * @param content The content of what is wished to be printed.
     */
    void print(String content);

    /**
     * Prints the input as in the traditional System.out.println way,
     * formatted in a different intended way.
     * Can also printed an exception.
     * The output will be displayed in red colour.
     * @param content The content of what is wished to be printed.
     */
    void print(String content,Exception ex);

    /**
     * Prints the input as in the traditional System.out.println way,
     * formatted in a different intended way.
     * Will write the content, " took " with the performance measured into correct unit.
     * @param content The content of what is wished to be printed.
     * @param performance The performance measured in milliseconds.
     */
    void print(String content, long performance);

    /**
     * Prints the input as in the traditional System.out.println way,
     * formatted in a different intended way.
     * Will write the content, " took " with the performance measured into correct unit.
     * @param content The content of what is wished to be printed.
     * @param start The start of the performance act.
     */
    void print(String content, LocalDateTime start);

    /**
     * Calculates and measures a performance from the start to now.
     * @param start The start of the performance act.
     * @return The calculated and measured performance in writing.
     */
    String measurePerformance(LocalDateTime start);

    /**
     * Calculates and measures a performance from the start to now.
     * @param performance The performance duration in milliseconds.
     * @return The calculated and measured performance in writing.
     */
    String measurePerformance(long performance);

    /**
     * Prints an arrays content as well as its curly brackets.
     * Splits with | and the content is printed as its toString().
     * Will be printed in normal text with normal colour.
     * @param array The specific array, that is wished to be printed
     */
    void print(Object[] array);


    /**
     * Will turn an array into a String with | as a delimiter.
     * @param objects The array with the objects to be converted.
     * @return The converted objects.
     */
    String toString(Object[] objects);

    /**
     * Generates the contents of the object array with | as a delimiter.
     * @param objects The array with the objects to be converted.
     * @return The converted objects' content.
     */
    String arrayContent(Object[] objects);
}
