package laustrup.bandwichpersistence.core.persistence.models;

import laustrup.bandwichpersistence.core.persistence.exceptions.DatabaseDefinitionException;
import laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.persistence.models.EntityDataCollection.Key.conjunctionKeyOf;
import static laustrup.bandwichpersistence.core.persistence.worm.models.DatabaseDefinition.Entity.entryKeyOf;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNull;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public interface EntityDataCollection {

  default DatabaseDefinition get(Class<?> clazz) {
    return get(new Key(clazz));
  }

  default DatabaseDefinition get(Key key) {
    DatabaseDefinition data = getAll().get(key.get());

    if (data == null)
      throw new NullPointerException("Entity key " + key + " has no data");

    return data;
  }

  Map<String, DatabaseDefinition> getAll();

  default DatabaseDefinition entityOf(Class<?> entity) {
    return entityOf(entity, null);
  }

  default DatabaseDefinition entityOf(
      Class<?> target,
      @Nullable Class<?> common,
      Class<?>... relations
  ) {
    return Optional.ofNullable(getAll().get(ifNull(common)
        .then(() -> entryKeyOf(target))
        .orElse(() -> conjunctionKeyOf(target, common, relations))
    )).orElseThrow(() -> new DatabaseDefinitionException(String.format("""
            Couldn't find any entity of
            
                target = "%s"
                common = "%s"
                relations = "%s"
            """,
        target,
        common,
        Arrays.stream(relations)
            .map(Class::getName)
            .collect(Collectors.joining(Key.KEY_DELIMITER))
    )));
  }

  class Key {

    public static final String KEY_DELIMITER = "_";

    private final Class<?> _class;

    @Getter
    private final String _name;

    public Key(Class<?> clazz) {
      _class = clazz;
      _name = clazz.getName();
    }

    public Key(Class<?> target, Class<?> common, Class<?>... relations) {
      _class = null;
      _name = conjunctionKeyOf(target, common, relations);
    }

    public Key(String name) {
      _name = name;
      _class = null;
    }

    private Optional<Class<?>> get_class() {
      return Optional.ofNullable(_class);
    }

    public String get() {
      Optional<Class<?>>  clazz = get_class();

      return stating(clazz.isPresent())
          .then(() -> clazz.map(Class::getName).orElse(null))
          .orElse(_name);
    }

    public static String conjunctionKeyOf(Class<?> target, Class<?> common, Class<?>... relations) {
      return Seszt.of(target, common)
          .Add(relations).stream()
          .map(DatabaseDefinition.Entity::entryKeyOf)
          .collect(Collectors.joining(Key.KEY_DELIMITER));
    }

    @Override
    public String toString() {
      return get();
    }
  }
}
