package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public interface EntityDataCollection {

    default DatabaseDefinition get(Class<?> clazz) {
        return get(new Key(clazz));
    }

    default DatabaseDefinition get(Key key) {
        DatabaseDefinition data = getAll().get(key);

        if (data == null)
            throw new NullPointerException("Entity key " + key + " has no data");

        return data;
    }

    Map<Key, DatabaseDefinition> getAll();

    default DatabaseDefinition.Entity entityOf(Class<?> entity) {
        return (DatabaseDefinition.Entity) getAll().get(new Key(entity));
    }

    default DatabaseDefinition.Conjunction conjunctionOf(Class<?> target, Class<?> common) {
        return (DatabaseDefinition.Conjunction) getAll().get(new Key(target, common));
    }

    class Key {

        private final Class<?> _class;

        @Getter
        private final String _name;

        public Key(Class<?> clazz) {
            _class = clazz;
            _name = clazz.getSimpleName();
        }

        public Key(Class<?> target, Class<?> common) {
            _class = null;
            _name = conjunctionKey(target, common);
        }

        public Key(String name) {
            _name = name;
            _class = null;
        }

        private Optional<Class<?>> get_class() {
            return Optional.ofNullable(_class);
        }

        public String get() {
            return stating(get_class().isPresent())
                    .then(_class.getSimpleName())
                    .orElse(_name);
        }

        public static String conjunctionKey(Class<?> target, Class<?> common) {
            return String.join(
                    "_",
                    target.getSimpleName(),
                    common.getSimpleName()
            );
        }

        @Override
        public String toString() {
            return get();
        }
    }
}
