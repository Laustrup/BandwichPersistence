package laustrup.bandwichpersistence.console_ui;

import laustrup.bandwichpersistence.repositories.DbLibrary;
import laustrup.bandwichpersistence.utilities.printers.Printer;
import laustrup.bandwichpersistence.utilities.printers.PrinterMode;

import lombok.Data;

import java.util.Scanner;

public class Greeter extends GreeterContents {

    /**
     * Singleton instance of the Greeter.
     */
    public static Greeter _instance = null;

    /**
     * Will render the static singleton Greeter instance.
     * @return The static singleton Greeter.
     */
    public static Greeter get_instance() {
        if (_instance == null) {
            _instance = new Greeter();
        }
        return _instance;
    }

    private Greeter() {}

    /**
     * Will set up the Printer before it will start up by printing instructions and scanning the inputs.
     */
    public static void startUp() {
        System.out.println(_introduction + _sectionSkip);
        readInput();

        allowContinue();

        promptPrinter();
        promptDb();

        System.out.println(_sectionSkip + _ending + _sectionSkip);
    }

    /**
     * When the application is running, this will display an information of the success.
     */
    public static void running() {
        Printer.get_instance().print("The application has now started.\n" +
                "like any other running program, press ctrl+c to close");
    }

    /**
     * A while loop that only stops, if there has been typed any key or start.
     * Will prompt the user to type again.
     */
    private static void allowContinue() {
        while (!_input.equalsIgnoreCase(Command.ANY_KEY._value) && !_quickStart) {
            System.out.println("\nThat isn't any key\n");
            readInput();
        }
        System.out.println("\n");
    }

    /**
     * Will print to the console information of instructions
     * and take inputs with a Scanner for the purpose of setting the Printer.
     */
    private static void promptPrinter() {
        while (!_quickStart) {
            printAndRead(_printerIntroduction + _highContrast + _optionSkip +
                    _noire + _optionSkip + _defaultMode + _sectionSkip);

            if (setPrinterMode())
                break;
        }
        System.out.println("\n");
    }

    /**
     * Will try to set the mode of the Printer of the previous set input.
     * If it can not convert the input, it will catch the exception and write a message to the console.
     * @return True if the conversion of the input is a success and the Printer's mode is set.
     */
    private static boolean setPrinterMode() {
        if (!_quickStart) {
            if (_input.equalsIgnoreCase(Command.SKIP._value)) return true;
            try {
                Printer.get_instance().set_mode(PrinterMode.valueOf(_input.toUpperCase()));
                return true;
            } catch (Exception e) {
                System.out.println("\n\tThat is not a valid input...\n\n");
                return false;
            }
        }
        else
            DbLibrary.get_instance().defaultSetup();

        return true;
    }

    /**
     * Will print to the console information of instructions
     * and take inputs with a Scanner for the purpose of setting the database connection-strings.
     */
    private static void promptDb() {
        String location,schema,username,password,
                redo = Command.REDO._value,start = Command.START._value,skip = Command.SKIP._value;
        int port = 0;
        boolean allowedMultipleQueries;

        while (!_quickStart) {
            System.out.println(_dbIntroduction + _sectionSkip);
            location = printAndRead("\t-Location ");
            if (_input.equalsIgnoreCase(redo))
                continue;
            if (_input.equalsIgnoreCase(start))
                break;
            if (_input.equalsIgnoreCase(skip)) {
                DbLibrary.get_instance().defaultSetup();
                break;
            }

            schema = printAndRead("\tSchema ");
            if (_input.equalsIgnoreCase(redo))
                continue;
            if (_input.equalsIgnoreCase(start))
                break;
            if (_input.equalsIgnoreCase(skip)) {
                DbLibrary.get_instance().defaultSetup();
                break;
            }

            username = printAndRead("\tUsername ");
            if (_input.equalsIgnoreCase(redo))
                continue;
            if (_input.equalsIgnoreCase(start))
                break;
            if (_input.equalsIgnoreCase(skip)) {
                DbLibrary.get_instance().defaultSetup();
                break;
            }

            password = printAndRead("\tPassword ");
            if (_input.equalsIgnoreCase(redo))
                continue;
            if (_input.equalsIgnoreCase(start))
                break;
            if (_input.equalsIgnoreCase(skip)) {
                DbLibrary.get_instance().defaultSetup();
                break;
            }

            while (true) {
                try {
                    String read = printAndRead("\tPort ");
                    if (read.equalsIgnoreCase(redo))
                        continue;
                    if (_input.equalsIgnoreCase(start))
                        break;
                    if (_input.equalsIgnoreCase(skip)) {
                        DbLibrary.get_instance().defaultSetup();
                        break;
                    }

                    port = Integer.parseInt(read);
                    break;
                } catch (Exception e) {
                    System.out.println("\nthat is not a port, a port is a number value.\n\n");
                }
            }
            if (_input.equalsIgnoreCase(redo))
                continue;
            if (_input.equalsIgnoreCase(Command.START._value))
                break;
            if (_input.equalsIgnoreCase(skip)) {
                DbLibrary.get_instance().defaultSetup();
                break;
            }

            allowedMultipleQueries = printAndRead("\tType y, if you want to allow multiple queries at once, " +
                    "when using an SQL statement").equalsIgnoreCase("y");
            if (_input.equalsIgnoreCase(redo))
                continue;
            if (_input.equalsIgnoreCase(Command.START._value))
                break;
            if (_input.equalsIgnoreCase(skip)) {
                DbLibrary.get_instance().defaultSetup();
                break;
            }

            if (setDb(new DbConfig(location,schema,username,password,port,allowedMultipleQueries)))
                break;
        }
    }

    /**
     * Will both print to the console and use the readInput() method.
     * @param content The content that will be printed to the console.
     * @return The return from the readInput() method
     */
    private static String printAndRead(String content) {
        System.out.println(content);
        return readInput();
    }

    /**
     * Will set the input into the value that the scanner has read and update boolean values concerned of the input.
     * @return The input value.
     */
    private static String readInput() {
        _input = new Scanner(System.in).nextLine();
        if (_input.equalsIgnoreCase(Command.START._value))
            _quickStart = true;
        if (_input.equalsIgnoreCase(Command.CANCEL._value)) {
            System.out.println("You have chosen to cancel the start of BandwichPersistence...");
            System.exit(2);
        }

        return _input;
    }

    /**
     * Will prompt the user for input for connection-strings and rules for the database.
     * @return True if the configuration was a success.
     */
    private static boolean setDb(DbConfig config) {
        String result = DbLibrary.get_instance().changeSetup(config.get_location(), config.get_port(),
                config.get_schema(),config.is_allowedMultipleQueries(),config.get_username(), config.get_password());
        System.out.println(result);

        return !result.equals("\tConfigurations were not allowed...");
    }

    @Data
    private static class DbConfig {

        /**
         * Field for configuring db-connection-strings.
         */
        private String _location,_schema,_username,_password;

        /**
         * Field for configuring db-connection-strings.
         */
        private int _port;

        /**
         * Field for configuring db-connection-strings.
         */
        private boolean _allowedMultipleQueries;

        public DbConfig(String location, String schema, String username, String password, int port, boolean allowedMultipleQueries) {
            _location = location;
            _schema = schema;
            _username = username;
            _password = password;
            _port = port;
            _allowedMultipleQueries = allowedMultipleQueries;
        }
    }
}
