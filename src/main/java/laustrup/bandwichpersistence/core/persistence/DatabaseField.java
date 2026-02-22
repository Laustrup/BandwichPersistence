package laustrup.bandwichpersistence.core.persistence;

import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.pluralTableNameAndColumnOfKey;
import static laustrup.bandwichpersistence.core.persistence.DatabaseField.SelectionHandlingConfiguration.singularTableName;
import static laustrup.bandwichpersistence.core.persistence.worm.services.DatabaseDefinitionService.get_databaseDefinitionTitle;
import static laustrup.bandwichpersistence.core.persistence.worm.services.NamingService.*;
import static laustrup.bandwichpersistence.core.services.EternaryService.ifNotNull;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;
import static laustrup.bandwichpersistence.core.services.StringService.firstCharacterAsUppercase;

public record DatabaseField(Table table, Column column) {

  private interface Unit {

    boolean contains(String value);

    String getKey();

    String get_title();
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
    return table.get_entity();
  }

  public Field getReflectedField() {
     return (Field) getMember();
  }

  public Member getMember() {
    return column.get_member();
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
    return Arrays.stream(_idIndicators)
        .anyMatch(column::contains);
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
          .orElse(unit::get_title);
    }
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Column implements Unit {

    private final Member _member;
    private final String _columnTitle;

    public Column(Member member) {
      this(member, toColumnTitle(member));
    }

    @Override
    public boolean contains(String value) {
      return get_title().contains(value) || _columnTitle.contains(value);
    }

    @Override
    public String getKey() {
      return ifNotNull(_columnTitle).otherwise(get_title());
    }

    @Override
    public String get_title() {
        return _member.getName();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      var that = (Column) obj;
      return Objects.equals(this._member, that._member) &&
          Objects.equals(get_title(), that.get_title()) &&
          Objects.equals(this._columnTitle, that._columnTitle);
    }

    @Override
    public int hashCode() {
      return Objects.hash(_member, get_title(), _columnTitle);
    }

    @Override
    public String toString() {
      return "Column[" +
          "member=" + _member + ", " +
          "title=" + get_title() + ", " +
          "alias=" + _columnTitle + ']';
    }
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Table implements Unit {
    private final Class<?> _entity;
    private final String _title;
    private final String _alias;

    public Table(Class<?> entity) {
      this(entity, get_databaseDefinitionTitle(entity));
    }

    private Table(Class<?> entity, String title) {
      this(entity, title, toTableAlias(title));
    }

    @Override
    public boolean contains(String value) {
      return _title.contains(value) || _alias.contains(value);
    }

    @Override
    public String getKey() {
      return ifNotNull(_alias).otherwise(_title);
    }

    @Override
    public String get_title() {
      return _title;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this)
        return true;
      if (obj == null || obj.getClass() != this.getClass())
        return false;

      var that = (Table) obj;

      return Objects.equals(this._entity, that._entity) &&
          Objects.equals(this._title, that._title) &&
          Objects.equals(this._alias, that._alias);
    }

    @Override
    public int hashCode() {
      return Objects.hash(_entity, _title, _alias);
    }

    @Override
    public String toString() {
      return "Table[" +
          "entity=" + _entity + ", " +
          "title=" + _title + ", " +
          "alias=" + _alias + ']';
    }
  }
}