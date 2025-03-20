package laustrup.bandwichpersistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Scanner;

import static laustrup.bandwichpersistence.ProgramInitializer.argumentsToMap;

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
                System.out.println("\n\tApplication started!\n\nNow you can enter restart to restart Spring Boot or exit to exit program.\n");

                while (input == null)
                    try {
                        System.out.println();
                        input = Input.valueOf(_scanner.nextLine().toUpperCase());
                        System.out.println();
                    } catch (Exception ignored) {
                        System.out.println("\n\tCommand not recognized!");
                    }
                if (input.equals(Input.RESTART))
                    SpringApplication.exit(context);
            } while (input != Input.EXIT);

        System.out.println("\n\tWill now exit... Good bye\n\n");
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
        EXIT("exit");

        private final String _key;
    }
}
