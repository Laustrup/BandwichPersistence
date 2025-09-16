package laustrup.bandwichpersistence.core.persistence.models.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseEntity {

    String title();

    IdReference idReference() default @IdReference;

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Column {

        String title() default "";

        boolean isPrimary() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface IdReference {

        String title() default "";
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
