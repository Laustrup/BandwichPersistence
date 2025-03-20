package laustrup.bandwichpersistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.ProgramInitializer.argumentsToMap;

public class Program {

    private static final String _password = "the password";

    private static final Scanner _scanner = new Scanner(System.in);

    public static void start(Class<?> applicationClass, String defaultSchema, String... args) {
        start(applicationClass, defaultSchema, null, args);
    }

    public static void start(
            Class<?> applicationClass,
            String defaultSchema,
            ILogo logo,
            String... args
    ) {
        Map<String, String> arguments = argumentsToMap(args);
        Stream<String> argumentsForDisplay = argumentDisplay(arguments);

        if (logo != null)
            logo.print(argumentsForDisplay);
        else
            argumentsForDisplay.forEach(System.out::println);

        if (arguments.get(Program.CommandOption.SKIP_CONFIRMATION.get_title()) == null) {
            System.out.println("\nTo continue, enter " + _password + "\n");

            while (!_scanner.nextLine().equals(_password))
                System.out.println("\tThat's not " + _password + "!\n");
        }

        running(applicationClass, arguments, args, defaultSchema);
    }

    private static Stream<String> argumentDisplay(Map<String, String> arguments) {
        Stream.Builder<String> builder = Stream.builder();

        builder.accept("Arguments:\n");
        arguments.forEach((key, value) -> builder.accept("\t" + key + ": " + value));

        return builder.build();
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

                while (input == null)
                    try {
                        input = Input.valueOf(_scanner.nextLine().toUpperCase());
                    } catch (Exception ignored) {
                        System.out.println("Command not recognized!");
                    }
                if (input.equals(Input.RESTART))
                    SpringApplication.exit(context);
            } while (input != Input.EXIT);

        System.out.println("Will now exit... Good bye");
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
