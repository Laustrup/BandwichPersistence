# Bandwich

Bandwich is a collection of projects, where venues and bands/artists can connect with each other,
with the purposes of arranging gigs together with one and another.

These gigs can be hosted as an event for a venue on Bandwich, this event can be published and promoted with Bandwich,
including other profiles of participants can sign up and join the events.

## BandwichPersistence

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

Default connection-strings and database test values is not included, since they are hidden by gitignore.
The DbLibrary will need the information of your connection strings.

For testing is there a SQL file called default_values.sql, which is also hidden by the gitignore.

View project can be found [HERE](https://github.com/Laustrup/BandwichView), although it is still being developed.

### Utilities

There are used special implemented utilities,
that are used across the project and are meant to be moved to another project with connection in maven.

The utilities are:

##### Collections

* ###### Liszt
  Implements a List of element E in an append way of adding elements.
  It also implements the interface ICollectionUtility, which contains extra useful methods.
  An extra detail is that this class also uses a map, which means that
  the approach of getting also can be done through the map, this also
  means, that they will be saved doing add and removed at remove.
  Index can both start at 0 or 1, every method starting with an uppercase
  letter starts with 1 instead 0 in the parameters.

* ###### Seszt
  Implements a Set of element E in an append way of adding elements.
  It also implements the interface ICollectionUtility, which contains extra useful methods.
  An extra detail is that this class also uses a map, which means that
  the approach of getting also can be done through the map, this also
  means, that they will be saved doing add and removed at remove.
  Index can both start at 0 or 1, every method starting with an uppercase
  letter starts with 1 instead 0 in the parameters.

##### Console

* ###### Printer
  Will handle printing of statements to the console.
  Contains three different options for modes, default will use a normal println,
  the others have a border around each print, noir mode is without colour
  and high contrast mode is with colour of high contrast.
  Is intended to log each print, but isn't implemented yet.

##### Parameters

* ###### Plato
  A utility class, that behaves as a boolean, but with extra features.
  Is named after the philosopher Plato ironically, because of Plato's duality theory.
  Instead of just being true or false, it has more values, such as undefined.
  Uses an enum for identifying those values.
  Can also be null, since it's a class object.

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