package laustrup.bandwichpersistence.repositories;

import laustrup.bandwichpersistence.Defaults;

import lombok.Getter;

public class DbLibrary {

    /** Determines if the current connection is to an in memory H2 db for testing purpose or the MySQL. */
    @Getter
    private final boolean _testing = true;

    /** Will allow a SQL statement to have multiple queries at once. */
    private String _allowMultipleQueries = Defaults.get_instance().get_sqlAllowMultipleQueries();

    /** The location of the database. */
    private String _location = Defaults.get_instance().get_dbLocation();

    /** The port of the database. */
    private int _port = Defaults.get_instance().get_dbPort();

    /** The schema that will be used of the database. */
    private String _schema = Defaults.get_instance().get_dbSchema();

    /**
     * Determines if this DbLibrary has been configured yet,
     * it can only configure the values, if they aren't already configured
     */
    private boolean _setupIsConfigured = false;

    /** Value for the DbGate with the purpose of creating a connection. */
    @Getter
    private String _path = setPath(),
        _user = _testing ? "sa" : Defaults._instance.get_dbUser(),
        _password = _testing ? "" : Defaults.get_instance().get_dbPassword(),
        _driverClassName = _testing ? "org.h2.Driver" : "com.mysql.cj.jdbc.Driver";

    /**
     * Will change the fields of crating a connection for the database,
     * but only in case they haven't already been configured.
     * If fields are null, empty or integers are 0, they will not change the values.
     * @param location The location of the database.
     * @param port The port used for the schema.
     * @param schema The schema that is wished to use.
     * @param allowMultipleQueries If true, it will allow a single SQL statement
     *                             to be able to run multiple queries at once.
     * @param user The user for the database, that has rules for uses of the database.
     * @param password The password to insure the User has access permitted for the database.
     * @return A statement of the fields that has been updated,
     * if the configuration is not allowed, it will return that it wasn't.
     */
    public String changeSetup(String location, int port, String schema,
                              boolean allowMultipleQueries,
                              String user, String password) {
        boolean changeLocation = !(location == null || location.isEmpty()),
                changePort = port > 0,
                changeSchema = !(schema == null || schema.isEmpty()),
                changeUser = !(user == null || user.isEmpty()),
                changePassword = !(password == null || password.isEmpty()),
                allowConfiguration = !_setupIsConfigured;

        if (allowConfiguration) {
            _location = changeLocation ?  location : _location;
            _port = changePort ? port : _port;
            _schema = changeSchema ? schema : _schema;
            _allowMultipleQueries = !allowMultipleQueries ? new String() : _allowMultipleQueries;
            _user = changeUser ? user : _user;
            _password = changePassword ? password : _password;
        }

        setPath();
        _setupIsConfigured = true;

        if (allowConfiguration) {
            String fields = (changeLocation ? "Location\n" : "") +
                    (changePort ? "Port\n" : "") +
                    (changeSchema ? "Schema\n" : "") +
                    (!allowMultipleQueries ? "Will not allow multiple queries\n" : "") +
                    (changeUser ? "User\n" : "") +
                    (changePassword ? "Password\n" : "");

            return "\tFields that has been successfully changed are:\n\n" + fields + "\n";
        }
        else
            return "\tConfigurations were not allowed...";
    }

    /**
     * Collects a string of a path to the database from the necessarily fields needed,
     * therefore the path should be set after location, port, schema and allow multiple queries.
     * @return The collected string.
     */
    private String setPath() {
        _path = "jdbc:mysql:" + (_testing ? "h2:mem:testdb"
                : "//" + _location + ":" + _port + "/" + _schema + _allowMultipleQueries);
        return _path;
    }

    /**
     * If at startup there isn't a wish for a different setup, this will use the default setup.
     * @return A message describing the setup is default.
     */
    public String defaultSetup() {
        _setupIsConfigured = true;
        return "Setup is default!";
    }

    public static DbLibrary _instance = null;

    /**
     * Lazy fetch of getting the Crate instance.
     * If the instance is null, it will use a private constructor to initialise the instance.
     * @return The static instance of the Crate.
     */
    public static DbLibrary get_instance() {
        if (_instance == null) { _instance = new DbLibrary(); }
        return _instance;
    }

    private DbLibrary() {}
}
