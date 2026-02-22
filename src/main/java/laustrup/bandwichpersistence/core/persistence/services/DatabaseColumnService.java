package laustrup.bandwichpersistence.core.persistence.services;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.getTableColumn;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.isIdless;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getFields;

public abstract class DatabaseColumnService {

  public static boolean fieldIsEqualToColumn(Field field, Table.Column column) {
    return field.isAnnotationPresent(Table.Column.class) &&
        getTableColumn(field.getDeclaringClass(), field.getName())
            .map(tableColumn -> tableColumn.equals(column))
            .orElse(false);
  }

  public static String fieldToColumnName(String... fields) {
    return fieldToColumnName(String.join("_", fields));
  }

  public static String fieldToColumnName(String field) {
    if (field == null)
      return null;

    if (field.charAt(0) == '_')
      field = field.substring(1);

    for (int i = 0; i < field.length(); i++) {
      char character = field.charAt(i);

      if (Character.isUpperCase(character)) {
        field = field.replace(String.valueOf(character), (i != 0 ? "_" : "") + Character.toLowerCase(character));
        if (i != 0)
          i++;
      }
    }

    return field;
  }

  public static AbstractMap.SimpleImmutableEntry<Field, DatabaseField> memberToColumnEntry(Field field) {
    return new AbstractMap.SimpleImmutableEntry<>(field, DatabaseField.of(field));
  }

  public static Map<? extends Member, DatabaseField> get_columns(Class<?> clazz) {
    if (clazz == null)
      return null;
    if (clazz.getDeclaredFields().length == 0)
      return new HashMap<>();

    return getFields(clazz).values().stream()
        .filter(field -> !field.isAnnotationPresent(Table.ExcludedColumn.class))
        .map(field -> DatabaseField.of(clazz, field))
        .collect(Collectors.toMap(DatabaseField::getMember, Function.identity()));
  }

  public static Optional<String> getIdColumnOf(Class<?> entity) {
    if (isIdless(entity))
      return Optional.empty();

    Map<String, Field> fields = getFields(entity);

    return Optional.of(Stream.of("_id", "_identity", "identity")
        .filter(field -> fields.values().stream().anyMatch(value -> field.equals(value.getName())))
        .findFirst()
        .orElseThrow(() -> new RuntimeException(String.format(
            "Could not find database field id in database field configuration! Class was %s",
            entity.getSimpleName()
        ))));
  }

  public static Stream<Table.Column> getIdColumnsOf(Class<?> entity) {
    return getFields(entity).values().stream()
        .map(field -> getTableColumn(field).orElse(null))
        .filter(column -> column != null && column.isPrimary());
  }
}
