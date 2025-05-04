package laustrup.bandwichpersistence.core.libraries;

import laustrup.bandwichpersistence.core.persistence.SQL;
import laustrup.bandwichpersistence.core.managers.ManagerService;
import laustrup.bandwichpersistence.core.repositories.DatabaseLibraryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;

public class DatabaseLibrary {

    @Getter
    private static String _driver;
    
    @Getter
    private static SQL _sql;

    @Getter
    private static String _target;

    @Getter
    private static int _port;

    @Getter
    private static String _schema;

    @Getter
    private static String _user;

    @Getter
    private static String _password;

    @Getter @Setter
    private static boolean _schemaExists;

    @Getter
    private static Properties _properties;

    @Getter
    private static boolean _isInMemory;

    private static boolean _isConfigured;

    public static void setup(
            SQL sql,
            String target,
            Integer port,
            String schema,
            String user,
            String password,
            String[] arguments,
            boolean isInMemory
    ) {
        setup(
                sql,
                target,
                port,
                schema,
                user,
                password,
                convertToProperties(arguments),
                isInMemory
        );
    }

    public static void setup(
            SQL sql,
            String target,
            Integer port,
            String schema,
            String user,
            String password,
            Properties properties,
            boolean isInMemory
    ) {
        if (_isConfigured)
            throw new IllegalStateException("Database library is already configured");
        if (schema == null)
            throw new IllegalArgumentException("Database must have a schema");
        
        _sql = sql;
        _driver = _sql == null ? null : switch (_sql) {
            case MySQL -> "com.mysql.cj.jdbc.Driver";
            case H2 -> "org.h2.Driver";
        };
        _target = Objects.requireNonNullElse(target, "localhost");
        _port = Objects.requireNonNullElse(port, 3306);
        _schema = schema;
        _user = Objects.requireNonNullElse(user, "root");
        _password = password;
        _properties = properties;
        _isInMemory = isInMemory;
        databaseInteraction(() -> DatabaseLibraryRepository.createSchemaIfNotExists(_schema));
        _isConfigured = true;
    }

    private static Properties convertToProperties(String[] arguments) {
        return new Properties(
                Stream.of(arguments)
                        .filter(argument -> argument.equals(CommandOption.DISALLOW_MULTIPLE_QUERIES.get_title()))
                        .toList()
                        .isEmpty()
        );
    }

    public static String get_connectionString() {
        return actIfIsConfigured(() -> get_rootConnectionString(false) + (_schema == null ? "" : _schema) + get_URLPropertyParameters());
    }

    public static String get_rootConnectionString(boolean includeProperties) {
        return String.format(
                "jdbc:%s://%s:%s/",
                _sql.name().toLowerCase(),
                _target,
                _port
        ) + (includeProperties
                ? get_URLPropertyParameters()
                : ""
        );
    }

    public static String get_URLPropertyParameters() {
        return _properties.get_options().stream()
                .filter(Properties.Option::is_isEnabled)
                .map(option -> option.get_command().get_parameter())
                .collect(Collectors.joining());
    }

    public static boolean isH2InMemory() {
        return _isInMemory && _sql == SQL.H2;
    }

    public static String actIfIsConfigured(Supplier<String> act) {
        return _isConfigured ? act.get() : null;
    }
    
    @Getter @AllArgsConstructor
    public enum CommandOption {
        SQL("sql", false),
        DATABASE_TARGET("databaseTarget", false),
        DATABASE_PORT("databasePort", false),
        DATABASE_SCHEMA("databaseSchema", false),
        DATABASE_USER("databaseUser", false),
        DATABASE_PASSWORD("databasePassword", false),
        DISALLOW_MULTIPLE_QUERIES("disAllowMultiQueries", false),
        IN_MEMORY("inMemory", true),;

        private final String _title;

        private final boolean _flag;
    }

    @Getter
    public static class Properties {

        private final List<Option> _options = new ArrayList<>(List.of(
                new Option(Option.Command.ALLOW_MULTIPLE_QUERIES)
        ));

        public Properties(
                boolean allowMultipleQueries
        ) {
            _options.forEach(option -> {
                if (allowMultipleQueries)
                    option.set_isEnabled(allowMultipleQueries);
            });
        }

        @Getter @AllArgsConstructor
        public static class Option {

            @Setter
            private boolean _isEnabled;

            private final Command _command;

            public Option(Command command) {
                _command = command;
            }

            @Getter @AllArgsConstructor
            public enum Command {
                ALLOW_MULTIPLE_QUERIES("?allowMultiQueries=true");

                private final String _parameter;
            }
        }
    }
}
