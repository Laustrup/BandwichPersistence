package laustrup.bandwichpersistence.core.persistence.models.members;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;

import java.lang.reflect.Member;

import static laustrup.bandwichpersistence.core.persistence.worm.services.IdReferenceService.defaultIdReference;
import static laustrup.bandwichpersistence.core.persistence.worm.services.IdReferenceService.getIdReferenceTitle;

public class IdReferenceMember extends SimpleField implements Member {

  public IdReferenceMember(Class<?> clazz, String name) {
    super(clazz, name);
  }

  public IdReferenceMember(Class<?> clazz, Table.IdReference idReference) {
    super(clazz, getIdReferenceTitle(clazz, idReference));
  }

  public IdReferenceMember(Class<?> clazz, Table.Column column) {
    super(clazz, column);
  }

  public IdReferenceMember(Class<?> clazz, DatabaseField databaseField) {
    super(clazz, databaseField);
  }

  public String get_name() {
    return _name != null
        ? _name
        : defaultIdReference(_classOfReflection).get();
  }
}
