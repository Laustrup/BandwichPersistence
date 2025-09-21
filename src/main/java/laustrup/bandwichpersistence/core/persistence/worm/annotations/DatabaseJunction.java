package laustrup.bandwichpersistence.core.persistence.worm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseJunction {

    String title();

    DatabaseEntity.IdReference idReference() default @DatabaseEntity.IdReference;

    DatabaseEntity.Column[] entityColumns();

    DatabaseEntity.Column[] additionalColumns() default {};
}
