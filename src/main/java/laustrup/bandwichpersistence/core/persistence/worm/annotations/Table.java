package laustrup.bandwichpersistence.core.persistence.worm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

  String value() default "";

  IdReference idReference() default @IdReference;

  @java.lang.annotation.Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Column {

    String value() default "";

    boolean isPrimary() default false;

    class Exception extends java.lang.IllegalStateException {
      public Exception(String message) {
        super(message);
      }

      public static Exception notFound(Class<?> entity, String when) {
        return new Exception(String.format("Couldn't find column for %s, when %s", entity.getSimpleName(), when));
      }
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface IdReference {

    String value() default "";
  }

  @java.lang.annotation.Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface ExcludedColumn {

  }

  @java.lang.annotation.Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Enum {

    String title();

    IdReference idReference() default @IdReference;

    Column[] columns();
  }

  @java.lang.annotation.Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Target {

  }

  @java.lang.annotation.Target(ElementType.CONSTRUCTOR)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Constructor {

  }

  @java.lang.annotation.Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Skeleton {

  }
}
