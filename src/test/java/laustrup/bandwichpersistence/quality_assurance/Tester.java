package laustrup.quality_assurance;

import laustrup.quality_assurance.inheritances.aaa.Actor;
import laustrup.bandwichpersistence.core.utilities.console.Printer;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Adds the test method, that will log each action of the test into print.
 * Extends ARRANGER, ACTOR and ASSERTER.
 * Includes a random generated password, that will be random generated beforeEach,
 * along with other attributes such as expected, a random and other.
 * @param <T> The return type.
 */
@NoArgsConstructor
public abstract class Tester<T> extends Actor<T> {

    /**
     * An interface to implement in constructor.
     * Can continue the before each method.
     */
    protected TestEditor _editor;

    /** The object that is to be expected to be asserted as the same as the actual. */
    protected Object _expected;

    /** Is used when there should be tested with an exception. */
    protected Exception _exception;

    /**
     * The object that is to be expected to be asserted as the same as the expected
     * and are the result from the act of the method, that will be tested.
     */
    protected Object _actual;

    /** Is used when there should be tested a specific index of a collection from a function. */
    protected int _index;

    /** A default password, with the purpose of creating, logging in and various alike features. */
    protected String _password = null;

    // TODO Use RandomCreatorService when new test dependency are added.
    private String generateString(boolean uniqueCharacter, int length) {
        int min = !uniqueCharacter ? 97 : 123, // letter a
                max = !uniqueCharacter ? 122 : 122 * 2; // letter z
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; i++)
            buffer.append((char) (min + (int) (_random.nextFloat() * (max - min + 1))));

        return buffer.toString();
    }

    private String generatePassword() {
        String password = "";
        password = password + this.generateString(false, 5);
        password = password + this.generateString(true, 2);
        password = password + this.generateString(false, 3);
        password = password + this.generateString(true, 2);
        password = password + "1";
        return password;
    }

    /** This Random is the java Random utility, that can be reused throughout tests. */
    protected Random _random = new Random();

    /** * Will divide Strings in CSVSources. */
    protected final String _divider = "|";

    /** Defines a character that is used to separate CSVSources */
    protected final char _delimiter = '|';

    /**
     * Can be used for testing with multiple adds.
     * Contains three Artists if it is meant for Bandwich project,
     * otherwise it will be three unique numbers.
     */
    protected Object[] _addings;

    /**
     * Can be used for testing with a single add.
     * Contains one Artist.
     */
    protected Object _adding;

    /**
     * Starts the tester with some adding for custom use.
     * They are reinitialized with each before each.
     * @param adding The adding that will be reused for tests.
     */
    protected Tester(Object adding) { _adding = adding; }

    /**
     * Starts the tester with some adding and addings for custom use.
     * They are reinitialized with each before each.
     * @param adding The adding that will be reused for tests.
     * @param addings The addings that will be reused for tests.
     */
    public Tester(Object[] addings, Object adding) {
        _addings = addings;
        _adding = adding;
    }

    /** This is the override of the JUnit @BeforeEach. */
    @BeforeEach
    protected void beforeEach() {
        _password = generatePassword();
        _expected = new String();
        _actual = new String();

        _adding = "Default";
        _addings = new Object[3];

        if (_editor != null)
            _editor.beforeEach();
    }

    /**
     * Must be used before each test method, since it will catch exceptions and print test information.
     * @param supplier The test algorithm that will be supplied,
     *                 if the algorithm return false, something unmeant occurred.
     */
    protected void test(Supplier<String> supplier) {
        try {
            print(supplier.get());
        } catch (Exception e) {
            addToPrint("An exception was caught in the main test method...");
            Printer.get_instance().print(_print, e);
            throw e;
        }
    }

    /**
     * Must be used before each test method, since it will catch exceptions and print test information.
     * @param runnable The test algorithm that will be run,
     *                 if the algorithm return false, something unmeant occurred.
     */
    protected void test(Runnable runnable) {
        try {
            runnable.run();
            print();
        } catch (Exception e) {
            addToPrint("An exception was caught in the main test method...");
            Printer.get_instance().print(_print, e);
            throw e;
        }
    }

    /**
     * Expects that everything went fine and therefore prints the print with the test message to begin with as status.
     */
    private void print() {
        print(TestMessage.SUCCESS.get_content());
    }

    /**
     * Will print the print but also takes handles the response of a supplier by its test message response.
     * @param response Simply the return of the supplier which could be any message, but only the test message success makes it understand that it succeeded.
     */
    private void print(String response) {
        String message = response + "\n\n" + _print;

        if (response.equals(TestMessage.SUCCESS.get_content()))
            Printer.get_instance().print(message);
        else
            Printer.get_instance().print(
                    message,
                    new Exception()
            );
    }
}
