package laustrup.bandwichpersistence.console_ui;

import laustrup.bandwichpersistence.BandwichPersistenceApplication;
import laustrup.utilities.console.Printer;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

/**
 * Is made to handle inputs from the console, while the application is running
 * and handle on those according to commands available.
 */
public class Runner {

    /**
     * Will receive any inputs from the terminal and take actions from it.
     * Will run in an eternal while loop, until exit has been entered as input.
     */
    public static void running(ConfigurableApplicationContext context) {
        while (true) {
            String input = new Scanner(System.in).nextLine();

            if (input.equalsIgnoreCase(ConsoleCommand.EXIT._value)) {
                input = insure(ConsoleCommand.EXIT._value, new ConsoleCommand[]{
                        ConsoleCommand.YES,
                        ConsoleCommand.Y,
                        ConsoleCommand.EXIT
                });

                if (inputIsOf(new ConsoleCommand[]{
                        ConsoleCommand.YES,
                        ConsoleCommand.Y,
                        ConsoleCommand.EXIT
                },input)) {
                    Printer.get_instance().print("The application will now shutdown!");
                    System.exit(667);
                } else
                    Printer.get_instance().print("The application will NOT shutdown!");
            }
            else if (input.equalsIgnoreCase(ConsoleCommand.RESTART._value)) {
                input = insure(ConsoleCommand.RESTART._value, new ConsoleCommand[]{
                        ConsoleCommand.YES,
                        ConsoleCommand.Y,
                        ConsoleCommand.RESTART
                });

                if (inputIsOf(new ConsoleCommand[]{
                        ConsoleCommand.YES,
                        ConsoleCommand.Y,
                        ConsoleCommand.RESTART
                }, input)) {
                    context.close();
                    Printer.get_instance().print("Wish to add any args? If not, simply press enter without any input," +
                            "otherwise type the arguments with | as a splitter.");
                    BandwichPersistenceApplication.main(new Scanner(System.in).nextLine().split("\\|"));
                }
            }
            else {
                Printer.get_instance().print("The command\n\n\t" + input + "\n\nis not recognized...");
            }
            Printer.get_instance().print("Ready for another command!");
        }
    }

    /**
     * Will print a message to the console, whether the action is curtain to be performed.
     * @param action The action that will be insured.
     * @param commands The commands available to type.
     * @return The nextLine scanner input.
     */
    private static String insure(String action, ConsoleCommand[] commands) {
        StringBuilder content = new StringBuilder("Are you sure you wish to " + action + "? Type ");
        for (int i = 0; i < commands.length; i++) {
            boolean isBeforeLast = i < commands.length - 1;
            content.append(commands[i]);

            if (isBeforeLast && (commands[i+1] == ConsoleCommand.Y || commands[i+1] == ConsoleCommand.YES))
                content.append("/");
            else if (isBeforeLast && (commands[i+1] == ConsoleCommand.EXIT || commands[i+1] == ConsoleCommand.RESTART))
                content.append(" or ");
        }

        Printer.get_instance().print(content + " to confirm.");

        return new Scanner(System.in).nextLine();
    }

    /**
     * Will check if the input is one of the commands with case ignored.
     * @param commands The commands to be checked.
     * @param input The input from the console.
     * @return True if the input is a command.
     */
    private static boolean inputIsOf(ConsoleCommand[] commands, String input) {
        for (ConsoleCommand command : commands)
            if (command._value.equalsIgnoreCase(input))
                return true;

        return false;
    }

    /** The commands that are available to be typed in the console. */
    private enum ConsoleCommand {
        EXIT("exit"),
        RESTART("restart"),
        Y("y"),
        YES("yes"),
        N("n"),
        NO("no");

        private String _value;

        ConsoleCommand(String command) {
            _value = command;
        }
    }
}
