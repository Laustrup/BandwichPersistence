package laustrup.bandwichpersistence.core.persistence.models.members;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;

import java.lang.reflect.Member;

public class SimpleField implements Member {

  protected final Class<?> _classOfReflection;
  protected final String _name;

  public SimpleField(Class<?> declaringClass, String name) {
    _classOfReflection = declaringClass;
    _name = name;
  }

  public SimpleField(Class<?> declaringClass, Table.Column column) {
    this(declaringClass, column.value());
  }

  public SimpleField(Class<?> clazz, DatabaseField databaseField) {
    this(clazz, databaseField.column().get_title());
  }

  @Override
  public Class<?> getDeclaringClass() {
    return _classOfReflection;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public int getModifiers() {
    return 0;
  }

  @Override
  public boolean isSynthetic() {
    return false;
  }
}
