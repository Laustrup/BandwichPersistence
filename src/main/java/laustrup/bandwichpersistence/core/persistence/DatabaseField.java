package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.worm.models.TableColumnData;
import laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;

import static laustrup.bandwichpersistence.core.persistence.DatabaseField.Configuration.Exception.noSuchColumn;
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.pluralTableNameAndColumnOfKey;
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.singularTableName;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseTableService.pluralToSingular;
import static laustrup.bandwichpersistence.core.services.EternaryService.*;
import static laustrup.bandwichpersistence.core.services.StringService.firstCharacterAsUppercase;

public record DatabaseField(Table table, Column column, Member member) {

  private interface Unit {

    boolean contains(String value);

    String getKey();

    String getTitle();
  }

  private static final String[] _idIndicators = new String[]{"id", "_id"};

  public DatabaseField(Member member) {
    this(new Table(member.getDeclaringClass()), new Column(member), member);
  }

  private DatabaseField(Configuration configuration) {
    this(configuration.get_table(), configuration.get_column(), configuration.get_member());
  }

  public static Seszt<DatabaseField> toSelections(Map<? extends Member, DatabaseField> data) {
    return new Seszt<>(data.keySet().stream()
        .map(DatabaseField::of)
    );
  }

  public static DatabaseField of(Member member) {
    return new DatabaseField(new Table(member.getDeclaringClass()), new Column(member), member);
  }

  public Class<?> getEntity() {
    return member.getDeclaringClass();
  }

  public Field convertToField() {
     return (Field) member();
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
    private final Member _member;
    private final laustrup.bandwichpersistence.core.persistence.worm.annotations.Table _table;
    private final TableColumnData _column;

    private Configuration(Member member, TableColumnData column) {
      if (member == null)
        throw new IllegalArgumentException("Entity member of database field configuration is null");
      if (column == null)
        throw new IllegalArgumentException("Column of database field configuration is null");

      _table = DatabaseDefinitionService.get_table(member.getDeclaringClass());

      if (_table == null)
        throw new IllegalArgumentException(String.format(
            "Could not define metadata from entity of %s in database field configuration!",
            member.getDeclaringClass().getSimpleName()
        ));

      _member = member;
      _column = column;
    }

    private Configuration(Member member, String columnName) {
      this(member, getTableColumnData(member.getDeclaringClass(), columnName)
          .orElseThrow(() -> noSuchColumn(member.getDeclaringClass(), columnName))
      );
    }

    public static Configuration databaseFieldConfiguration(
        Member member,
        laustrup.bandwichpersistence.core.persistence.worm.annotations.Table.Column columnEntity
    ) {
      Class<?> declaringClass = member.getDeclaringClass();

      return new Configuration(member, getTableColumnData(declaringClass, columnEntity)
          .orElseThrow(() -> Exception.noSuchColumn(declaringClass, getColumnTitle(declaringClass, columnEntity)
              .orElse(String.format("Column to '%s' class doesn't exist!", declaringClass.getSimpleName()))
          ))
      );
    }

    public static Configuration databaseFieldConfiguration(Member member, String columnName) {
      return new Configuration(member, columnName);
    }

    public Table get_table() {
      return new Table(_member.getDeclaringClass());
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