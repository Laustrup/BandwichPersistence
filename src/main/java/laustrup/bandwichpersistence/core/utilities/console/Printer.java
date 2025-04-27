package laustrup.bandwichpersistence.core.utilities.console;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

// TODO first \t of a row only appears as a space.
// TODO Double newline is error
/**
 * Will handle printing of statements to the console.
 * Contains three different options for modes, default will use a normal println,
 * the others have a border around each print, noir mode is without colour
 * and high contrast mode is with colour of high contrast.
 * Is intended to log each print, but isn't implemented yet.
 */
public class Printer extends Painter {

    /** Containing previous printed contents */
    @Getter
    public static PrintDatabase _db = new PrintDatabase();

    /** Determines if the message that is to be printed is an error message or not */
    private static boolean _isAnErrorMessage = false;

    /** The specified PrinterMode for this Printer, that will behave depending on the choosen enum. */
    @Getter
    private static PrinterMode _mode = /*Program.get_instance().get_state().equals(Program.State.TESTING)
            ?*/ PrinterMode.HIGH_CONTRAST /*: PrinterMode.NOIRE*/;

    /**
     * Configures the mode for the Printer.
     * @param mode The mode that should be configured.
     * @return The configured mode.
     */
    public PrinterMode set_mode(PrinterMode mode) {
        _mode = /*Program.get_instance().get_state().equals(Program.State.TESTING) ?*/ PrinterMode.HIGH_CONTRAST /*: mode*/;
        set_startRow();
        return _mode;
    }

    /** The content that is meant to be printed. */
    private static String _content = new String();

    /**
     * This will be printed, if the content is unrecognisable,
     * such as empty or null.
     */
    private static final String _emptyIndicator = "Nothing to print...";

    /**
     * Will determined the allowed length of a print.
     */
    @Getter
    private static final int _length = 143;

    /** Determines how many rows are in the print, including the current. */
    private static int _rows = 1;

    /** The index of the content. */
    private static int _index;

    /**
     * Will indicate how a row of the content will start.
     */
    @SuppressWarnings("all")
    private static String _startRow = set_startRow();

    /**
     * Sets the start of each row depending on the mode.
     * @return The start of each row.
     */
    private static String set_startRow() {
        _startRow = _mode.equals(PrinterMode.HIGH_CONTRAST) ? cyan("\n | ") : "\n | ";
        return _startRow;
    }

    /**
     * The border used for separating the print in the console.
     */
    private static final String _border = generateBorder();

    /**
     * Will describe the border from the given length.
     * @return The described border.
     */
    private static String generateBorder() { return "-".repeat(2); }

    /** Is used to be reused as a border for beginning and ending of a print. */
    private static final String _startBorder = "\n-+ " + _border + " +\n $";
    private static final String _endBorder = "\n $\n-+ " + _border + " +\n";

    /** Singleton instance of the Printer. */
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

    public static void print(String content) { handlePrint(content); }
    public static void print(String content, Exception ex) {
        _isAnErrorMessage = true;
        handlePrint((content != null && !content.isEmpty() ? content + "\n\n-- EXCEPTION\n\n" : "-- EXCEPTION\n\n") + manage(ex));
    }

    public static void print(String content, long performance) {
        handlePrint(content + measurePerformance(performance));
    }

    public static void print(String content, LocalDateTime start) {
        handlePrint(content + measurePerformance(start));
    }

    public static String measurePerformance(LocalDateTime start) {
        return measurePerformance(Duration.between(start,LocalDateTime.now()).toMillis());
    }

