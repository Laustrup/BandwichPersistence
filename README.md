## Bandwich

Bandwich is a collection of projects, where venues and bands/artists can connect with each other,
with the purposes of arranging gigs together with one and another.

These gigs can be hosted as an event for a venue on Bandwich, this event can be published and promoted with Bandwich,
including other profiles of participants can sign up and join the events.

It also includes functionality that the whole venue organisation.

# BandwichPersistence

### Introduction
This web project is a [Spring Boot](https://github.com/spring-projects/spring-boot) [Maven](https://maven.apache.org/) project with [Java](https://www.oracle.com/java/technologies/) language that use [JDBC](https://www.baeldung.com/java-jdbc) to connect with a [MySQL](https://www.mysql.com/) database.

The responsibilities of this application is:

* Handling incoming requests.
* Reacting accordingly to the database with the given data.
* Make sure nothing unintended will occur.
* Return a response with the current state and data of the database to the view, that is the sender of the request.

### Starting the application
This application should be started as a [Maven](https://maven.apache.org/) project, either from an IDEA with a configuration or with the console through [Maven](https://maven.apache.org/) with the commandline ```mvn spring-boot:run``` at the root of the project.
Additionally it takes run arguments to specify various properties and states.

#### Run arguments

These arguments can either be properties, which will be required to have the format ``<key>=<value>``, or state that simply is written as the key, since it workes as a flag.

Since [Spring Boot](https://github.com/spring-projects/spring-boot) is used, it requires to be followed by ``-Dspring-boot.run.arguments=<arguments>`` in the same start command, when starting the application.

All properties marked with * is required.

##### properties

* ###### gibberish*
  An extra step for password encryption.
  
  Must be 3 digits in length and be valued as hex decimal format.
  Important to remember and reuse when running on a database, that has had that gibberish introduced.
* ###### databasePort
  The port for the database, that will be added to the connectionstring.

  Will by default be 3306, which is the default [MySQL](https://www.mysql.com/) port.
* ###### sql*
  Specifies which SQL syntax to use.
* ###### databaseTarget
  The location of the database.
  
  Will by default be localhost.
* ###### databaseSchema
  The database schema that will be used for queries.
  
  Will by default be as implemented in the application, which for this project is bandwich.
* ###### databaseUser
  The user for the database.

  Keep in mind that queries will be executed with the privileges of this user.

  Will by default be root.
* ###### databasePassword*
  The password that will be required for accessing the database.
  
##### States

* ###### skipConfirmation

  Will simply go directly to initialization of context and skip confirmation of arguments. 

#### Runtime
When the application is running, there is commands available:

* ###### Restart
  Will simply restart the application from the beginning with the application startup.

* ###### Exit
  Is for when the application should be shutdown, which will cause the context to close.