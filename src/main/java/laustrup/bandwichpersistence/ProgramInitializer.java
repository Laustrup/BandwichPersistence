package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;
import laustrup.bandwichpersistence.core.libraries.SecurityLibrary;
import laustrup.bandwichpersistence.core.persistence.SQL;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.persistence.SQL.MySQL;
import static laustrup.bandwichpersistence.core.scriptorian.managers.ScriptorianManager.*;

public class ProgramInitializer {

    private static final Logger _logger = Logger.getLogger(ProgramInitializer.class.getSimpleName());

    static boolean startup(Map<String, String> arguments, String[] args, String defaultSchema) throws IllegalArgumentException {
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
                args,
                arguments.containsKey(DatabaseLibrary.CommandOption.IN_MEMORY.get_title())
            );

            return true;
        } catch (Exception e) {
            System.err.println("\n\tHad trouble initializing!\n\n" + e.getMessage());
            e.getStackTrace();

            return false;
        }
    }

    static void startUpTestMode(String schema) {
        SecurityLibrary.setup("123");
        if (!DatabaseLibrary.is_configured())
                DatabaseLibrary.setup(
                        MySQL,
                        null,
                        3307,
                        schema,
                        null,
                        "devword",
                        new String[]{},
                        true
                );
        else
            DatabaseLibrary.startup(schema);
        onStartup();
        runPopulation();
    }

    static Map<String, String> argumentsToMap(String[] arguments) {
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
                                DatabaseLibrary.CommandOption.IN_MEMORY.get_title(),
                                findArgument(DatabaseLibrary.CommandOption.IN_MEMORY.get_title(), arguments, true, false)
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

    public static String findArgument(String command, String[] arguments, boolean isFlag, boolean isNecessary) {
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
