package laustrup.bandwichpersistence.utilities;

import laustrup.bandwichpersistence.Tester;

import laustrup.bandwichpersistence.utilities.printers.Printer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PrinterTests extends Tester {

    private static final Printer _printer = Printer.get_instance();

    /**
     * Defines a content input that will be used in parametrized tests.
     */
    private final String _null = "null", _empty = "empty", _single = "This is some content",
            _multiple = "multiple", _borderCheck = "border check";

    /**
     * Defines an exception inputs that will be used in parametrized tests.
     */
    private final String _numberFormatException = "NumberFormatException";

    @ParameterizedTest
    @CsvSource(value = {_null, _empty, _single, _multiple, _borderCheck})
    void canPrint(String content) {
        //ARRANGE
        content = arrange(content);

        //ACT
        begin();
        _printer.print(content);
        String actual = _printer.get_latest();
        calculatePerformance();

        //ASSERT
        asserting(content == null || content.isEmpty() ? "Nothing to print..." : content, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            _null+_divider+ _numberFormatException,
            _empty+_divider+ _numberFormatException,
            _single+_divider+ _numberFormatException,
            _multiple+_divider+ _numberFormatException,
            _borderCheck+_divider+ _numberFormatException}, delimiter = _delimiter)
    void canPrint(String content, String exception) {
        //ARRANGE
        content = arrange(content);
        Exception determined = determine(exception);
        String expected = (content != null && !content.isEmpty() ? content + "\n\n-- EXCEPTION\n\n" : "-- EXCEPTION\n\n")
                + determined.toString();

        //ACT
        begin();
        _printer.print(content,determined);
        String actual = _printer.get_latest();
        calculatePerformance();

        //ASSERT
        asserting(expected, actual);
    }

    /**
     * Will arrange the content of Printer tests, so the String attributes
     * is rightfully represented.
     * @param content The content that will be arranged.
     * @return The arranged content. If default case, it returns the content value.
     */
    private String arrange(String content) {
        switch (content) {
            case _null -> { return null; }
            case _empty -> { return ""; }
            case _multiple -> { return _multiple + "\n" + _multiple + "\n" + _multiple; }
            case _borderCheck -> { return "bordercheck ".repeat(100); }
            default -> { return content; }
        }
    }

    /**
     * Determines the exception that is described and makes it into an Exception object.
     * @param exception A String representation of the Exception.
     * @return The Exception object of the representation. If default case, its IllegalStateException.
     */
    private Exception determine(String exception) {
        switch (exception) {
            case _numberFormatException -> {
                try {
                    int integer = Integer.parseInt("a");
                    return null;
                }
                catch (Exception e) {
                    return e;
                }
            }
            default -> { return new IllegalStateException("Unexpected value: " + exception); }
        }
    }
}
