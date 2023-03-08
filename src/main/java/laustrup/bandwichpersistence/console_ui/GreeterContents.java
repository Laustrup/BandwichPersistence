package laustrup.bandwichpersistence.console_ui;

/**
 * Contains messages for the console, that the Greeter.java will use
 * before and at the end of starting the application.
 */
public class GreeterContents {

    /** A field that will make a specific space with an escape-sequence.*/
    protected static final String _sectionSkip = "\n\n", _newLine = "\n\t", _optionSkip = "\n\n--\n\n";

    /** Message defined for printing. */
    protected static final String _introduction = "-- Welcome" + _sectionSkip +

            "\tThis application is meant for the Bandwich project, where this application's responsibility is to " + _sectionSkip +

            "\t\t1. handle incoming requests," + _newLine +
            "\t2. react accordingly to the database with the given data," + _newLine +
            "\t3. make sure nothing unintended will occur and" + _newLine +
            "\t4. return a response with the current state and data of the database to the view, that is the sender of the request." + _sectionSkip +

            "Now there will be a short prompting of settings for the application." + _sectionSkip +

            "\tIf at any time you wish to quickstart, simply type start " +
            "and the application will use the default settings for the rest of the fields." + _newLine +
            "If you wish to skip the prompt section, simply type skip " +
            "and the application will use the default settings of the section and the next will start." + _newLine +
            "In case you want to quit running the application, simply type cancel and the application will cancel to run." + _sectionSkip +
            "Type any key, when you are ready to begin.",
            _printerIntroduction = "-- Printer" + _sectionSkip +
                    "\tThe Printer is a class used for writing to the console, it can print in various ways and logs actions." + _sectionSkip +
                    "Please choose a mode for the printer.\n\n",
            _highContrast = "HIGH CONTRAST" + _sectionSkip +
                    "\tThis mode will change the output of the console into different colour, " +
                    "but needs to be able to use the escape sequences needed." + _newLine +
                    "If \033[0;92mTHIS\033[0m is in green, your system supports the colouring feature." + _sectionSkip +
                    "\tTo pick this mode, type high_contrast or simply skip",
            _noire = "NOIRE" + _sectionSkip +
                    "\tIs a lot like the default mode, but still contains some minor changes of borders." + _sectionSkip +
                    "\tTo pick this mode, type noire",
            _defaultMode = "DEFAULT" + _sectionSkip +
                    "\tIs recommended for users of a terminal, " +
                    "that doesn't support the Java console text colouring escape-sequences," + _newLine +
                    "since it will occur in plain print outputs." + _newLine +
                    "Logging will still be processed." + _sectionSkip +
                    "\tTo pick this mode, type default",

    _dbIntroduction = "-- Database" + _sectionSkip +
            "\tThis section is for the connections strings to the database." + _newLine +
            "In case you have the default values already embedded in the application, " +
            "we recommend you to skip this part." + _sectionSkip +
            "You will be prompted a field, where you'll type the wished input for that field." + _sectionSkip +
            "There are also the default values available, if they are acquired." +
            "If you made a mistake, simply type redo and this section will start again.",
            _ending = "Thank you, your configuration settings have been added and the application will now start.." + _sectionSkip +
                    "\tHave a good day!";

    /** A reusable field that contains the last input of the user from the Scanner. */
    protected static String _input;

    /**
     * Will be true, if the user types start.
     * In that case it will not enter any while loops.
     */
    protected static boolean _quickStart = false;

    /** Inputs that will have a command. */
    protected enum Command {
        ANY_KEY("any key, when you are ready"),
        START("start"),
        SKIP("skip"),
        CANCEL("cancel"),
        REDO("redo");

        public final String _value;
        private Command(String value) { _value = value; }
    }
}
