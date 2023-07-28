## Bandwich

Bandwich is a collection of projects, where venues and bands/artists can connect with each other,
with the purposes of arranging gigs together with one and another.

These gigs can be hosted as an event for a venue on Bandwich, this event can be published and promoted with Bandwich,
including other profiles of participants can sign up and join the events.

# BandwichPersistence

### Introduction
Is still in development, which is test driven with solo developer Laust Eberhardt Bonnesen aka. Laustrup.

This web project is a Spring Boot Maven project with Java language that use JDBC to connect with a database.

Spring Boot project can be found [HERE](https://github.com/spring-projects/spring-boot).

The responsibilities of this application is:
* Handling incoming requests.
* Reacting accordingly to the database with the given data.
* Make sure nothing unintended will occur.
* Return a response with the current state and data of the database to the view, that is the sender of the request.

It can use either customized connections-strings or defaults.

Default connection-strings and database test values is not included, since they are hidden by .gitignore.
The DbLibrary will need the information of your connection strings. In order to make this work,
create a java class at laustrup.bandwichpersistence called Defaults.java, it could look like this, with hidden values changed with your values:

```
public class Defaults {

    public static Defaults _instance = null;

    public static Defaults get_instance() {
        if (_instance == null) { _instance = new Defaults(); }
        return _instance;
    }

    private Defaults() {}

    @Getter
    private final boolean _included = true;

    @Getter
    private final String _sqlAllowMultipleQueries = "?allowMultiQueries=true";

    @Getter
    private final String _dbLocation = "***.*.*.*";

    @Getter
    private final int _dbPort = ****;

    @Getter
    private final String _dbSchema = "****_schema";

    @Getter
    private final String _dbPath = "jdbc:mysql://" + _dbLocation + ":" + _dbPort + "/" + _dbSchema + _sqlAllowMultipleQueries,
            _dbUser = "****", _dbPassword = "****";

    @Getter
    private final String _directory = "C:*****/IdeaProjects/BandwichPersistence";

    public String get_directory(String delimiter) {
        return _directory + delimiter;
    }
}
```

For testing there is a SQL file called default_values.sql, which is also hidden by the .gitignore.

View project can be found [HERE](https://github.com/Laustrup/BandwichView), although it is still being developed.

### Laustrup dependencies
There are some Laustrup dependencies used in this project, they can be read at:

* [Utilities](https://github.com/laustrup/utilities)
* [Models](https://github.com/Laustrup/Models/tree/master/BandwichModels)
* [Tests](https://github.com/Laustrup/QualityAssurance)


Keep in mind, that at the moment these dependencies are only uploaded to the GitHub packages of Maven, and not the local Maven repository.
This might respond in some issues when downloading them.

### Starting the application
This application should be started as a maven project, either from an IDEA with a configuration or with the console through maven with the commandline ```mvn spring-boot:run``` at the root of the project.
As a default option, it will run automatically in testing-mode, this can be changed in the BandwichPersistenceApplication.java class.

It is intended to be build as a JAR (Java Archive), which will be executed with the commandline ```java jar BandwichPersistence.jar```. Can also be build as WAR (Web Archive), where a server, such as Tomcat, will run the application.

#### Startup
When the application has been started, you will either be able to start immediately, if the default values are added,
or type in the different opportunities, when prompted at startup.
* ###### Printer

  As mentioned in the introduction, the Printer is used for writing to the console.
  
  The various previous mentioned modes are able to be chosen:
  * Default
  * Noir
  * High Contrast


* ###### Database

  Here the different settings such as connection-strings can be defined
  
  Settings to define are:

  * Location
  * Schema
  * Username
  * Password
  * Port
  * Multiple queries of single statement

After these settings have been typed and entered, Spring Boot will start the application and the application is running.

In case there is a problem with Spring Boot startup, the application will shut down and the issue needs to be fixed.

#### Runtime
When the application is running, there is commands available:

* ###### Restart
  Will simply restart the application from the beginning with the application startup.

* ###### Exit
  Is for when the application should be shutdown, which will cause the context to close.

In case there is any exception occurring while running,
the application will restart itself in milliseconds without any runtime issues,
preventing unintentional acts and eternal loops.