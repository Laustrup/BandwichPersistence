arg databaseUser
arg databasePassword
arg gibberish
arg directory="/home/bandwich/BandwichPersistence"

from openjdk:21-oracle
arg JAR_FILE=build/libs/*.jar
copy ${JAR_FILE} app.jar
entrypoint ["java","-jar","/app.jar"]
