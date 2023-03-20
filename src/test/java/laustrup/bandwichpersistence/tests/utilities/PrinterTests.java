package laustrup.bandwichpersistence.tests.utilities;

import laustrup.bandwichpersistence.tests.Tester;
import laustrup.bandwichpersistence.utilities.console.Printer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PrinterTests extends Tester<String, String> {

    /** The Printer that is used for this test */
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

    /**
     * Will find the print of the act.
     * @return The found print.
     */
    private String printOfAct() {
        return _printer.get_db().get_data()[
            _printer.get_db().get_index() == 0
                ? _printer.get_db().get_data().length-1
                : _printer.get_db().get_index() - 1
        ];
    }

    @ParameterizedTest
    @CsvSource(value = {_null, _empty, _single, _multiple, _borderCheck})
    void canPrint(String content) {
        test(t -> {
            String expected = arrange(content, this::arrange);

            String actual = act(expected, e -> {
                _printer.print(e);
                return printOfAct();
            });

            asserting(expected == null || expected.isEmpty() ? "Nothing to print..." : expected, actual);

            return true;
        });
    }

    @ParameterizedTest
    @CsvSource(value = {
            _null+_divider+_numberFormatException,
            _empty+_divider+_numberFormatException,
            _single+_divider+_numberFormatException,
            _multiple+_divider+_numberFormatException,
            _borderCheck+_divider+_numberFormatException}, delimiter = _delimiter)
    void canPrint(String content, String exception) {
        test(t -> {
            String expected = arrange(e -> {
                String arranged = arrange(content);
                _exception = determine(exception);
                if (_exception != null)
                    return ((arranged != null && !arranged.isEmpty())
                        ? arranged + "\n\n-- EXCEPTION\n\n"
                        : "-- EXCEPTION\n\n") + _exception;
                return arranged;
            });

            String actual = act(e -> {
                _printer.print(arrange(content),_exception);
                return printOfAct();
            });

            asserting(expected, actual);

            return true;
        });

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
                    Integer.parseInt("a");
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
