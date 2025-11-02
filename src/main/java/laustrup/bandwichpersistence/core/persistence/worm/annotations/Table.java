package laustrup.bandwichpersistence.core.persistence.worm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

  String value() default "";

  IdReference idReference() default @IdReference;

  boolean idLess() default false;

  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Column {

    String value() default "";

    boolean isPrimary() default false;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface IdReference {

    String value() default "";
  }

  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface ExcludedColumn {

  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Enum {

    String title();

    IdReference idReference() default @IdReference;

    Column[] columns();
  }
}
