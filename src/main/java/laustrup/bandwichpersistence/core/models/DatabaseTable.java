package laustrup.bandwichpersistence.core.models;

import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.services.EternaryService.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseTable {

    String title();

    String idReference() default "";

    @Getter
    class Properties {

        private Optional<Class<?>> _clazz;
        private Optional<Field> _field;
        private String _title;
        private String _idReference;

        public Properties(Class<?> clazz, Field field, String title, String idReference) {
            _clazz = Optional.ofNullable(clazz);
            _field = Optional.ofNullable(field);
            _title = title;
            _idReference = defineIdReference(title, idReference);
        }

        public Properties(Class<?> clazz, String title, String idReference) {
            this(clazz, null, title, idReference);
        }

        public Properties(Field field, String title, String idReference) {
            this(null, field, title, idReference);
        }

        public Properties(String title, String idReference) {
            this(null, null, title, idReference);
        }

        public static Properties of(Properties target, Properties common) {
            return new Properties(
                    target.get_clazz().orElse(null),
                    null,
                    defineTitle(target.get_title(), common.get_title()),
                    ""
            );
        }

        public String defineIdReference(String title, String idReference) {
            _idReference = idReference != null && !idReference.isEmpty()
                    ? idReference
                    : fieldToColumnName(pluralToSingular(title), "id");
            return _idReference;
        }

        public static String defineTitle(String target, String common) {
            return String.join("_", List.of(pluralToSingular(target), common));
        }

        public String get_idReference() {
            return _idReference != null && !_idReference.isEmpty()
                    ? _idReference
                    : defineIdReference(_title, _idReference);
        }

        private static String pluralToSingular(String title) {
            String ending = title.substring(title.length() - 3);
            return stating(title.length() > 4 && ending.equals("ies"))
                    .then(title.substring(0, title.length() - 3) + "y")
                    .or(title.substring(0, title.length() - 2), ignored -> ending.endsWith("ses"))
                    .orElse(() -> stating(ending.endsWith("s"))
                            .then(title.substring(0, title.length() - 1))
                            .orElse(title)
                    );
        }
    }
}
