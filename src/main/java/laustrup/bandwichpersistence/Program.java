package laustrup.bandwichpersistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Scanner;

import static laustrup.bandwichpersistence.ProgramInitializer.argumentsToMap;
import static laustrup.bandwichpersistence.core.scriptorian.managers.ScriptorianManager.runInjections;

public class Program {

    private static final String _password = "the password";

    private static final Scanner _scanner = new Scanner(System.in);

    public static void start(
            Class<?> applicationClass,
            String defaultSchema,
            String... args
    ) {
        Map<String, String> arguments = argumentsToMap(args);
        displayArguments(arguments);

        if (arguments.get(Program.CommandOption.SKIP_CONFIRMATION.get_title()) == null) {
            System.out.println("\nTo continue, enter " + _password + "\n");

            while (!_scanner.nextLine().equals(_password))
                System.out.println("\n\tThat's not " + _password + "!\n");
        }

        System.out.println("\n\tSpring Boot will now start application\n");

        running(applicationClass, arguments, args, defaultSchema);
    }

    private static void displayArguments(Map<String, String> arguments) {
        System.out.println("Arguments:\n");
        arguments.forEach((key, value) -> System.out.println(key + ": " + value));
    }

    private static void running(
            Class<?> applicationClass,
            Map<String, String> arguments,
            String[] args,
            String defaultSchema
    ) {
        Input input;
        boolean startApplication = ProgramInitializer.startup(arguments, args, defaultSchema);

        if (startApplication)
            do {
                input = null;
                ConfigurableApplicationContext context = SpringApplication.run(applicationClass, args);
                System.out.println("""
                    
                        Application started!
                    
                    Now you can enter the following commands:
                    
                    * restart: Restarts Spring Boot, but program keeps running
                    * inject: With generate some data for developers, if any sqls are specified
                    * exit: Simply shuts down the whole application
                    """);

                while (input == null) {
                    boolean doRestart = false;

                    try {
                        System.out.println();
                        input = Input.valueOf(_scanner.nextLine().toUpperCase());
                        System.out.println();
                    } catch (Exception ignored) {
                        System.out.println("\n\tCommand not recognized!");
                    }

                    switch (input) {
                        case RESTART: {
                            SpringApplication.exit(context);
                            doRestart = true;
                            break;
                        } case INJECT: {
                            runInjections();
                            input = null;
                            break;
                        }
                    }

                    if (doRestart)
                        break;
                }
            } while (input != Input.EXIT);

        System.out.println("\n\tWill now exit... Goodbye\n\n");
        System.exit(-1);
    }

    @Getter @AllArgsConstructor
    public enum CommandOption {
        SKIP_CONFIRMATION("skipConfirmation", true);

        private final String _title;

        private final boolean _flag;
    }

    @Getter @AllArgsConstructor
    public enum Input {
        RESTART("restart"),
        EXIT("exit"),
        INJECT("inject");

        private final String _key;
    }
}
