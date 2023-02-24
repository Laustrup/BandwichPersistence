package laustrup.bandwichpersistence.utilities.printers;


import java.util.Collection;

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
     * Prints an arrays content as well as its curly brackets.
     * Splits with | and the content is printed as its toString().
     * Will be printed in normal text with normal colour.
     * @param array The specific array, that is wished to be printed
     */
    void print(Object[] array);

    /**
     * Compares objects with values.
     * Objects and values must be in same order.
     * The length of both Collections must be the same.
     * @param objects Objects that will be compared. Uses the toString() as identifying the names.
     * @param values Decides which object has the largest value and therefore its place in the hierarchy.
     */
    void compare(Collection<Object> objects, Collection<Double[]> values);

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
