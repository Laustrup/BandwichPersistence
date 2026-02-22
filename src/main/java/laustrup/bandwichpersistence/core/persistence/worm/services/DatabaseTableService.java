package laustrup.bandwichpersistence.core.persistence.worm.services;

import java.util.List;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.persistence.worm.services.NamingService.handleDefineTitle;
import static laustrup.bandwichpersistence.core.persistence.worm.services.NamingService.pluralToSingular;

public abstract class DatabaseTableService {

  public static String defineTableTitle(String target, String common) {
    return String.join("_", List.of(pluralToSingular(target), common));
  }

  public static String defineTableTitle(String title) {
    return handleDefineTitle(title, true);
  }
  

  public static String defineIdReference(String title) {
    return defineIdReference(title, "");
  }

  public static String defineIdReference(String title, String idReference) {
    return idReference != null && !idReference.isEmpty()
        ? idReference
        : fieldToColumnName(pluralToSingular(title), "id");
  }
}