    /**
     * Calculates and measures a performance from the start to now.
     * @param performance The performance measured in milliseconds.
     * @return The calculated and measured performance in writing.
     */
    public static String measurePerformance(long performance) {
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
        String hour = hours > 0 ? measurementStatement(hours,"hour",new boolean[] {
            minutes > 0 && (seconds > 0 || milliseconds > 0),
            minutes > 0 || seconds > 0 || milliseconds > 0
        }) : "",
        minute = minutes > 0 ? measurementStatement(minutes,"minute",new boolean[] {
            seconds > 0 && milliseconds > 0,
            seconds > 0 || milliseconds > 0
        }) : "",
        second = seconds > 0 ? measurementStatement(seconds,"second",new boolean[] {
            milliseconds > 0
        }) : "",
        millisecond = milliseconds > 0 ? measurementStatement(milliseconds,"millisecond",new boolean[]{}) : "";

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
     * Will determine if the amount is more than one
     * and in that case will change the word into plural.
     * @param amount The amount of the word.
     * @param word The name of the amount that will occur.
     * @return The amount followed by the word in singular or plural.
     */
    private static String isPlural(long amount, String word) {
        return amount + (amount > 1 ? " " + word + "s" : " " + word);
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
     * Will manage how an exception will be displayed in the console.
     * @param exception The exception that will be managed before displayed.
     * @return The managed exception, as a String representation.
     */
    private static String manage(Exception exception) {
        return exception.toString();
    }

    public void print(Object[] array) { handlePrint(toString(array)); }

    /**
     * Will save the content as current, use the content to be printed to console and then save it the latest.
     * @param content The content that is wished to be handled.
     */
    private static void handlePrint(String content) {
        _content = "";

        switch (_mode) {
            case DEFAULT -> {
                System.out.println(content);
                savePrint(content);
            }
            default -> {
                systemOut(generate(content));
                savePrint();
            }
        }

        _isAnErrorMessage = false;
    }

    /**
     * Will use the System.out.println to print the param with a border surrounding it.
     * Also saves the print as latest.
     * @param print The content that will be printed without borders.
     * @return The print param for saving to be latest.
     */
    private static String systemOut(String print) {
        System.out.println(
            _mode.equals(PrinterMode.HIGH_CONTRAST)
                ? cyan(_startBorder) + print + cyan(_endBorder)
                : _startBorder + print + _endBorder
        );
        _rows = 1;
        return print;
    }

    public static String toString(Object[] objects) {
        return "{ " + arrayContent(objects) + " }";
    }

    public static String arrayContent(Object[] objects) {

        StringBuilder content = new StringBuilder();

        for (int i = 0; i < objects.length; i++) {
            content.append(objects[i] == null ? "null" : objects[i].toString());

            if (i > objects.length - 1)
                content.append(" | ");
        }

        return content.toString();
    }

    /**
     * Will generate the content with rules of the permitted length and start of lines.
     * @param content The element the content should be generated from.
     * @return The generated content.
     */
    private static String generate(String content) {
        if (content == null || content.isEmpty()) {
            String generated = _startRow + (_mode.equals(PrinterMode.HIGH_CONTRAST)
                    ? green(_emptyIndicator)
                    : _emptyIndicator);
            _content = "";
            return generated;
        }

        //TODO fix size issue
        if (true)
            return content;

        StringBuilder print = new StringBuilder(_startRow);

        for (_index = 0; _index < content.length(); _index++) {
            int index = _index;
            print.append(generate(print,content));
            if (index == _index)
                _content += content.charAt(index);
        }

        return print.toString();
    }

    /**
     * Will generate the text that will be printed on the console from the original text.
     * @param print The generated text, that will be updated.
     * @param content The text, that the generated text will be generated from.
     * @return The updated generated text.
     */
    private static StringBuilder generate(StringBuilder print, String content) {
        Colour colour = _mode.equals(PrinterMode.HIGH_CONTRAST) ? setColour(content, _index) : null;

        if (wordIsTooLong(content)) {
            while (content.charAt(_index) != ' ') {
                print.append(colorize(String.valueOf(content.charAt(_index)),colour));
                _content += content.charAt(_index);
                _index++;
            }
            print.append(_startRow);
            _content += content.charAt(_index);
            _rows++;
        }
        else if (_index/_rows > _length || content.charAt(_index) == '\n') {
            print.append(_startRow);
            _rows++;
        }
        else
            print.append(colorize(String.valueOf(content.charAt(_index)),colour));

        return print;
    }

    /**
     * Will determine if a new row should be added.
     * @param content The text, that the generated text will be generated from.
     * @return True if a whitespace after the current word is on a higher index than the permitted length.
     */
    private static boolean wordIsTooLong(String content) {
        boolean whitespaceIsFound = false;
        int index = _index;

        while (!whitespaceIsFound) {
            if (index >= content.length()-1)
                break;
            if (content.charAt(index) == ' ')
                whitespaceIsFound = true;
            index++;
        }
        return whitespaceIsFound && index/_rows > _length;
    }

    /**
     * Will set the colour depending on the current characters of the text.
     * @param text The original text that is being processed.
     * @param index The current index of the text.
     * @return The colour that has been set.
     */
    private static Colour setColour(String text, int index) {
        Colour colour;
        text = _startRow + text;
        index += _startRow.length();
        boolean yellowIndexLengthIsAllowed = index > 4 && index <= text.length()-2,
            beforeIsAStartBorder = index == _startRow.length() || (text.charAt(index-1) == '\n'),
            headlineIdentifier = String.valueOf(text.charAt(index)).equals("-") && String.valueOf(text.charAt(index+1)).equals("-"),
            isStillHeadline = _previousColour == Colour.YELLOW && !beforeIsAStartBorder;

        if (yellowIndexLengthIsAllowed && ((beforeIsAStartBorder && headlineIdentifier) || isStillHeadline))
            colour = Colour.YELLOW;
        else if (_content.contains("-- EXCEPTION"))
            colour = Colour.RED;
        else
            colour = _isAnErrorMessage ? Colour.WHITE : Colour.GREEN;

        return colour;
    }

    /**
     * Adds the print to be stored.
     * @return The stored prints.
     */
    private static String[] savePrint() {
        return _db.add(_content == null || _content.isEmpty() ? _emptyIndicator : _content);
    }

    /**
     * Adds the print to be stored.
     * @return The stored prints.
     */
    private static String[] savePrint(String content) {
        _content = content;
        return _db.add(_content == null || _content.isEmpty() ? _emptyIndicator : _content);
    }

    /** A class created to save prints of the Printer */
    public static class PrintDatabase {

        /** The permitted amount of prints to be stored */
        private final int _storageSize = 5;

        /** The stored prints */
        @Getter
        private String[] _data = new String[_storageSize];

        /** The last print that was stored */
        @Getter
        private String _last;

        /** The index of the print to be overwritten */
        @Getter
        private int _index;

        /**
         * Will add some print to the data. Also increments the index.
         * @param print The print that will be saved.
         * @return The data of the prints.
         */
        public String[] add(String print) {
            _data[_index] = print;
            _last = _data[_index];
            increment();
            return _data;
        }

        /** Will increment the index, if the index is the same as the storage size, it will be reset */
        private int increment() {
            if (_index == _storageSize-1)
                _index = 0;
            else
                _index++;
            return _index;
        }
    }
}
