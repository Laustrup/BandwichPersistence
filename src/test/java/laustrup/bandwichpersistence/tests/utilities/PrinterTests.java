package laustrup.bandwichpersistence.tests.utilities;

import laustrup.bandwichpersistence.tests.Tester;
import laustrup.bandwichpersistence.utilities.console.Printer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PrinterTests extends Tester<String, String> {

    /** The Printer that is used for this test */
    private static final Printer _printer = Printer.get_instance();

    /** Defines a content input that will be used in parametrized tests. */
    private final String _null = "null", _empty = "empty", _single = "This is some content",
            _multiple = "multiple", _borderCheck = "border check";

    /** Defines an exception inputs that will be used in parametrized tests. */
    private final String _numberFormatException = "NumberFormatException";

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

    @ParameterizedTest
    @CsvSource(value = {"-5","0","1","5","1000","1001","1005","2001","2000","2005",
        "60000","61000","61001","61005","62000","62005",
        "122000","122001","121005","122005","3600000","7200000",
        "3661000","3662000","3661001","3661005","3662005","3722000","3721001","3721005","3722005",
        "7261000","7262000","7261001","7261005","7262005","7322000","7321001","7321005","7322005"}, delimiter = _delimiter)
    void canMeasure(long performance) {
        test(t -> {
            String content = "performance",
                   expected = arrange(e -> arrange(content, performance));

            String actual = act(e -> {
                Printer.get_instance().print(content,performance);
                return printOfAct();
            });

            asserting(expected, actual);

            return true;
        });
    }

    /**
     * Will set up the inputs for measurement tests.
     * @param content The content of the print.
     * @param performance The performance in milliseconds.
     * @return The arranged expected String.
     */
    private String arrange(String content, long performance) {
        switch (String.valueOf(performance)) {
            case "-5" -> { return content + "...\nPerformance is negative..."; }
            case "0" -> { return content + " took less than a millisecond!"; }

            default -> {
                String arranged = content + " took ";
                switch (String.valueOf(performance)) {
                    case "1" -> { return arranged + "1 millisecond!"; }
                    case "5" -> { return arranged + "5 milliseconds!"; }

                    case "1000" -> { return arranged + "1 second!"; }
                    case "1001" -> { return arranged + "1 second and 1 millisecond!"; }
                    case "1005" -> { return arranged + "1 second and 5 milliseconds!"; }
                    case "2000" -> { return arranged + "2 seconds!"; }
                    case "2001" -> { return arranged + "2 seconds and 1 millisecond!"; }
                    case "2005" -> { return arranged + "2 seconds and 5 milliseconds!"; }

                    case "60000" -> { return arranged + "1 minute!";}
                    case "120000" -> { return arranged + "2 minutes!"; }
                    case "61000" -> { return arranged + "1 minute and 1 second!"; }
                    case "62000" -> { return arranged + "1 minute and 2 seconds!"; }
                    case "61001" -> { return arranged + "1 minute, 1 second and 1 millisecond!"; }
                    case "61005" -> { return arranged + "1 minute, 1 second and 5 milliseconds!"; }
                    case "62005" -> { return arranged + "1 minute, 2 seconds and 5 milliseconds!"; }
                    case "122000" -> { return arranged + "2 minutes and 2 seconds!"; }
                    case "122001" -> { return arranged + "2 minutes, 2 seconds and 1 millisecond!"; }
                    case "121001" -> { return arranged + "2 minutes, 1 second and 1 millisecond!"; }
                    case "121005" -> { return arranged + "2 minutes, 1 second and 5 milliseconds!"; }
                    case "122005" -> { return arranged + "2 minutes, 2 seconds and 5 milliseconds!"; }
                    case "3600000" -> { return arranged + "1 hour!"; }
                    case "7200000" -> { return arranged + "2 hours!"; }

                    case "3661000" -> { return arranged + "1 hour, 1 minute and 1 second!"; }
                    case "3662000" -> { return arranged + "1 hour, 1 minute and 2 seconds!"; }
                    case "3661001" -> { return arranged + "1 hour, 1 minute, 1 second and 1 millisecond!"; }
                    case "3661005" -> { return arranged + "1 hour, 1 minute, 1 second and 5 milliseconds!"; }
                    case "3662005" -> { return arranged + "1 hour, 1 minute, 2 seconds and 5 milliseconds!"; }
                    case "3722000" -> { return arranged + "1 hour, 2 minutes and 2 seconds!"; }
                    case "3721001" -> { return arranged + "1 hour, 2 minutes, 1 second and 1 millisecond!"; }
                    case "3721005" -> { return arranged + "1 hour, 2 minutes, 1 second and 5 milliseconds!"; }
                    case "3722005" -> { return arranged + "1 hour, 2 minutes, 2 seconds and 5 milliseconds!"; }

                    case "7261000" -> { return arranged + "2 hours, 1 minute and 1 second!"; }
                    case "7262000" -> { return arranged + "2 hours, 1 minute and 2 seconds!"; }
                    case "7261001" -> { return arranged + "2 hours, 1 minute, 1 second and 1 millisecond!"; }
                    case "7261005" -> { return arranged + "2 hours, 1 minute, 1 second and 5 milliseconds!"; }
                    case "7262005" -> { return arranged + "2 hours, 1 minute, 2 seconds and 5 milliseconds!"; }
                    case "7322000" -> { return arranged + "2 hours, 2 minutes and 2 seconds!"; }
                    case "7321001" -> { return arranged + "2 hours, 2 minutes, 1 second and 1 millisecond!"; }
                    case "7321005" -> { return arranged + "2 hours, 2 minutes, 1 second and 5 milliseconds!"; }
                    case "7322005" -> { return arranged + "2 hours, 2 minutes, 2 seconds and 5 milliseconds!"; }

                    default -> { return "Couldn't recognize expected performance..."; }
                }
            }
        }
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
}
