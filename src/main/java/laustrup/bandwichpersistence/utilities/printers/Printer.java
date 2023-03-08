package laustrup.bandwichpersistence.utilities.printers;

import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * This class will handle printing of statements.
 */
public class Printer extends Painter implements IPrinter {

    /**
     * The specified PrinterMode for this Printer, that will behave depending on the choosen enum.
     */
    @Getter @Setter
    private PrinterMode _mode;

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
    @Getter @Setter
    private final int _length = 143;

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
    private String generateBorder() { return "-".repeat(2); }

    /**
     * Is used to be reused as a border for beginning and ending of a print.
     */
    private final String _startBorder = "\n-+ " + _border + " +\n $",
        _endBorder = "\n $\n-+ " + _border + " +\n";

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

    private Printer() {}

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
            String generated = _startRow + green(_emptyIndicator);
            _content = new StringBuilder();
            return generated;
        }

        StringBuilder generated = new StringBuilder();
        _content = new StringBuilder();

        for (int i = 0; i < element.length(); i++) {
            generated = generate(generated,element,i);
            _content.append(element.charAt(i));
        }

        return generated.toString();
    }

    /**
     * Will generate the text that will be printed on the console from the original text.
     * @param generated The generated text, that will be updated.
     * @param original The text, that the generated text will be generated from.
     * @param index The current index.
     * @return The updated generated text.
     */
    private StringBuilder generate(StringBuilder generated, String original, int index) {
        Colour colour = setColour(original, index);

        if (index % _length == 0 || original.charAt(index) == '\n')
            generated.append(_startRow);
        if (original.charAt(index) != '\n')
            generated.append(colorize(String.valueOf(original.charAt(index)),colour));

        return generated;
    }

    /**
     * Will set the colour depending on the current characters of the text.
     * @param text The original text that is being processed.
     * @param index The current index of the text.
     * @return The colour that has been set.
     */
    private Colour setColour(String text, int index) {
        Colour colour;
        text = _startRow + text;
        index += _startRow.length();
        boolean yellowIndexLengthIsAllowed = index > 4 && index <= text.length()-2,
            beforeIsAStartBorder = index == _startRow.length() || (text.charAt(index-1) == '\n'),
            headlineIdentifier = String.valueOf(text.charAt(index)).equals("-") && String.valueOf(text.charAt(index+1)).equals("-"),
            isStillHeadline = _previousColour == Colour.YELLOW && !beforeIsAStartBorder;

        if (yellowIndexLengthIsAllowed && ((beforeIsAStartBorder && headlineIdentifier) || isStillHeadline))
            colour = Colour.YELLOW;
        else if (_content.toString().contains("-- EXCEPTION"))
            colour = Colour.RED;
        else
            colour = Colour.GREEN;

        return colour;
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
