package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.worm.models.TableColumnData;
import laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService;
import laustrup.bandwichpersistence.core.utilities.collections.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;

import static laustrup.bandwichpersistence.core.persistence.DatabaseField.Configuration.Exception.noSuchColumn;
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.pluralTableNameAndColumnOfKey;
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.singularTableName;
import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.getIdColumnsOf;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseTableService.pluralToSingular;
import static laustrup.bandwichpersistence.core.services.EternaryService.*;
import static laustrup.bandwichpersistence.core.services.StringService.firstCharacterAsUppercase;

public record DatabaseField(Table table, Column column, Class<?> entity) {

  private interface Unit {

    boolean contains(String value);

    String getKey();

    String getTitle();
  }

  private static final String[] _idIndicators = new String[]{"id", "_id"};

  private DatabaseField(Configuration configuration) {
    this(configuration.get_table(), configuration.get_column(), configuration.get_declaringClass());
  }

  public static Seszt<DatabaseField> toSelections(Map<? extends Member, DatabaseField> data) {
    return new Seszt<>(data.keySet().stream()
        .map(DatabaseField::of)
    );
  }

  public static DatabaseField of(Member member) {
    return new DatabaseField(new Table(member.getDeclaringClass()), new Column(member), member.getDeclaringClass());
  }

  public Field convertToField() {
    try {
      return entity.getDeclaredField(column().title());
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(toString(), e);
    }
  }

  public static DatabaseField idOf(Class<?> clazz) {
    return new DatabaseField(new Table(clazz), new Column("id"), clazz.getDeclaringClass());
  }

  public static DatabaseField referenceOf(Class<?> entity, Class<?> target) {
    return new DatabaseField(new Table(entity), new Column(getIdReference(target).orElseThrow()), entity);
  }

  public static DatabaseField of(Configuration configuration) {
    return new DatabaseField(configuration);
  }

  public String get_tableColumn() {
    return handleSelection(pluralTableNameAndColumnOfKey("."));
  }

  public String get_entityColumn() {
    return firstCharacterAsUppercase(handleSelection(singularTableName(".")));
  }

  public String get_columnAlias() {
    return handleSelection(pluralTableNameAndColumnOfKey("_"));
  }

  private String handleSelection(SelectionHandlingConfiguration configuration) {
    return String.format("%s%s%s",
        configuration.sanitize(table),
        configuration.delimiter(),
        configuration.keyOrTitle(column)
    );
  }

  public boolean is_key() {
    return Arrays.stream(_idIndicators).anyMatch(column::contains);
  }

  record SelectionHandlingConfiguration(String delimiter, boolean tableAsPlural, boolean ofKey) {

    static SelectionHandlingConfiguration pluralTableNameAndColumnOfKey(String delimiter) {
      return new SelectionHandlingConfiguration(delimiter, true, true);
    }

    static SelectionHandlingConfiguration singularTableName(String delimiter) {
      return new SelectionHandlingConfiguration(delimiter, false, false);
    }

    String sanitize(DatabaseField.Table table) {
      return stating(tableAsPlural)
          .then(table::getKey)
          .orElse(() -> pluralToSingular(table.getKey()));
    }

    public String keyOrTitle(Unit unit) {
      return stating(ofKey)
          .then(unit::getKey)
          .orElse(unit::getTitle);
    }
  }

  public static class Configuration {

    @Getter
    private final Class<?> _declaringClass;
    private final laustrup.bandwichpersistence.core.persistence.worm.annotations.Table _table;
    private final TableColumnData _column;

    private Configuration(Class<?> entity, TableColumnData column) {
      if (entity == null)
        throw new IllegalArgumentException("Entity of database field configuration is null");
      if (column == null)
        throw new IllegalArgumentException("Column of database field configuration is null");

      _table = DatabaseDefinitionService.get_table(entity);

      if (_table == null)
        throw new IllegalArgumentException(String.format(
            "Could not define metadata from entity of %s in database field configuration!",
            entity.getSimpleName()
        ));

      _declaringClass = entity;
      _column = column;
    }

    private Configuration(Class<?> entity, String columnName) {
      this(entity, getTableColumnData(entity, columnName)
          .orElseThrow(() -> noSuchColumn(entity, columnName))
      );
    }

    public static Configuration databaseFieldConfiguration(
        Class<?> entity,
        laustrup.bandwichpersistence.core.persistence.worm.annotations.Table.Column columnEntity
    ) {
      return new Configuration(entity, getTableColumnData(entity, columnEntity)
          .orElseThrow(() -> Exception.noSuchColumn(entity, getColumnTitle(entity, columnEntity)
              .orElse(String.format("Column to '%s' class doesn't exist!", entity.getSimpleName()))
          ))
      );
    }

    public static Configuration databaseFieldConfiguration(Class<?> entity, String columnName) {
      return new Configuration(entity, columnName);
    }

    public static Liszt<Configuration> databaseFieldConfigurationsOfId(Class<?> entity) {
      if (entity == null)
        throw new IllegalArgumentException("Entity of database field configuration is null");

      return Liszt.of(getIdColumnsOf(entity)
          .map(column -> databaseFieldConfiguration(entity, column))
      );
    }

    public Table get_table() {
      return new Table(_declaringClass);
    }

    public Column get_column() {
      return Column.of(_column);
    }

    public static class Exception extends RuntimeException {

      public Exception(String message) {
        super(message);
      }

      public static Exception noSuchColumn(Class<?> clazz, String columnName) {
        return new Exception(String.format("No such column: '%s' for class '%s'",
            columnName,
            clazz == null ? "null" : clazz.getSimpleName()
        ));
      }
    }
  }

  public record Column(String title, String alias) implements Unit {

    public Column(Member member) {
      this(member.getName(), getColumnTitle(member));
    }

    private Column(String title) {
      this(title, toAlias(title));
    }

    public static Column of(
        Member member,
        laustrup.bandwichpersistence.core.persistence.worm.annotations.Table.Column columnEntity
    ) {
      return new Column(ifNotEmpty(columnEntity.value())
          .otherwise(getColumnTitle(member))
      );
    }

    public static Column of(TableColumnData columnData) {
      return new Column(columnData.member());
    }

    @Override
    public boolean contains(String value) {
      return title.contains(value) || alias.contains(value);
    }

    @Override
    public String getKey() {
      return ifNotNull(alias).otherwise(title);
    }

    @Override
    public String getTitle() {
      return title();
    }
  }

  public record Table(String title, String alias) implements Unit {

    public Table(String title) {
      this(title, toAlias(title));
    }

    public Table(laustrup.bandwichpersistence.core.persistence.worm.annotations.Table entity) {
      this(entity.value(), toAlias(entity.value()));
    }

    public Table(Class<?> clazz) {
      this(get_databaseDefinitionTitle(clazz), toAlias(get_databaseDefinitionTitle(clazz)));
    }

    @Override
    public boolean contains(String value) {
      return title.contains(value) || alias.contains(value);
    }

    @Override
    public String getKey() {
      return ifNotNull(alias).otherwise(title);
    }

    @Override
    public String getTitle() {
      return title();
    }
  }
}