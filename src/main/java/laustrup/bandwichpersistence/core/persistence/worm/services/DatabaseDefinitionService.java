package laustrup.bandwichpersistence.core.persistence.worm.services;

import laustrup.bandwichpersistence.core.persistence.exceptions.DatabaseDefinitionException;
import laustrup.bandwichpersistence.core.persistence.models.members.CommonField;
import laustrup.bandwichpersistence.core.persistence.models.members.InheritanceField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Junction;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.persistence.worm.models.TableColumnData;
import laustrup.bandwichpersistence.core.services.ClassFieldService;
import laustrup.bandwichpersistence.core.services.EternaryService.Operator.Property;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Optional;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseTableService.*;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getDeclared;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

@Slf4j
public abstract class DatabaseDefinitionService {

  public static Optional<Annotation> get_databaseDefinition(Class<?> clazz) {
    return stating(Liszt.of(
        Property.inCase(clazz.isAnnotationPresent(Table.class))
            .then(() -> get_table(clazz)),
        Property.inCase(clazz.isAnnotationPresent(Junction.class))
            .then(() -> get_junction(clazz)),
        Property.inCase(clazz.isAnnotationPresent(Table.Enum.class))
            .then(() -> get_tableEnum(clazz)),
        Property.inCase(clazz.isAnnotationPresent(Table.Constructor.class))
            .then(() -> get_constructor(clazz))
    )).orEmpty();
  }

  public static Table get_table(Class<?> clazz) {
    return handleGet(clazz, Table.class);
  }

  private static Table.Enum get_tableEnum(Class<?> clazz) {
    return handleGet(clazz, Table.Enum.class);
  }

  public static Table.Constructor get_constructor(Class<?> clazz) {
    return handleGet(clazz, Table.Constructor.class);
  }

  private static <ANNOTATION extends Annotation> ANNOTATION handleGet(Class<?> clazz, Class<ANNOTATION> annotation) {
    return stating(clazz != null)
        .then(() -> ifAnnotationIsPresent(clazz, annotation, clazz.getAnnotation(annotation)))
        .orElseThrow(new NullPointerException(String.format("Can't get %s since its class is null!", annotation.getSimpleName())));
  }

  @SuppressWarnings("unchecked")
  public static <CLASS> Constructor<CLASS> get_tableConstructor(Class<?> clazz) {
    if (clazz == null)
      throw new NullPointerException("Can't get constructor since its class is null!");

    Liszt<Constructor<?>> constructors = Liszt.of(Arrays.stream(clazz.getConstructors())
        .filter(constructor -> constructor.isAnnotationPresent(Table.Constructor.class))
    );

    if (constructors.size() > 1)
      throw new IllegalStateException("Only one constructor allowed for a builder!");

    return (Constructor<CLASS>) stating(constructors.size() == 1)
        .then(() -> constructors.stream().findAny().orElseThrow())
        .orElseThrow(new IllegalStateException(stating(constructors.isEmpty())
            .then("Couldn't find any constructor for Database Definition, please add one for " + clazz.getSimpleName())
            .orElse("Only one constructor allowed for a builder! The class was " + clazz.getSimpleName())
        ));
  }

  public static TableColumnData getTableColumnData(Class<?> clazz, Table.Column column) {
    return getTableColumnData(clazz, ClassFieldService.getField(clazz, column).orElseThrow().getName());
  }

  public static TableColumnData getTableColumnData(Class<?> clazz, String columnName) {
    return new TableColumnData(
        getTableColumn(clazz, columnName),
        getDeclared(clazz, columnName)
    );
  }

  public static Table.Column getTableColumn(Class<?> entity, String columnName) {
    return getTableColumn(getDeclared(entity, columnName));
  }

  public static Table.Column getTableColumn(Member member) {
    if (member == null) {
      log.warn("Member is null when getting the entity column, which it shouldn't!");
      return null;
    }
    return ifAnnotationIsPresent(member, getField(member).getAnnotation(Table.Column.class));
  }

  public static Field getField(Member member) {
    if (member instanceof InheritanceField)
      return ((InheritanceField) member).get_reflection();
    else if (member instanceof CommonField)
      return ((CommonField) member).get_reflection();
    else
      return (Field) member;
  }

  public static Junction get_junction(Class<?> clazz) {
    return ifAnnotationIsPresent(clazz, Junction.class, clazz.getAnnotation(Junction.class));
  }

  public static String get_databaseDefinitionTitle(Class<?> clazz) {
    return ifNotEmpty(handleTitle(clazz))
        .otherwise(defineTableTitle(clazz.getSimpleName()));
  }

