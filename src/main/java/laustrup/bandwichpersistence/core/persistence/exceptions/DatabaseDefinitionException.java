package laustrup.bandwichpersistence.core.persistence.exceptions;

public class DatabaseDefinitionException extends RuntimeException {

  public DatabaseDefinitionException(String message) {
    super(message);
  }

  public static DatabaseDefinitionException noIdReference(Class<?> clazz) {
    return new DatabaseDefinitionException(String.format("Couldn't find id reference of %s",
        clazz.getSimpleName()
    ));
  }
}
