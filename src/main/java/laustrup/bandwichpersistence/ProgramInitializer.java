package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;
import laustrup.bandwichpersistence.core.libraries.SecurityLibrary;
import laustrup.bandwichpersistence.core.persistence.SQL;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProgramInitializer {

    private static final Logger _logger = Logger.getLogger(ProgramInitializer.class.getSimpleName());

    private static final String _password = "the password";

    private static final Scanner _scanner = new Scanner(System.in);

    public static boolean startup(String[] args, String defaultSchema) throws IllegalArgumentException {
        Map<String, String> arguments = argumentsToMap(args);
        displayArguments(arguments);

        try {
            SecurityLibrary.setup(arguments.get(SecurityLibrary.CommandOption.GIBBERISH.get_title()));

            String port = arguments.get(DatabaseLibrary.CommandOption.DATABASE_PORT.get_title());
            String databaseSchema = arguments.get(DatabaseLibrary.CommandOption.DATABASE_SCHEMA.get_title());

            DatabaseLibrary.setup(
                SQL.valueOf(arguments.get(DatabaseLibrary.CommandOption.SQL.get_title())),
                arguments.get(DatabaseLibrary.CommandOption.DATABASE_TARGET.get_title()),
                port != null
                        ? Integer.valueOf(
                                Objects.requireNonNull(
                                        arguments.get(DatabaseLibrary.CommandOption.DATABASE_PORT.get_title())
                                )
                        ) : null,
                databaseSchema != null ? databaseSchema : defaultSchema,
                arguments.get(DatabaseLibrary.CommandOption.DATABASE_USER.get_title()),
                arguments.get(DatabaseLibrary.CommandOption.DATABASE_PASSWORD.get_title()),
                args
            );

            return true;
        } catch (Exception e) {
            System.err.println("\n\tHad trouble initializing!\n\n" + e.getMessage());
            e.getStackTrace();

            return false;
        }
    }

    private static Map<String, String> argumentsToMap(String[] arguments) {
        return Stream.of(new String[][]{
                {
                        SecurityLibrary.CommandOption.GIBBERISH.get_title(),
                        findArgument(SecurityLibrary.CommandOption.GIBBERISH.get_title(), arguments, false, true)
                },
                {
                        DatabaseLibrary.CommandOption.DATABASE_PORT.get_title(),
                        findArgument(DatabaseLibrary.CommandOption.DATABASE_PORT.get_title(), arguments, false, false)
                },
                {
                        DatabaseLibrary.CommandOption.SQL.get_title(),
                        findArgument(DatabaseLibrary.CommandOption.SQL.get_title(), arguments, false, true)
                },
                {
                        DatabaseLibrary.CommandOption.DATABASE_TARGET.get_title(),
                        findArgument(DatabaseLibrary.CommandOption.DATABASE_TARGET.get_title(), arguments, false, false)
                },
                {
                        DatabaseLibrary.CommandOption.DATABASE_SCHEMA.get_title(),
                        findArgument(DatabaseLibrary.CommandOption.DATABASE_SCHEMA.get_title(), arguments, false, false)
                },
                {
                        DatabaseLibrary.CommandOption.DATABASE_USER.get_title(),
                        findArgument(DatabaseLibrary.CommandOption.DATABASE_USER.get_title(), arguments, false, true)
                },
                {
                        DatabaseLibrary.CommandOption.DATABASE_PASSWORD.get_title(),
                        findArgument(DatabaseLibrary.CommandOption.DATABASE_PASSWORD.get_title(), arguments, false, false)
                },
                {
                        Program.CommandOption.SKIP_CONFIRMATION.get_title(),
                        findArgument(Program.CommandOption.SKIP_CONFIRMATION.get_title(), arguments, true, false)
                }
        })
                .filter(data -> data[1] != null)
                .collect(Collectors.toMap(data -> data[0], data -> data[1]));
    }

    private static void displayArguments(Map<String, String> arguments) {
        System.out.println("Arguments:\n");
        arguments.forEach((key, value) -> System.out.println("\t" + key + ": " + value));

        if (arguments.get(Program.CommandOption.SKIP_CONFIRMATION.get_title()) == null) {
            System.out.println("\nTo continue, enter " + _password + "\n");

            while (!_scanner.nextLine().equals(_password))
                System.out.println("\tThat's not " + _password + "!\n");
        }
    }

    private static String findArgument(String command, String[] arguments, boolean isFlag, boolean isNecessary) {
        try {
            String sentence = Arrays.stream(arguments).toList().stream().filter(argument -> (
                            argument.contains("=")
                                    &&
                            argument.split("=")[0].equals(command)
                    ) || (isFlag && argument.equals(command))
            )
                    .findFirst()
                    .orElseGet(() -> {
                        if (isNecessary)
                            throw new IllegalArgumentException(String.format("""
                                    Couldn't find command %s from arguments when initializing program!
                                    """, command
                            ));
                        else
                            return null;
                    });

            return sentence == null
                    ? null
                    : (isFlag
                            ? sentence
                            : sentence.split("=")[1]
                    );
        } catch (Exception e) {
            return null;
        }
    }
}