  private static String handleTitle(Class<?> clazz) {
    if (clazz == null)
      return null;

    Annotation annotation = get_databaseDefinition(clazz)
        .orElseThrow(() -> new DatabaseDefinitionException(String.format(
            "Couldn't define database definition for %s.\nDoes it have the correct annotation?",
            clazz.getSimpleName()
        )));

    return stating(Liszt.of(
        Property.inCase(clazz.isAnnotationPresent(Table.class))
            .then(() -> ((Table) annotation).value()),
        Property.inCase(clazz.isAnnotationPresent(Junction.class))
            .then(() -> ((Junction) annotation).title()),
        Property.inCase(clazz.isAnnotationPresent(Table.Enum.class))
            .then(() -> ((Table.Enum) annotation).title())
    )).orElseNull();
  }

  public static boolean isIdless(Class<?> clazz) {
    return ifAnnotationIsPresent(clazz, Table.class, clazz.getAnnotation(Table.class).idLess());
  }

  private static Table.IdReference handleIdReference(Class<?> clazz) {
    Annotation annotation = get_databaseDefinition(clazz)
        .orElseThrow(() -> new IllegalStateException("Couldn't define database definition for " + clazz.getSimpleName()));

    return stating(Liszt.of(
        Property.inCase(clazz.isAnnotationPresent(Table.class))
            .then(() -> ((Table) annotation).idReference()),
        Property.inCase(clazz.isAnnotationPresent(Junction.class))
            .then(() -> ((Junction) annotation).idReference()),
        Property.inCase(clazz.isAnnotationPresent(Table.Enum.class))
            .then(() -> ((Table.Enum) annotation).idReference())
    )).orElseNull();
  }

  public static String getIdReference(Class<?> entity) {
    if (entity == null)
      return null;

    String idReference = handleIdReference(entity).value();

    return defineColumnTitle(ifNotEmpty(idReference)
        .otherwise(String.join("_", pluralToSingular(get_databaseDefinitionTitle(entity)), "id"))
    );
  }

  public static Seszt<Table.Column> getTableColumns(Class<?> clazz) {
    return new Seszt<>(Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Table.Column.class))
        .map(field -> field.getAnnotation(Table.Column.class))
    );
  }

  public static String getColumnTitle(Member member) {
    Field field = getField(member);

    if (!field.isAnnotationPresent(Table.Column.class))
      return defineColumnTitle(member.getName());

    String value = field.getAnnotation(Table.Column.class).value();

    return value.isEmpty() ? defineColumnTitle(member.getName()) : value;
  }

  public static String getTableTitle(Class<?> clazz, String tableName) {
    try {
      return getColumnTitle(clazz.getDeclaredField(tableName));
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  public static String toAlias(String tableName) {
    if (tableName == null || tableName.isEmpty()) {
      log.warn(
          "Table name is {} when creating alias, which it should not be!",
          tableName == null ? "null" : "empty"
      );
      return null;
    }

    boolean underscoreReached = false;
    String alias = tableName;

    for (int i = 0; i < alias.length(); i++) {
      char character = alias.charAt(i);
      if (character == '_') {
        underscoreReached = true;
        alias = alias.substring(0, i) + alias.substring(i + 1);
        i -= 1;
      } else if (underscoreReached) {
        alias = alias.substring(0, i) + String.valueOf(character).toUpperCase() + alias.substring(i + 1);
        underscoreReached = false;
      }
    }

    if (alias.equals(tableName)) {
      char[] chars = alias.toCharArray();
      chars[0] = Character.toLowerCase(chars[0]);
      alias = "_" + new String(chars);
    }

    return alias;
  }

  private static Table.Column ifAnnotationIsPresent(Member member, Table.Column element) {
    return stating(getField(member).isAnnotationPresent(Table.Column.class))
        .then(element)
        .orElse(get_defaultDatabaseEntityColumn(member.getName()));
  }

  private static Table.Column get_defaultDatabaseEntityColumn(String memberName) {
    return new Table.Column() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return Table.Column.class;
      }

      @Override
      public String value() {
        return fieldToColumnName(memberName);
      }

      @Override
      public boolean isPrimary() {
        return false;
      }
    };
  }

  private static <RETURN> RETURN ifAnnotationIsPresent(Class<?> clazz, Class<? extends Annotation> annotation, RETURN element) {
    if (!clazz.isAnnotationPresent(annotation))
      throw new IllegalStateException(String.format(
          "Class %s is not annotated with @%s and therefore can't get table!",
          clazz.getSimpleName(),
          annotation.getSimpleName()
      ));

    return element;
  }
}
