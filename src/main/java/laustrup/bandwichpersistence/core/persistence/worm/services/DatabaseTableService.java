package laustrup.bandwichpersistence.core.persistence.worm.services;

import laustrup.bandwichpersistence.core.services.EternaryService.Operator.Property;

import java.util.List;

import static laustrup.bandwichpersistence.core.persistence.services.DatabaseColumnService.fieldToColumnName;
import static laustrup.bandwichpersistence.core.services.EternaryService.stating;

public abstract class DatabaseTableService {

  public static String defineTableTitle(String target, String common) {
    return String.join("_", List.of(pluralToSingular(target), common));
  }

  public static String defineTableTitle(String title) {
    return handleDefineTitle(title, true);
  }

  public static String defineColumnTitle(String title) {
    return handleDefineTitle(title, false);
  }

  private static String handleDefineTitle(String title, boolean isTable) {
    title = title.replaceAll("([A-Z])", "_$1");

    for (char c : title.toCharArray()) {
      if (c == '_')
        title = title.substring(1);
      else
        break;
    }

    title = title.toLowerCase();

    return isTable ? singularToPlural(title) : title;
  }

  public static String singularToPlural(String title) {
    if (title == null || (title.endsWith("s") && !title.endsWith("ss")) || title.endsWith("info") || title.endsWith("data"))
      return title;

    return stating(title.endsWith("y"))
        .then(title.substring(0, title.length() - 1) + "ies")
        .or(Property.inCase(title.endsWith("ss")).then(title + "es"))
        .orElse(title + "s");
  }

  public static String pluralToSingular(String title) {
    if (title == null || title.length() < 2)
      throw new IllegalArgumentException("Entity title is empty when trying to make it singular");

    String ending = title.substring(title.length() - 3);
    return stating(title.length() > 4 && ending.equals("ies"))
        .then(title.substring(0, title.length() - 3) + "y")
        .or(Property.inCase(ending.endsWith("ses")).then(title.substring(0, title.length() - 2)))
        .orElse(() -> stating(ending.endsWith("s"))
            .then(title.substring(0, title.length() - 1))
            .orElse(title)
        );
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
