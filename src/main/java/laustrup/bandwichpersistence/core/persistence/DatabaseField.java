package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.worm.models.TableColumnData;
import laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.getIdColumnOf;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotEmpty;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;

public record DatabaseField(Table table, Column column) {

  private interface Unit {

    boolean contains(String value);

    String getKey();
  }

  private DatabaseField(Configuration configuration) {
    this(configuration.get_table(), configuration.get_column());
  }

  private static final String[] _idIndicators = new String[]{"id", "id"};

  public static Map<DatabaseField, String> toSelections(Map<? extends Member, DatabaseField> data) {
    return data.keySet().stream()
        .map(DatabaseField::of)
        .collect(Collectors.toMap(Function.identity(), DatabaseField::get_tableColumn));
  }

  public static DatabaseField of(Member member) {
    return new DatabaseField(new Table(member.getDeclaringClass()), new Column(member));
  }

  public static DatabaseField idOf(Class<?> clazz) {
    return new DatabaseField(new Table(clazz), new Column("id"));
  }

  public static DatabaseField referenceOf(Class<?> entity, Class<?> target) {
    return new DatabaseField(new Table(entity), new Column(getIdReference(target)));
  }

  public static DatabaseField of(Configuration configuration) {
    return new DatabaseField(configuration);
  }

  public String get_tableColumn() {
    return handleSelection(".");
  }

  public String get_columnAlias() {
    return handleSelection("_");
  }

  private String handleSelection(String delimiter) {
    return String.format("%s%s%s", table.title(), delimiter, column.title());
  }

  public boolean is_key() {
    return Arrays.stream(_idIndicators).anyMatch(column::contains);
  }

  public static class Configuration {

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
      this(entity, getTableColumnData(entity, columnName));
    }

    public static Configuration databaseFieldConfiguration(
        Class<?> entity,
        laustrup.bandwichpersistence.core.persistence.worm.annotations.Table.Column columnEntity
    ) {
      return new Configuration(entity, getTableColumnData(entity, columnEntity));
    }

    public static Configuration databaseFieldConfiguration(Class<?> entity, String columnName) {
      return new Configuration(entity, columnName);
    }

    public static Configuration databaseFieldConfigurationOfId(Class<?> entity) {
      if (entity == null)
        throw new IllegalArgumentException("Entity of database field configuration is null");

      return new Configuration(entity, getIdColumnOf(entity));
    }

    public Table get_table() {
      return new Table(_declaringClass);
    }

    public Column get_column() {
      return Column.of(_column);
    }
  }

  public record Column(String title, String alias) implements Unit {

    public Column(Member member) {
      this(getColumnTitle(member));
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
  }
}