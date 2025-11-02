package laustrup.bandwichpersistence.core.persistence.worm.models;

import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;

import java.lang.reflect.Member;

import static laustrup.bandwichpersistence.core.services.ClassFieldService.getField;

public record TableColumnData(Table.Column column, Member member) {

  public static TableColumnData of(Table.Column column, Member member) {
    return new TableColumnData(column, member);
  }

  public static TableColumnData of(Class<?> clazz, Table.Column column) {
    return new TableColumnData(column, getField(clazz, column).orElseThrow());
  }
}
