package laustrup.bandwichpersistence.tests;

import laustrup.bandwichpersistence.Program;
import laustrup.bandwichpersistence.items.aaa.Asserter;
import laustrup.bandwichpersistence.items.TestItems;
import laustrup.bandwichpersistence.repositories.DbLibrary;
import laustrup.bandwichpersistence.services.RandomCreatorService;
import laustrup.bandwichpersistence.utilities.console.Printer;
import laustrup.bandwichpersistence.utilities.console.PrinterMode;

import org.junit.jupiter.api.BeforeEach;

import java.util.Random;
import java.util.function.Function;

/**
 * Adds a few functions to test methods to reuse.
 */
public abstract class Tester<T,R> extends Asserter<T,R> {


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

    /**
     * Contains different generated items to use for testing.
     * Are being reset for each method.
     */
    protected TestItems _items;

    /** A default password, with the purpose of creating, logging in and various alike features. */
    protected String _password = RandomCreatorService.get_instance().generatePassword();

    /** This Random is the java Random utility, that can be reused throughout tests. */
    protected Random _random = new Random();

    /** * Will divide Strings in CSVSources. */
    protected final String _divider = "|";

    /** Defines a character that is used to separate CSVSources */
    protected final char _delimiter = '|';

    @BeforeEach
    void beforeEach() {
        preStart();

        _items = new TestItems();
        _expected = new String();
        _actual = new String();
        _password = RandomCreatorService.get_instance().generatePassword();
    }

    /**
     * Will set the program to be run in testing mode.
     */
    private void preStart() {
        if (!Program.get_instance().is_applicationIsRunning()) {
            Program.get_instance().setTestingMode(true);
            Printer.get_instance().set_mode(PrinterMode.HIGH_CONTRAST);
            DbLibrary.get_instance().set_path();
        }

        Program.get_instance().applicationIsRunning();
    }

    /**
     * Must be used before each test method, since it will catch exceptions and print test informations.
     * @param function The test algorithm that will be applied,
     *                 if the algorithm return false, something unmeant occurred.
     */
    protected void test(Function<T,Boolean> function) {
        try {
            if (function.apply(null))
                Printer.get_instance().print(_print);
            else {
                addToPrint("Return of function is false, therefore something that was unmeant occurred...");
                Printer.get_instance().print(_print);
            }
        } catch (Exception e) {
            Printer.get_instance().print(_print, e);
            fail("An exception was caught in the main test method...", e);
        }
    }
}
