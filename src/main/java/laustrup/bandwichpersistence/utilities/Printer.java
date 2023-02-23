package laustrup.bandwichpersistence.utilities;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.io.*;

/**
 * This class will handle printing of statements.
 */
public class Printer implements IPrinter {

    /**
     * Contains the last content that was printed.
     */
    @Getter
    private String _latest;

    /**
     * The content that is meant to be printed.
     */
    private StringBuilder _content;

    /**
     * This will be printed, if the content is unrecognisable,
     * such as empty or null.
     */
    private final String _emptyIndicator = "Nothing to print...";

    /**
     * Will determined the allowed length of a print.
     */
    private final int _length = 145;

    /**
     * Will indicate how a row of the content will start.
     */
    @SuppressWarnings("all")
    private final String _startRow = cyan("\n | ");

    /**
     * The border used for separating the print in the console.
     */
    private final String _border = generateBorder();
    /**
     * Will describe the border from the given length.
     * @return The described border.
     */
    private String generateBorder() { return "-".repeat(_length); }

    /**
     * Is used to be reused as a border for beginning and ending of a print.
     */
    private final String _startBorder = "\n++ " + _border + "\n +",
        _endBorder = "\n +\n++ " + _border + "\n";

    /**
     * Singleton instance of the Printer.
     */
    public static Printer _instance = null;

    /**
     * Will render the static singleton Printer instance.
     * @return The static singleton Printer.
     */
    public static Printer get_instance() {
        if (_instance == null) {
            _instance = new Printer();
        }
        return _instance;
    }

    @Override
    public void print(String content) { handlePrint(content); }
    @Override
    public void print(String content, Exception ex) {
        handlePrint((content != null && !content.isEmpty() ? content + "\n\n-- EXCEPTION\n\n" : "-- EXCEPTION\n\n") + manage(ex));
    }

    /**
     * Will manage how an exception will be displayed in the console.
     * @param exception The exception that will be managed before displayed.
     * @return The managed exception, as a String representation.
     */
    private String manage(Exception exception) {
        return exception.toString();
    }

    @Override
    public void print(Object[] array) { handlePrint(toString(array)); }

    /**
     * Will save the content as current, use the content to be printed to console and then save it the latest.
     * @param content The content that is wished to be handled.
     */
    private void handlePrint(String content) {
        systemOut(generateContent(content));
        _latest = _content == null || _content.toString().isEmpty() ? _emptyIndicator : _content.toString();
    }

    /**
     * Will use the System.out.println to print the param with a border surrounding it.
     * Also saves the print as latest.
     * @param print The content that will be printed without borders.
     * @return The print param for saving to be latest.
     */
    private String systemOut(String print) {
        System.out.println(cyan(_startBorder) + print + cyan(_endBorder));
        return print;
    }

    /**
     * Will make some specific text the colour cyan for console.
     * @param text The text that should be in cyan.
     * @return The text with some escape sequences for the console.
     */
    private String cyan(String text) {
        return "\033[0;96m" + text + "\033[0m";
    }

    /**
     * Will make some specific text the colour yellow for console.
     * @param text The text that should be in yellow.
     * @return The text with some escape sequences for the console.
     */
    private String yellow(String text) {
        return "\033[0;93m" + text + "\033[0m";
    }

    /**
     * Will make some specific text the colour red for console.
     * @param text The text that should be in red.
     * @return The text with some escape sequences for the console.
     */
    private String red(String text) {
        return "\033[0;91m" + text + "\033[0m";
    }

    @Override
    public String toString(Object[] objects) {
        return "{ " + arrayContent(objects) + " }";
    }

    @Override
    public String arrayContent(Object[] objects) {

        StringBuilder content = new StringBuilder("");

        for (int i = 0; i < objects.length; i++) {
            content.append(objects[i] == null ? "null" : objects[i].toString());

            if (i > objects.length - 1)
                content.append(" | ");
        }

        return content.toString();
    }

    /**
     * Will generate the content with rules of the permitted length and start of lines.
     * @param element The element the content should be generated from.
     * @return The generated content.
     */
    private String generateContent(String element) {
        if (element == null || element.isEmpty()) {
            String generated = _startRow + yellow(_emptyIndicator);
            _content = new StringBuilder();
            return generated;
        }

        boolean isException = false;
        StringBuilder generated = new StringBuilder();
        _content = new StringBuilder();

        for (int i = 0; i < element.length(); i++) {
            if (generated.toString().contains("-- EXCEPTION"))
                isException = true;
            if (i % _length == 0 || element.charAt(i) == '\n')
                generated.append(_startRow);
            if (element.charAt(i) != '\n')
                generated.append(isException ? red(String.valueOf(element.charAt(i)))
                        : yellow(String.valueOf(element.charAt(i))));
            _content.append(element.charAt(i));
        }

        return generated.toString();
    }

    @Override
    public void compare(Collection<Object> objects, Collection<Double[]> values) {
        if (objects.size()==values.size()) {
            Object[] convertedObjects = objects.toArray();
            Object[] convertedValues = values.toArray();

            Liszt<ComparingObject> comparingObjects = new Liszt<>();
            for (int i = 0; i < objects.size(); i++) { comparingObjects.add(new ComparingObject(convertedObjects[i], (Double) convertedValues[i])); }

            String content = "Comparing of following objects are: \n\n";
            int place = 1;
            for (ComparingObject object : comparingObjects) {
                content += "\tPlace " + place + " is " + object.get_data().toString() +
                        " with the value of " + convertedValues[place-1].toString() + ".\n";
                place++;
            }

            print(content);
        }
        print("The two array inputs are not the same in Printer...", new ArrayIndexOutOfBoundsException());
    }
    @Data
    private static class ComparingObject {
        private Object _data;

        private double _value;
        public ComparingObject(Object data, double value) {
            _data = data;
            _value = value;
        }

    }
}
