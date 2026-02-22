package laustrup.bandwichpersistence.core.persistence.worm.services;

import laustrup.bandwichpersistence.core.persistence.exceptions.DatabaseDefinitionException;
import laustrup.bandwichpersistence.core.persistence.models.members.CommonField;
import laustrup.bandwichpersistence.core.persistence.models.members.InheritanceField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Junction;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.persistence.worm.models.TableColumnData;
import laustrup.bandwichpersistence.core.services.ClassFieldService;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Optional;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseTableService.defineTableTitle;
import static laustrup.bandwichpersistence.core.persistence.worm.services.NamingService.pluralToSingular;
import static laustrup.bandwichpersistence.core.services.ClassFieldService.getFields;
import static laustrup.bandwichpersistence.core.services.EternaryService.Operator.Property.inCase;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

@Slf4j
public abstract class DatabaseDefinitionService {

  public static Optional<Annotation> get_databaseDefinition(Class<?> clazz) {
    if (clazz == null)
      throw new NullPointerException("Class can't be null when getting its database definition!");

    return stating(Liszt.of(
        inCase(clazz.isAnnotationPresent(Table.class))
            .then(() -> get_table(clazz)),
        inCase(clazz.isAnnotationPresent(Junction.class))
            .then(() -> get_junction(clazz)),
        inCase(clazz.isAnnotationPresent(Table.Enum.class))
            .then(() -> get_tableEnum(clazz)),
        inCase(clazz.isAnnotationPresent(Table.Constructor.class))
            .then(() -> get_constructor(clazz)),
        inCase(clazz.isAnnotationPresent(Table.Skeleton.class))
            .then(() -> clazz.getAnnotation(Table.Skeleton.class))
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

  public static Optional<TableColumnData> getTableColumnData(Class<?> clazz, Table.Column column) {
    return ClassFieldService.getField(clazz, column)
        .flatMap(field -> getTableColumnData(clazz, field.getName()));
  }

  public static Optional<TableColumnData> getTableColumnData(Class<?> clazz, String columnName) {
    return getTableColumn(clazz, columnName)
        .map(tableColumn -> new TableColumnData(
            tableColumn,
            ClassFieldService.getField(clazz, columnName)
        ));
  }

  public static Optional<Table.Column> getTableColumn(Class<?> entity, String columnName) {
    return getTableColumn(ClassFieldService.getField(entity, columnName));
  }

  public static Optional<Table.Column> getTableColumn(Member member) {
    if (member == null) {
      log.warn("Member is null when getting the entity column, which it shouldn't!");
      return Optional.empty();
    }

    Field field = getField(member);

    return stating(!field.isAnnotationPresent(Table.ExcludedColumn.class))
        .then(() -> ifAnnotationIsPresent(member, getField(member).getAnnotation(Table.Column.class)))
        .orEmpty();
  }

  public static boolean idReferenceReferencesTable(Table.IdReference idReference, Class<?> entity) {
    Table table = get_table(entity);

    if (table == null || idReference == null)
      return false;

    return idReference.value().contains(pluralToSingular(table.value()));
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
        inCase(clazz.isAnnotationPresent(Table.class))
            .then(() -> ((Table) annotation).value()),
        inCase(clazz.isAnnotationPresent(Junction.class))
            .then(() -> ((Junction) annotation).title()),
        inCase(clazz.isAnnotationPresent(Table.Enum.class))
            .then(() -> ((Table.Enum) annotation).title())
    )).orElseNull();
  }

  public static boolean isIdless(Class<?> clazz) {
    return getFields(clazz).values().stream().noneMatch(DatabaseDefinitionService::fieldIsPrimary);
  }

  public static boolean fieldIsPrimary(Field field) {
    Table.Column column = getTableColumn(field)
        .orElse(null);

    return column != null && column.isPrimary();
  }

  public static String intendedNameOf(Member member) {
    Field field = getField(member);

    if (field == null)
      return "";

    Table.Column tableColumn = field.getAnnotation(Table.Column.class);

    return tableColumn != null
        ? tableColumn.value()
        : field.getName();
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
