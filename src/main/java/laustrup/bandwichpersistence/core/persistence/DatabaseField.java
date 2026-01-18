package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.persistence.worm.models.TableColumnData;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;

import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.pluralTableNameAndColumnOfKey;
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.singularTableName;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.*;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseTableService.pluralToSingular;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;
import static laustrup.bandwichpersistence.core.services.StringService.firstCharacterAsUppercase;

public record DatabaseField(Table table, Column column) {

  private interface Unit {

    boolean contains(String value);

    String getKey();

    String getTitle();
  }

  private static final String[] _idIndicators = new String[]{"id", "_id"};

  public DatabaseField(Class<?> entity, Member member) {
    this(new Table(entity), new Column(member));
  }

  public DatabaseField(Member member) {
    this(new Table(member.getDeclaringClass()), new Column(member));
  }

  public static Seszt<DatabaseField> toSelections(Map<? extends Member, DatabaseField> data) {
    return new Seszt<>(data.keySet().stream()
        .map(DatabaseField::of)
    );
  }

  public static DatabaseField of(Class<?> entity, Member member) {
    return new DatabaseField(new Table(entity), new Column(member));
  }

  public static DatabaseField of(Member member) {
    return new DatabaseField(new Table(member.getDeclaringClass()), new Column(member));
  }

  public Class<?> getEntity() {
    return table.entity();
  }

  public Field getReflectedField() {
     return (Field) column.member();
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

  public record Column(Member member, String title, String alias) implements Unit {

    public Column(Member member) {
      this(member, member.getName(), getColumnTitle(member));
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

  public record Table(Class<?> entity, String title, String alias) implements Unit {

    public Table(Class<?> entity) {
      this(entity, get_databaseDefinitionTitle(entity));
    }

    private Table(Class<?> entity, String title) {
      this(entity, title, toAlias(title));
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