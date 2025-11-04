package laustrup.bandwichpersistence.core.persistence.worm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

  String value() default "";

  IdReference idReference() default @IdReference;

  boolean idLess() default false;

  @java.lang.annotation.Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Column {

    String value() default "";

    boolean isPrimary() default false;
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
}
