package laustrup.bandwichpersistence.core.persistence.worm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Junction {

  String title();

  Table.IdReference idReference() default @Table.IdReference;

  Table.Column[] entityColumns();

  Table.Column[] additionalColumns() default {};
}
